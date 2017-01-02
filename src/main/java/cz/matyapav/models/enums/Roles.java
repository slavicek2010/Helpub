package cz.matyapav.models.enums;

/**
 * Created by Pavel on 25.12.2016.
 */
public enum Roles {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String roleName;

    Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
