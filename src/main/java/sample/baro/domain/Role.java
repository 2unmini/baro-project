package sample.baro.domain;

public enum Role {
    USER("USER"), ADMIN("ADMIN");
    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role of(String name) {
        for (Role role : Role.values()) {
            if (role.name.equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 역할");
    }
}
