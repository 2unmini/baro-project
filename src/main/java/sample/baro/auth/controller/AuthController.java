package sample.baro.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.baro.auth.jwt.TokenResponse;
import sample.baro.dto.request.UserLoginRequest;
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

}
