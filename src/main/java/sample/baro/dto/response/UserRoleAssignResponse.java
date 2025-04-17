package sample.baro.dto.response;

import sample.baro.domain.User;

import java.util.List;

public record UserRoleAssignResponse(String username, String nickname, List<RoleResponse> roles) {
    public record RoleResponse(String role) {
    }

    public static UserRoleAssignResponse from(User user) {
        return new UserRoleAssignResponse(user.getUsername(), user.getNickname(), List.of(new RoleResponse(user.getRole().name())));
    }
}
