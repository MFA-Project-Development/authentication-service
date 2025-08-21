package kh.com.kshrd.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kh.com.kshrd.authentication.model.dto.request.*;
import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import kh.com.kshrd.authentication.model.dto.response.SessionResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kh.com.kshrd.authentication.utils.ResponseUtil.buildResponse;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registrations")
    @Operation(
            summary = "Register a new user account",
            description = "Creates a new user account. Requires user details such as email, password, and profile information. "
                          + "An OTP will be sent to the user’s email for verification.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<User>> registration(@RequestBody @Valid RegistrationRequest request) {
        return buildResponse("User registered successfully", authenticationService.registration(request), HttpStatus.CREATED);
    }

    @PostMapping("/registrations/verification")
    @Operation(
            summary = "Verify user registration",
            description = "Verifies a newly registered user account using the OTP code sent to the registered email.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> registrationVerification(@RequestBody @Valid RegistrationVerificationRequest request) {
        authenticationService.registrationVerification(request);
        return buildResponse("Registration verified successfully", null, HttpStatus.OK);
    }

    @PostMapping("/registrations/resend-otp")
    @Operation(
            summary = "Resend registration OTP",
            description = "Resends a new OTP code to the user’s email in case the previous code expired or was not received.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> registrationResendOtp(@RequestBody @Valid EmailRequest request) {
        authenticationService.resendOtp(request);
        return buildResponse("OTP resent successfully", null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/password-resets")
    @Operation(
            summary = "Request password reset",
            description = "Initiates the password reset process by sending an OTP to the user’s registered email.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> passwordReset(@RequestBody @Valid EmailRequest request) {
        authenticationService.resendOtp(request);
        return buildResponse("Password reset OTP sent successfully", null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/password-resets/verification")
    @Operation(
            summary = "Verify password reset OTP",
            description = "Verifies the OTP sent to the user’s email during the password reset process.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> passwordResetVerification(@RequestBody @Valid PasswordResetVerificationRequest request) {
        authenticationService.passwordResetVerification(request);
        return buildResponse("Password reset OTP verified successfully", null, HttpStatus.OK);
    }

    @PostMapping("/password-resets/confirmation")
    @Operation(
            summary = "Confirm new password",
            description = "Completes the password reset process by setting a new password after successful OTP verification.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> passwordResetConfirmation(@RequestBody @Valid PasswordResetConfirmationRequest request) {
        authenticationService.passwordResetConfirmation(request);
        return buildResponse("Password reset confirmed successfully", null, HttpStatus.OK);
    }

    @PostMapping("/password-resets/resend-otp")
    @Operation(
            summary = "Resend password reset OTP",
            description = "Resends a new OTP code for the password reset process if the previous one expired or was not received.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> passwordResetResendOtp(@RequestBody @Valid EmailRequest request) {
        authenticationService.resendOtp(request);
        return buildResponse("Password reset OTP resent successfully", null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/sessions")
    @Operation(
            summary = "Login and create session",
            description = "Authenticates a user with email and password. Returns access and refresh tokens for managing the session.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<SessionResponse>> sessions(@RequestBody @Valid SessionRequest request) {
        return buildResponse("User logged in successfully", authenticationService.sessions(request), HttpStatus.OK);
    }

    @PostMapping("/sessions/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token, keeping the user session active.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<SessionResponse>> sessionRefresh(@RequestBody @Valid RefreshRequest request) {
        return buildResponse("Access token refreshed successfully", authenticationService.sessionRefresh(request), HttpStatus.OK);
    }

    @PostMapping("/sessions/logout")
    @Operation(
            summary = "Logout and revoke session",
            description = "Logs out the user by revoking the refresh token and invalidating the active session.",
            tags = "Authentication"
    )
    public ResponseEntity<APIResponse<Void>> sessionLogout(@RequestBody @Valid RefreshRequest request) {
        authenticationService.sessionLogout(request);
        return buildResponse("User logged out successfully", null, HttpStatus.OK);
    }

}
