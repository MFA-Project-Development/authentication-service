package kh.com.kshrd.authentication.model.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("admin"),
    STUDENT("student"),
    TEACHER("teacher");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}
