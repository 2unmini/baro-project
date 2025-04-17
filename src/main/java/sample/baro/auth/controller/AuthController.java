package sample.baro.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.baro.auth.jwt.TokenResponse;
import sample.baro.dto.request.UserLoginRequest;
import sample.baro.dto.response.UserRoleAssignResponse;
import sample.baro.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        TokenResponse accessToken = userService.login(userLoginRequest);
        return ResponseEntity.ok().body(accessToken);
    }

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<?> assignAdminRole(@PathVariable Long userId) {
        UserRoleAssignResponse userRoleAssignResponse = userService.assignAdminRoleById(userId);
        return ResponseEntity.ok().body(userRoleAssignResponse);
    }

}
