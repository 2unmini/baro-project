package sample.baro;

import sample.baro.domain.User;

public class UserTestBuilder {
    private static final String DEFAULT_USERNAME = "JIN HO";
    private static final String DEFAULT_PASSWORD = "12341234";
    private static final String DEFAULT_NICKNAME = "Mentos";

    private String username = DEFAULT_USERNAME;
    private String nickname = DEFAULT_NICKNAME;
    private String password = DEFAULT_PASSWORD;

    private UserTestBuilder() {
    }

    public static UserTestBuilder builder() {
        return new UserTestBuilder();
    }


    public UserTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }

    public static User defaultUser() {
        return builder().build();
    }
}
