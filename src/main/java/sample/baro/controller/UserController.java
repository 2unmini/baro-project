package sample.baro.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.baro.apidocs.UserControllerApi;
import sample.baro.dto.request.UserSignupRequest;
import sample.baro.dto.response.UserSignupResponse;
import sample.baro.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        UserSignupResponse userSignupResponse = userService.signup(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponse);

    }

}
