package sample.baro.auth.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import sample.baro.UserTestBuilder;
import sample.baro.auth.CustomUserDetails;
import sample.baro.domain.User;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret-key=67CU66Gc7J247YS0IOyngeustOqzvOygnCDthYzsiqTtirjsmqkg7YKk7IS47YyF7J6F64uI64ukIQ==",
        "jwt.expiration-ms=5000"
})
class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("JWT 생성 시 유저정보와 권한이 포함된다")
    void successGenerateToken() {

        //given
        User user = UserTestBuilder.defaultUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtUtil.generateAccessToken(userDetails);

        //when
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //then
        assertThat(username).isEqualTo("JIN HO");
        assertThat(role).isEqualTo("USER");
    }

    @Test
    @DisplayName("토큰이 만료되면 False 를 반환한다.")
    void failExpiredTokenValidation() throws InterruptedException {
        //given
        User user = UserTestBuilder.defaultUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtUtil.generateAccessToken(userDetails);

        TimeUnit.MILLISECONDS.sleep(5000);

        //when
        boolean isValid = jwtUtil.validateToken(token);
        //then
        assertThat(isValid).isFalse();
    }


    @Test
    @DisplayName("유효한 토큰은 True 를 반환한다.")
    void successValidateToken() {
        //given
        User user = UserTestBuilder.defaultUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtUtil.generateAccessToken(userDetails);

        //when
        boolean isValid = jwtUtil.validateToken(token);
        //then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("형식이 잘못된 토큰은 False 를 반환한다.")
    void failTokenValidation() {
        //given
        String fakeToken = "fakeToken";
        //when
        boolean isValid = jwtUtil.validateToken(fakeToken);
        //then
        assertThat(isValid).isFalse();
    }


}