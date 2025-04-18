package sample.baro.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.baro.auth.jwt.TokenResponse;
import sample.baro.domain.Role;
import sample.baro.dto.request.UserLoginRequest;
import sample.baro.dto.response.UserRoleAssignResponse;
import sample.baro.exception.UserCustomException;
import sample.baro.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.baro.exception.ErrorCode.ACCESS_DENIED;
import static sample.baro.exception.ErrorCode.INVALID_CREDENTIALS;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String ACCESS_TOKEN = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJKSU4gSE8iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc0NDk5NjM5MiwiZXhwIjoxNzQ0OTk5OTkyfQ.L6qJT6I4WQ-A5zUeMJb0_grknhHARxuX-20TUixzu7qPxeeJOII52oSv2rL2hFCI";

    @Test
    @DisplayName("로그인 성공하면 토큰을 반환한다")
    void login() throws Exception {

        //given
        UserLoginRequest userLoginRequest = new UserLoginRequest("JIN HO", "12341234");
        TokenResponse tokenResponse = new TokenResponse(ACCESS_TOKEN);

        given(userService.login(userLoginRequest)).willReturn(tokenResponse);

        //when
        //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN));
    }

    @Test
    @DisplayName("잘못된 아이디 또는 비밀번호를 입력하면 로그인을 실패한다.")
    void loginFail() throws Exception {
        //given
        UserLoginRequest userLoginRequest = new UserLoginRequest("JIN HO", "틀린 비밀번호");

        given(userService.login(any())).willThrow(new UserCustomException(INVALID_CREDENTIALS));

        //when
        //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
                .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다"));


    }

    private final Long userId = 1L;

    @Test
    @DisplayName("ADMIN 은 USER 에게 ADMIN 권한을 부여 할 수 있다.")
    void assignAdminRole() throws Exception {

        //given
        UserRoleAssignResponse userRoleAssignResponse = new UserRoleAssignResponse("JIN HO", "Mentos", List.of(new UserRoleAssignResponse.RoleResponse(Role.ADMIN.name())));

        given(userService.assignAdminRoleById(any())).willReturn(userRoleAssignResponse);

        //when
        //then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JIN HO"))
                .andExpect(jsonPath("$.nickname").value("Mentos"))
                .andExpect(jsonPath("$.roles[0].role").value("ADMIN"));


    }

    @Test
    @DisplayName("USER 은 다른 USER 에게 ADMIN 권한을 부여 할 수 없다.")
    @WithMockUser(username = "user")
    void assignAdminRoleNotAdmin() throws Exception {

        //given
        given(userService.assignAdminRoleById(any())).willThrow(new UserCustomException(ACCESS_DENIED));

        //when
        //then
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));


    }

}