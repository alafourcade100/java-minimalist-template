package py.com.code100.infrastructure.persistence.base;

import org.springframework.stereotype.Component;
import py.com.code100.infrastructure.persistence.specification.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class FilterPaginationQueryModel {

    private final List<String> filters;
    private final List<String> orders;
    private int page;
    private int pageSize;
    protected List<Filter> customFilters;
    protected List<OrderBy> customOrders;

    public FilterPaginationQueryModel() {
        this.customFilters = new ArrayList<>();
        this.customOrders = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public FilterPaginationQueryModel(List<String> filters, List<String> orders, int page, int pageSize) {
        this.filters = filters == null ? new ArrayList<>() : filters;
        this.orders = orders == null ? new ArrayList<>() : orders;
        this.page = page;
        this.pageSize = pageSize;
    }

    public void addCustomFilters(List<Filter> customFilters) {
        this.customFilters.addAll(customFilters);
    }

    public void addCustomOrders(List<OrderBy> customOrders) {
        this.customOrders.addAll(customOrders);
    }

    public List<String> getFilters() {
        return this.filters;
    }

    public List<String> getOrders() {
        return this.orders;
    }

    public int getPage() {
        return this.page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public List<Filter> constructFilters() throws Exception {

        var result = new ArrayList<Filter>();
        if (filters.isEmpty()) {
            if (customFilters != null && !customFilters.isEmpty()) {
                result.addAll(customFilters);
            }

            return result;
        }

        for (var filterString : filters) {

            var filterParameterList = filterString.split(Pattern.quote("__"));
            if (filterParameterList.length != 4) {
                throw new Exception("filter format not valid");
            }

            if (customFilters != null && customFilters.stream().anyMatch(f -> f.getFieldName().equals(filterParameterList[0]))) {
                continue;
            }

            var field = filterParameterList[0];
            var operator = filterParameterList[1];
            var valueStr = filterParameterList[2];
            var concat = filterParameterList[3];

            Filter filter = null;
            Object value = null;

            if(valueStr.matches("[-+]?[0-9]*\\.?[0-9]+")){
                if (valueStr.contains(".")){
                    value = Double.valueOf(valueStr);
                }
                else {
                    value = Integer.valueOf(valueStr);
                }
            }
            else if (valueStr.toLowerCase().startsWith("list(") && operator.toUpperCase().contains(FilterOperator.CONTAINS.name())){
                valueStr = valueStr.substring(5, valueStr.length() - 6);
                value = Arrays.stream(valueStr.split(";")).toList();
            }
            else{
                try {
                    var format = new SimpleDateFormat("dd/MM/yyyy");
                    value = format.parse(valueStr);
                }
                catch (Exception ignored){
                }

                try {
                    var format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    value = format.parse(valueStr);
                }
                catch (Exception ignored){
                }

                try {
                    value = UUID.fromString(valueStr);
                }
                catch (Exception ignored){
                }

                if (value == null){
                    value = valueStr;
                }
            }

            filter = new Filter(field, FilterOperator.valueOf(operator.toUpperCase()), value, FilterConcat.valueOf(concat.toUpperCase()));
            result.add(filter);
        }

        if (customFilters != null) {
            result.addAll(customFilters);
        }

        return result;
    }

    public List<OrderBy> constructOrders() {

        var result = new ArrayList<OrderBy>();
        if (orders.isEmpty()) {
            if (customOrders != null && !customOrders.isEmpty()) {
                result.addAll(customOrders);
            }

            return result;
        }

        for (var orderString : orders) {
            var parameters = orderString.split(Pattern.quote("__"));
            if (parameters.length != 2) {
                //throw exception format invalid
                continue;
            }

            if (customOrders != null && customOrders.stream().anyMatch(f -> f.getFieldName().equals(parameters[0]))) {
                continue;
            }

            var orderBy = new OrderBy(parameters[0], OrderType.valueOf(parameters[1]));
            result.add(orderBy);
        }

        if (customOrders != null) {
            result.addAll(customOrders);
        }

        return result;
    }
}
