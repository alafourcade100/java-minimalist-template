package py.com.code100.infrastructure.persistence.specification;

public class Filter {
    private String fieldName;
    private FilterOperator filterOperator;
    private Object value;
    private FilterConcat filterConcat;

    public Filter() {
        this.fieldName = null;
        this.filterOperator = null;
        this.value = null;
        this.filterConcat = null;
    }

    public Filter(String fieldName, FilterOperator filterOperator, Object value) {
        this.fieldName = fieldName;
        this.filterOperator = filterOperator;
        this.value = value;
        this.filterConcat = FilterConcat.AND;
    }

    public Filter(String fieldName, FilterOperator filterOperator, Object value, FilterConcat filterConcat) {
        this.fieldName = fieldName;
        this.filterOperator = filterOperator;
        this.value = value;
        this.filterConcat = filterConcat;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FilterConcat getFilterConcat() {
        return filterConcat;
    }

    public void setFilterConcat(FilterConcat filterConcat) {
        this.filterConcat = filterConcat;
    }

    public FilterOperator getFilterOperator() {
        return filterOperator;
    }

    public void setFilterOperator(FilterOperator filterOperator) {
        this.filterOperator = filterOperator;
    }
}
