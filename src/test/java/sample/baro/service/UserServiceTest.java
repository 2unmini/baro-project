package sample.baro.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("사용자는 회원가입 할수 있고 기본적으로 USER 권한이다.")
    void SuccessSignup() {

        //given
        UserSignupRequest userSignupRequest = new UserSignupRequest("JIN HO", "12341234", "Mentos");
        given(userRepository.existByUsername("JIN HO")).willReturn(false);

        User user = User.builder().username("JIN HO").password("12341234").nickname("Mentos").build();

        given(userRepository.save(any())).willReturn(user);

        //when
        UserSignupResponse response = userService.signup(userSignupRequest);

        //then
        assertThat(response.username()).isEqualTo("JIN HO");
        assertThat(response.nickname()).isEqualTo("Mentos");
        assertThat(response.roles().get(0).role()).isEqualTo("USER");
    }

    @Test
    @DisplayName("동일한 username 으로 가입할 수 없다.")
    void failSignUp() {

        //given
        UserSignupRequest userSignupRequest = new UserSignupRequest("JIN HO", "12341234", "Mentos");
        given(userRepository.existByUsername("JIN HO")).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> userService.signup(userSignupRequest)).isInstanceOf(UserCustomException.class).hasMessage("이미 가입된 사용자 입니다.");
    }

    @Test
    @DisplayName("사용자가 로그인을 성공하면 토큰을 반환한다.")
    void SuccessLogin() {

        //given
        UserLoginRequest userLoginRequest = new UserLoginRequest("JIN HO", "12341234");

        User user = User.builder().username("JIN HO").password("12341234").nickname("Mentos").build();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        given(userRepository.findByUsername("JIN HO")).willReturn(customUserDetails);
        given(passwordEncoder.matches("12341234", "12341234")).willReturn(true);
        given(jwtUtil.generateAccessToken(customUserDetails)).willReturn("accessToken");

        //when
        TokenResponse tokenResponse = userService.login(userLoginRequest);

        //then
        assertThat(tokenResponse.accessToken()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("잘못된 정보를 입력하면 로그인을 할수 없다")
    void FailLogin() {

        //given
        UserLoginRequest userLoginRequest = new UserLoginRequest("JIN HO", "wrongPassword");

        User user = User.builder().username("JIN HO").password("12341234").nickname("Mentos").build();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        given(userRepository.findByUsername("JIN HO")).willReturn(customUserDetails);
        given(passwordEncoder.matches("wrongPassword", "12341234")).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> userService.login(userLoginRequest)).isInstanceOf(UserCustomException.class).hasMessage("아이디 또는 비밀번호가 올바르지 않습니다");
    }

    @Test
    @DisplayName("ADMIN 권한을 부여 할 수 있다.")
    void successAssignRole() {

        //given
        User user = User.builder().id(2L).username("JIN HO").password("12341234").nickname("Mentos").build();
        given(userRepository.findById(2L)).willReturn(Optional.of(user));

        //when
        UserRoleAssignResponse userRoleAssignResponse = userService.assignAdminRoleById(2L);
        //then
        assertThat(userRoleAssignResponse.roles().get(0).role()).isEqualTo("ADMIN");
    }
}
