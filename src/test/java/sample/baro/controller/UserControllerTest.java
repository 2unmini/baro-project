package sample.baro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.baro.domain.Role;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.UserSignupResponse;
import sample.baro.exception.UserCustomException;
import sample.baro.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sample.baro.exception.ErrorCode.USER_ALREADY_EXISTS;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자는 회원가입 할 수 있다.")
    void signup() throws Exception {

        //given
        UserSignupRequest userSignupRequest = new UserSignupRequest("JIN HO", "12341234", "Mentos");
        UserSignupResponse userSignupResponse = new UserSignupResponse("JIN HO", "Mentos", List.of(new UserSignupResponse.RoleResponse(Role.USER.name())));

        given(userService.signup(any())).willReturn(userSignupResponse);

        //when
        //then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("JIN HO"))
                .andExpect(jsonPath("$.nickname").value("Mentos"))
                .andExpect(jsonPath("$.roles[0].role").value("USER"));
    }

    @Test
    @DisplayName("중복된 Username 이 있다면 가입 할 수 없다.")
    void duplicateSignup() throws Exception {

        //given
        UserSignupRequest userSignupRequest = new UserSignupRequest("JIN HO", "12341234", "Mentos");
        UserSignupResponse userSignupResponse = new UserSignupResponse("JIN HO", "Mentos", List.of(new UserSignupResponse.RoleResponse(Role.USER.name())));

        given(userService.signup(any())).willThrow(new UserCustomException(USER_ALREADY_EXISTS));

        //when
        //then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.error.message").value("이미 가입된 사용자 입니다."));
    }
}