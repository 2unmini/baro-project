package sample.baro.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sample.baro.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        User savedUser = User.builder()
                .id(userId)
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .role(user.getRole())
                .build();

        log.info("새로운 사용자 저장 = {}", savedUser.getNickname());

        userMap.put(userId, savedUser);


        return savedUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        for (User user : userMap.values()) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }
}
