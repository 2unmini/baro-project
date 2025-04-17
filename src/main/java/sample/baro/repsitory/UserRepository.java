package sample.baro.repsitory;

import sample.baro.domain.User;

public interface UserRepository {

    boolean existByUsername(String username);

    User save(User user);

}
