package sample.baro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Role role;

    public User(Long id, String username, String password, String nickname, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = (role != null) ? role : Role.USER;
    }


    public void assignAdminRole() {
        this.role = Role.ADMIN;
    }
}
