package py.com.code100.applications.web.shared;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import py.com.code100.domain.entities.User;

import java.util.List;

@Mapper
public interface GetUserQueryResponseMapper {
    @InheritConfiguration
    User toEntity(GetUserQueryResponse model);
    @InheritConfiguration
    List<User> toEntities(List<GetUserQueryResponse> models);
    @InheritConfiguration
    GetUserQueryResponse toDto(User entity);
    @InheritConfiguration
    List<GetUserQueryResponse> toDtoList(List<User> entities);
}
