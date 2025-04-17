package sample.baro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.UserSignupResponse;
import sample.baro.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원관리", description = "회원에 대한 [요구사항에 따른 수정]")
public class UserController {
    private final UserService userService;

    //todo 상세정보 더하기
    @Operation(summary = "사용자 회원 가입 API ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "올바른 입력값이 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자 입니다.")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        UserSignupResponse userSignupResponse = userService.signup(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponse);

    }

}
