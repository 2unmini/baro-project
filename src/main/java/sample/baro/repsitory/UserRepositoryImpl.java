package sample.baro.repsitory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sample.baro.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
}
