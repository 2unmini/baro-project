package sample.baro.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@Schema(defaultValue = "JIN HO") @NotBlank String username,
                               @Schema(defaultValue = "12341234") @NotBlank String password) {
}
