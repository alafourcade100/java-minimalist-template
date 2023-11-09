package py.com.code100.infrastructure.persistence.base;

import org.hibernate.Hibernate;
import org.mapstruct.Condition;
import org.mapstruct.Context;
import org.mapstruct.InheritInverseConfiguration;
import org.springframework.data.domain.Page;
import py.com.code100.infrastructure.persistence.configuration.CycleAvoidingMappingContext;
import py.com.code100.infrastructure.persistence.configuration.DoIgnore;

import java.util.Collection;
import java.util.List;


public interface BaseJpaMapper<D, JPA> {

    D toDomainModel(JPA jpaModel, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default D toDomainModel(JPA jpaModel) {
        return toDomainModel(jpaModel, new CycleAvoidingMappingContext());
    }

    @InheritInverseConfiguration
    JPA toJpaModel(D domainModel, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default JPA toJpaModel(D domainModel) {
        return toJpaModel(domainModel, new CycleAvoidingMappingContext());
    }

    List<D> toDomainModel(List<JPA> list, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default List<D> toDomainModel(List<JPA> list) {
        return toDomainModel(list, new CycleAvoidingMappingContext());
    }

    @InheritInverseConfiguration
    List<JPA> toJpaModel(List<D> list, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    default List<JPA> toJpaModel(List<D> list) {
        return toJpaModel(list, new CycleAvoidingMappingContext());
    }

    default Page<D> toDomainModel(Page<JPA> page) {
        return page.map(entity -> toDomainModel(entity, new CycleAvoidingMappingContext()));
    }

    @Condition
    default  <T> boolean isLazyCollectionAvailable(Collection<T> collection) {
        if (collection == null) {
            return false;
        }
        return Hibernate.isInitialized(collection);
    }
}
