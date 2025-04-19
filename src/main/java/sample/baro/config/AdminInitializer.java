package sample.baro.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import sample.baro.domain.Role;
import sample.baro.domain.User;
import sample.baro.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminAccount() {

        User admin = User.builder()
                .username("ADMIN")
                .password(passwordEncoder.encode("ADMIN"))
                .nickname("관리자")
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
