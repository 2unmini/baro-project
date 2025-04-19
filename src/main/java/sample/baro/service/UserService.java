package sample.baro.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sample.baro.auth.CustomUserDetails;
import sample.baro.auth.jwt.JwtUtil;
import sample.baro.auth.jwt.TokenResponse;
import sample.baro.domain.User;
import sample.baro.dto.request.UserLoginRequest;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.UserRoleAssignResponse;
import sample.baro.dto.response.UserSignupResponse;
import sample.baro.exception.UserCustomException;
import sample.baro.repository.UserRepository;

import static sample.baro.domain.Role.USER;
import static sample.baro.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignupResponse signup(UserSignupRequest request) {
        boolean isExist = userRepository.existByUsername(request.username());

        if (isExist) {
            throw new UserCustomException(USER_ALREADY_EXISTS);
        }
        User user = createUser(request);
        User savedUser = userRepository.save(user);
        return UserSignupResponse.from(savedUser);
    }

    public TokenResponse login(@Valid UserLoginRequest userLoginRequest) {
        CustomUserDetails userDetails = loadUserByUsername(userLoginRequest.username());
        isMatches(userLoginRequest, userDetails);
        return new TokenResponse(jwtUtil.generateAccessToken(userDetails));

    }

    public UserRoleAssignResponse assignAdminRoleById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(NOT_FOUND_USER));
        user.assignAdminRole();
        return UserRoleAssignResponse.from(user);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserCustomException(INVALID_CREDENTIALS));
        return new CustomUserDetails(user);
    }

    private User createUser(UserSignupRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .role(USER)
                .build();
    }

    private void isMatches(UserLoginRequest userLoginRequest, UserDetails userDetails) {
        if (!passwordEncoder.matches(userLoginRequest.password(), userDetails.getPassword())) {
            throw new UserCustomException(INVALID_CREDENTIALS);
        }
    }
}
