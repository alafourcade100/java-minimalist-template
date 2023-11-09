package py.com.code100.infrastructure.persistence.specification;

import py.com.code100.infrastructure.persistence.base.FilterPaginationQueryModel;

import java.util.ArrayList;
import java.util.List;

public class Criteria {
    private List<Filter> filters;
    private List<OrderBy> orders;
    private int page;
    private int pageSize;
    private static final int MAX_PAGE_SIZE = 1000;

    public Criteria() {
        this.filters = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.page = 0;
        this.pageSize = Criteria.MAX_PAGE_SIZE;
    }

    public Criteria(List<Filter> filters) {
        this.filters = filters;
        this.orders = new ArrayList<>();
        this.page = 0;
        this.pageSize = Criteria.MAX_PAGE_SIZE;
    }

    public Criteria(List<Filter> filters, List<OrderBy> orders) {
        this.filters = filters;
        this.orders = orders;
        this.page = 0;
        this.pageSize = Criteria.MAX_PAGE_SIZE;
    }

    public Criteria(List<Filter> filters, List<OrderBy> orders, int page, int pageSize) {
        this.filters = filters;
        this.orders = orders;
        this.page = page;
        this.pageSize = pageSize == 0 ? Criteria.MAX_PAGE_SIZE : pageSize;
    }

    public Criteria(FilterPaginationQueryModel filterPaginationQueryModel) throws Exception {
        if (filterPaginationQueryModel == null){
            this.filters = new ArrayList<>();
            this.orders = new ArrayList<>();
            this.page = 0;
            this.pageSize = Criteria.MAX_PAGE_SIZE;
        }
        else{
            this.filters = filterPaginationQueryModel.constructFilters();
            this.orders = filterPaginationQueryModel.constructOrders();
            this.page = filterPaginationQueryModel.getPage();
            var pageSize  = filterPaginationQueryModel.getPageSize();
            this.pageSize = pageSize == 0 ? Criteria.MAX_PAGE_SIZE : pageSize;
        }
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Criteria setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }

    public List<OrderBy> getOrders() {
        return orders;
    }

    public Criteria setOrders(List<OrderBy> orders) {
        this.orders = orders;
        return this;
    }

    public int getPage() {
        return page;
    }

    public Criteria setPage(int page) {
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Criteria setPageSize(int pageSize) {
        if (pageSize > Criteria.MAX_PAGE_SIZE){
            pageSize = Criteria.MAX_PAGE_SIZE;
        }
        this.pageSize = pageSize;
        return this;
    }
}
