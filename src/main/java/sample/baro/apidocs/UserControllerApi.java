package sample.baro.apidocs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.ExceptionResponse;
import sample.baro.dto.response.UserSignupResponse;

@Tag(name = "회원관리", description = "회원 가입 API")
public interface UserControllerApi {


    @Operation(summary = "사용자 회원 가입 API ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공하였습니다."),
            @ApiResponse(responseCode = "409", description = "이미 가입된 사용자 입니다.",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<UserSignupResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest);
}
