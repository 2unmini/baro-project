package sample.baro.repsitory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import sample.baro.auth.CustomUserDetails;
import sample.baro.domain.User;
import sample.baro.exception.UserCustomException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static sample.baro.exception.ErrorCode.NOT_FOUND_USER;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public boolean existByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User save(User user) {
        Long userId = id.getAndIncrement();
        log.info("유저 id = {}", userId);

        User savedUser = User.builder()
                .id(userId)
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
        userMap.put(userId, savedUser);


        return savedUser;
    }

    @Override
    public UserDetails findByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return new CustomUserDetails(user);
            }
        }
        throw new UserCustomException(NOT_FOUND_USER);
    }
}
