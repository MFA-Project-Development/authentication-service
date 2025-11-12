package kh.com.kshrd.authentication.model.enums;

import lombok.Getter;

@Getter
public enum BaseRole {

    ROLE_STUDENT("STUDENT"),
    ROLE_INSTRUCTOR("INSTRUCTOR");

    private final String roleName;

    BaseRole(String roleName) {
        this.roleName = roleName;
    }

}
