package sample.baro.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.baro.UserTestBuilder;
import sample.baro.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    @DisplayName("사용자를 저장하면 ID가 자동 부여되고 조회 가능하다.")
    void saveUser() {
        // given
        User user = UserTestBuilder.defaultUser();

        // when
        User savedUser = userRepository.save(user);

        // then
        User foundUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertThat(foundUser.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("사용자가 존재하면 True를 반환한다.")
    void existByUsername() {
        // given
        User user = UserTestBuilder.defaultUser();

        // when
        userRepository.save(user);

        // then
        boolean result = userRepository.existByUsername("JIN HO");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자가 존재하지 않다면 False를 반환한다.")
    void NotExistByUsername() {
        // given
        // when
        // then
        boolean result = userRepository.existByUsername("JIN HO");

        assertThat(result).isFalse();
    }
}
