package sample.baro.repository;

import sample.baro.domain.User;

import java.util.Optional;

public interface UserRepository {

    boolean existByUsername(String username);

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);
}
