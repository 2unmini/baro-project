package sample.baro.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@Schema(defaultValue = "ADMIN") @NotBlank String username,
                               @Schema(defaultValue = "ADMIN") @NotBlank String password) {
}
