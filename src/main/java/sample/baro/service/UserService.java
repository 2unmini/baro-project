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
import sample.baro.repsitory.UserRepository;

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
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .role(USER)
                .build();
        User savedUser = userRepository.save(user);
        return UserSignupResponse.from(savedUser);
    }

    public TokenResponse login(@Valid UserLoginRequest userLoginRequest) {
        CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(userLoginRequest.username());
        isMatches(userLoginRequest, userDetails);
        return new TokenResponse(jwtUtil.generateAccessToken(userDetails));

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    private void isMatches(UserLoginRequest userLoginRequest, UserDetails userDetails) {
        if (!passwordEncoder.matches(userLoginRequest.password(), userDetails.getPassword())) {
            throw new UserCustomException(INVALID_CREDENTIALS);
        }
    }

    public UserRoleAssignResponse assignAdminRoleById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserCustomException(NOT_FOUND_USER));
        user.assignAdminRole();
        return UserRoleAssignResponse.from(user);
    }
}
