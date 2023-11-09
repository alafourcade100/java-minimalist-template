package py.com.code100.applications.web.shared;

import org.mapstruct.factory.Mappers;
import py.com.code100.domain.entities.User;
import py.com.code100.infrastructure.domain.response.GetEntitiesResponse;
import py.com.code100.infrastructure.domain.response.PaginationResponse;

import java.util.List;

public class GetUsersQueryResponse extends GetEntitiesResponse<GetUserQueryResponse> {
    public GetUsersQueryResponse(List<User> entities, PaginationResponse pagination) {
        super(Mappers.getMapper(GetUserQueryResponseMapper.class).toDtoList(entities), pagination);
    }
}