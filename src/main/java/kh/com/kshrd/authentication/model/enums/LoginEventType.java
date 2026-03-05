package kh.com.kshrd.authentication.model.enums;

import lombok.Getter;

@Getter
public enum LoginEventType {
    LOGIN_SUCCESS("login success"),
    LOGIN_FAILURE("login failure"),
    LOGOUT_SUCCESS("logout success"),
    LOGOUT_FAILURE("logout failure");

    private final String eventType;

    LoginEventType(String eventType) {
        this.eventType = eventType;
    }
}
