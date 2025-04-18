package sample.baro.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import sample.baro.auth.jwt.TokenResponse;
import sample.baro.dto.request.UserLoginRequest;
import sample.baro.dto.response.ExceptionResponse;
import sample.baro.dto.response.UserRoleAssignResponse;

@Tag(name = "회원관리", description = "회원에 대한 [요구사항에 따른 수정]")
public interface AuthControllerApi {

    @Operation(summary = "사용자 로그인 API ", description = "회원가입한 사용자는 로그인을 할 수있습니다.,ADMIN 로그인시 username:ADMIN ,password:ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호가 올바르지 않습니다",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest);


    @Operation(summary = "관리자 역할 부여 API ", description = "관리자는 사용자의 역할을 관리자로 부여할 수있습니다.")
    @Parameter(name = "id", description = "유저 ID", example = "2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 부여에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = UserRoleAssignResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효한 사용자가 아닙니다.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.")

    })
    ResponseEntity<?> assignAdminRole(@PathVariable Long userId);
}
