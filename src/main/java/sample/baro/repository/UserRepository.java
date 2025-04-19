package sample.baro.repository;

import org.springframework.security.core.userdetails.UserDetails;
import sample.baro.domain.User;

import java.util.Optional;

public interface UserRepository {

    boolean existByUsername(String username);

    User save(User user);

    UserDetails findByUsername(String username);

    Optional<User> findById(Long userId);
}
