package kh.com.kshrd.authentication.service;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginLogService {

    void logSuccess(String email, String timezone, HttpServletRequest request);

    void logFailure(String email, String reason, String timezone, HttpServletRequest request);

    void logoutSuccess(String email, String timezone, HttpServletRequest request);

    void logoutFailure(String email, String reason, String timezone, HttpServletRequest request);

}
