package kh.com.kshrd.authentication.service;

import jakarta.servlet.http.HttpServletRequest;
import kh.com.kshrd.authentication.model.dto.request.*;
import kh.com.kshrd.authentication.model.dto.response.SessionResponse;
import kh.com.kshrd.authentication.model.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;


public interface AuthenticationService {

    User registration(RegistrationRequest request);

    void registrationVerification(RegistrationVerificationRequest request);

    void passwordResetConfirmation(PasswordResetConfirmationRequest request);

    void resendOtp(EmailRequest request);

    void passwordResetVerification(PasswordResetVerificationRequest request);

    SessionResponse sessions(SessionRequest request, HttpServletRequest httpRequest);

    SessionResponse sessionRefresh(RefreshRequest request);

    void sessionLogout(RefreshRequest request, HttpServletRequest httpRequest);

    Jwt getJwt();
}
