package sample.baro.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static sample.baro.domain.Role.USER;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Role role;

    @Builder
    public User(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = USER;
    }


}
