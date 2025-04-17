package sample.baro.repsitory;

import org.springframework.security.core.userdetails.UserDetails;
import sample.baro.domain.User;

public interface UserRepository {

    boolean existByUsername(String username);

    User save(User user);

    UserDetails findByUsername(String username);
}
