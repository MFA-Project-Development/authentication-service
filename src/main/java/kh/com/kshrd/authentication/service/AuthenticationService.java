package kh.com.kshrd.authentication.service;

import kh.com.kshrd.authentication.model.dto.request.*;
import kh.com.kshrd.authentication.model.entity.Session;
import kh.com.kshrd.authentication.model.entity.User;

public interface AuthenticationService {

    User registration(RegistrationRequest request);

    void registrationVerification(RegistrationVerificationRequest request);

    void passwordResetConfirmation(PasswordResetConfirmationRequest request);

    void resendOtp(EmailRequest request);

    void passwordResetVerification(PasswordResetVerificationRequest request);

    Session sessions(SessionRequest request);

    Session sessionRefresh(RefreshRequest request);

    void sessionLogout(RefreshRequest request);
}
