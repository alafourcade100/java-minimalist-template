package py.com.code100.infrastructure.domain.base;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import py.com.code100.infrastructure.domain.response.GetEntitiesResponse;
import py.com.code100.infrastructure.domain.response.ProcessResponse;
import py.com.code100.infrastructure.domain.response.Response;
import py.com.code100.infrastructure.persistence.base.FilterPaginationQueryModel;
import py.com.code100.infrastructure.persistence.specification.Criteria;
import py.com.code100.infrastructure.utils.ReflectionUtils;

import java.util.UUID;

public class BaseService<TEntity extends BaseEntity, DomainRepository extends BaseRepository<TEntity>> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected DomainRepository getDomainRepository() {
        Class<?> clase = ReflectionUtils.getParameterizedClass(getClass(), 1);
        return (DomainRepository) applicationContext.getBean(clase);
    }

    @Transactional
    public Response<TEntity> add(TEntity entidad) {
        DomainRepository repository = getDomainRepository();
        var processResponse = new ProcessResponse<TEntity>();
        repository.add(entidad);
        return processResponse.success(entidad);
    }

    @Transactional
    public Response<TEntity> actualizar(TEntity entidad) {
        DomainRepository repository = getDomainRepository();
        var processResponse = new ProcessResponse<TEntity>();
        var entity = repository.getById(entidad.getId());
        if(entity == null) {
            return processResponse.error("Entity not found");
        }
        Response<TEntity> entidadBD = this.obtenerPorId(entidad.getId());
        BeanUtils.copyProperties(entidad, entidadBD);
        repository.update(entidadBD.getValue());
        return processResponse.success(entidadBD.getValue());
    }

    public Response<TEntity> obtenerPorId(UUID id) {

        DomainRepository repository = getDomainRepository();
        var processResponse = new ProcessResponse<TEntity>();
        var entity = repository.getById(id);
        return processResponse.success(entity);
    }


    public Response<GetEntitiesResponse> listar(FilterPaginationQueryModel filterPagination) {

        var processResponse = new ProcessResponse<GetEntitiesResponse>();

        Criteria criteria;
        try {
            criteria = new Criteria(filterPagination);
        } catch (Exception e) {
            return processResponse.error(e);
        }
        DomainRepository repository = getDomainRepository();
        var entitiesResponse = repository.getAll(criteria);
        var response = new GetEntitiesResponse(entitiesResponse.getEntities(), entitiesResponse.getPagination());

        return processResponse.success(response);
    }
}
