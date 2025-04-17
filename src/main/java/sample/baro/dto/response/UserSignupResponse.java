package sample.baro.dto.response;

import sample.baro.domain.User;

import java.util.List;

public record UserSignupResponse(String username, String nickname, List<RoleResponse> roles) {
    public record RoleResponse(String role) {

    }

    public static UserSignupResponse from(User user) {
        return new UserSignupResponse(user.getUsername(), user.getNickname(), List.of(new RoleResponse(user.getRole().name())));
    }
}

