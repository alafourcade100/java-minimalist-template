package py.com.code100.domain.services.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import py.com.code100.domain.entities.User;
import py.com.code100.domain.repositories.user.UserRepository;
import py.com.code100.infrastructure.domain.base.BaseService;

@Service
@RequiredArgsConstructor
public class CrudUserService extends BaseService<User, UserRepository> {



}
