package py.com.code100.persistence.mapper;

import org.mapstruct.Mapper;
import py.com.code100.domain.entities.User;
import py.com.code100.infrastructure.persistence.base.BaseJpaMapper;
import py.com.code100.persistence.model.UserModelJPA;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseJpaMapper<User, UserModelJPA> {

}
