package py.com.code100.infrastructure.domain.response;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetEntitiesResponse<T> {

    private List<T> entities;
    private final PaginationResponse pagination;

    public GetEntitiesResponse(List<T> entities, PaginationResponse pagination) {
        this.entities = entities;
        this.pagination = pagination;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public PaginationResponse getPagination() {
        return pagination;
    }
}