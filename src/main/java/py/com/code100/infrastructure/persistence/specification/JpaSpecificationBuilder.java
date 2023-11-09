package py.com.code100.infrastructure.persistence.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class JpaSpecificationBuilder<TDbModel> {
    public Specification<TDbModel> createSpecification(Criteria criteria) {
        if (criteria == null || CollectionUtils.isEmpty(criteria.getFilters())) {
            return null;
        }
        Specification<TDbModel> specification = null;
        Specification<TDbModel> currentSpecification;
        List<Filter> filters = criteria.getFilters();
        for (Filter filter : filters) {
            currentSpecification = buildCurrentSpecification(filter);
            if (specification != null) {
                specification = filter.getFilterConcat() == FilterConcat.OR
                        ? specification.or(currentSpecification)
                        : specification.and(currentSpecification);
                continue;
            }
            specification = currentSpecification;
        }
        return specification;
    }

    private Specification<TDbModel> buildCurrentSpecification(Filter filter) {

        if (filter.getFilterOperator() == FilterOperator.EQ) {
            return filter.getValue().getClass() == Integer.class || filter.getValue().getClass() == Double.class || filter.getValue().getClass() == Float.class ||filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.equal(entity.get(filter.getFieldName()), filter.getValue())
                    : filter.getValue().getClass() == String.class
                    ? (entity, cq, cb) -> cb.equal(cb.lower(entity.get(filter.getFieldName())), filter.getValue().toString().toLowerCase())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.NEQ) {
            return filter.getValue().getClass() == Integer.class || filter.getValue().getClass() == Double.class || filter.getValue().getClass() == Float.class ||filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.notEqual(entity.get(filter.getFieldName()), filter.getValue())
                    : filter.getValue().getClass() == String.class
                    ? (entity, cq, cb) -> cb.notEqual(cb.lower(entity.get(filter.getFieldName())), filter.getValue().toString().toLowerCase())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.GTE) {
            return filter.getValue().getClass() == Integer.class
                    ? (entity, cq, cb) -> cb.greaterThanOrEqualTo(entity.get(filter.getFieldName()), (Integer) filter.getValue())
                    : filter.getValue().getClass() == Float.class
                    ? (entity, cq, cb) -> cb.greaterThanOrEqualTo(entity.get(filter.getFieldName()), (Float) filter.getValue())
                    : filter.getValue().getClass() == Double.class
                    ? (entity, cq, cb) -> cb.greaterThanOrEqualTo(entity.get(filter.getFieldName()), (Double) filter.getValue())
                    : filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.greaterThanOrEqualTo(entity.get(filter.getFieldName()), (Date) filter.getValue())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.LTE) {
            return filter.getValue().getClass() == Integer.class
                    ? (entity, cq, cb) -> cb.lessThanOrEqualTo(entity.get(filter.getFieldName()), (Integer) filter.getValue())
                    : filter.getValue().getClass() == Float.class
                    ? (entity, cq, cb) -> cb.lessThanOrEqualTo(entity.get(filter.getFieldName()), (Float) filter.getValue())
                    : filter.getValue().getClass() == Double.class
                    ? (entity, cq, cb) -> cb.lessThanOrEqualTo(entity.get(filter.getFieldName()), (Double) filter.getValue())
                    : filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.lessThanOrEqualTo(entity.get(filter.getFieldName()), (Date) filter.getValue())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.GT) {
            return filter.getValue().getClass() == Integer.class
                    ? (entity, cq, cb) -> cb.greaterThan(entity.get(filter.getFieldName()), (Integer) filter.getValue())
                    : filter.getValue().getClass() == Float.class
                    ? (entity, cq, cb) -> cb.greaterThan(entity.get(filter.getFieldName()), (Float) filter.getValue())
                    : filter.getValue().getClass() == Double.class
                    ? (entity, cq, cb) -> cb.greaterThan(entity.get(filter.getFieldName()), (Double) filter.getValue())
                    : filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.greaterThan(entity.get(filter.getFieldName()), (Date) filter.getValue())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.LT) {
            return filter.getValue().getClass() == Integer.class
                    ? (entity, cq, cb) -> cb.lessThan(entity.get(filter.getFieldName()), (Integer) filter.getValue())
                    : filter.getValue().getClass() == Float.class
                    ? (entity, cq, cb) -> cb.lessThan(entity.get(filter.getFieldName()), (Float) filter.getValue())
                    : filter.getValue().getClass() == Double.class
                    ? (entity, cq, cb) -> cb.lessThan(entity.get(filter.getFieldName()), (Double) filter.getValue())
                    : filter.getValue().getClass() == Date.class
                    ? (entity, cq, cb) -> cb.lessThan(entity.get(filter.getFieldName()), (Date) filter.getValue())
                    : null;

        } else if (filter.getFilterOperator() == FilterOperator.CONTAINS) {
            return (entity, cq, cb) -> cb.like(cb.lower(entity.get(filter.getFieldName())), "%"+filter.getValue().toString().toLowerCase()+"%");

        } else if (filter.getFilterOperator() == FilterOperator.NOT_CONTAINS) {
            return (entity, cq, cb) -> cb.notLike(cb.lower(entity.get(filter.getFieldName())), "%"+filter.getValue().toString().toLowerCase()+"%");
        }

        return null;
    }

}
