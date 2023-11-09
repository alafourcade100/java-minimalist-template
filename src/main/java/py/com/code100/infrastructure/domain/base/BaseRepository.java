package py.com.code100.infrastructure.domain.base;

import py.com.code100.infrastructure.domain.response.GetEntitiesResponse;
import py.com.code100.infrastructure.persistence.specification.Criteria;

import java.util.UUID;

public interface BaseRepository<TEntity extends BaseEntity> {
    TEntity add(TEntity entity);
    void delete(UUID id);
    TEntity update(TEntity entity);
    TEntity getById(UUID id);
    long count();
    GetEntitiesResponse<TEntity> getAll(Criteria criteria);
}
