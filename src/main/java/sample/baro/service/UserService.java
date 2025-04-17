package sample.baro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sample.baro.domain.User;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.UserSignupResponse;
import sample.baro.exception.UserCustomException;
import sample.baro.repsitory.UserRepository;

import static sample.baro.exception.ErrorCode.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserSignupResponse signup(UserSignupRequest request) {
        boolean isExist = userRepository.existByUsername(request.username());

        if (isExist) {
            throw new UserCustomException(USER_ALREADY_EXISTS);
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();
        User savedUser = userRepository.save(user);
        return UserSignupResponse.from(savedUser);
    }
}
