package kh.com.kshrd.authentication.service.serviceimpl;

import jakarta.ws.rs.core.Response;
import kh.com.kshrd.authentication.exception.BadRequestException;
import kh.com.kshrd.authentication.exception.ConflictException;
import kh.com.kshrd.authentication.exception.NotFoundException;
import kh.com.kshrd.authentication.exception.UpstreamException;
import kh.com.kshrd.authentication.model.dto.request.*;
import kh.com.kshrd.authentication.model.entity.Session;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.model.enums.Role;
import kh.com.kshrd.authentication.service.AuthenticationService;
import kh.com.kshrd.authentication.service.EmailService;
import kh.com.kshrd.authentication.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final int USERNAME_SUFFIX_MAX = 1000;
    private static final SecureRandom RAND = new SecureRandom();

    private final Keycloak keycloak;
    private final OtpService otpService;
    private final EmailService emailService;
    private final RestClient restClient;


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-endpoint}")
    private String tokenEndpoint;

    @Value("${keycloak.end-session-endpoint}")
    private String endSessionEndpoint;

    @Override
    public User registration(RegistrationRequest request) {
        final UsersResource users = keycloak.realm(realm).users();

        if (emailExists(users, request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        final UserRepresentation userRep = buildUserRepresentation(request);

        try (Response response = users.create(userRep)) {
            final int code = response.getStatus();

            if (code == Response.Status.CREATED.getStatusCode()) {
                final String userId = CreatedResponseUtil.getCreatedId(response);

                users.get(userId).resetPassword(buildPasswordCredential(request.getPassword()));

                assignRealmRole(userId, request.getRole());

                final String otp = otpService.generateOtp(request.getEmail());
                emailService.sendMail(otp, request.getEmail());

                return User.builder()
                        .userId(UUID.fromString(userId))
                        .username(userRep.getUsername())
                        .email(userRep.getEmail())
                        .firstName(userRep.getFirstName())
                        .lastName(userRep.getLastName())
                        .role(mapRoleSafely(request.getRole()))
                        .build();
            }

            if (code == Response.Status.CONFLICT.getStatusCode()) {
                throw new ConflictException("User already exists");
            }

            final String body = safeRead(response);
            throw new UpstreamException("Keycloak error " + code + (body.isBlank() ? "" : ": " + body));
        }
    }

    @Override
    public void registrationVerification(RegistrationVerificationRequest request) {
        final UsersResource users = keycloak.realm(realm).users();
        final String email = request.getEmail();
        final String otp = request.getOtp();

        if (!emailExists(users, request.getEmail())) {
            throw new NotFoundException("The email address provided is not registered. Please check and try again.");
        }

        boolean isValidateOtp = otpService.validateOtp(email, otp);
        if (!isValidateOtp) {
            throw new BadRequestException("The OTP entered is invalid or has expired. Please request a new OTP and try again.");
        }

        UserRepresentation userRepresentation = users.searchByEmail(email, true).getFirst();

        if (Boolean.TRUE.equals(userRepresentation.isEnabled())
            && Boolean.TRUE.equals(userRepresentation.isEmailVerified())) {
            return;
        }

        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        users.get(userRepresentation.getId()).update(userRepresentation);
    }

    @Override
    public void passwordResetConfirmation(PasswordResetConfirmationRequest request) {
        final UsersResource users = keycloak.realm(realm).users();
        final String email = request.getEmail();
        final String otp = request.getOtp();
        final String newPassword = request.getNewPassword();

        if (!emailExists(users, request.getEmail())) {
            throw new NotFoundException("The email address provided is not registered. Please check and try again.");
        }

        boolean isValidateOtp = otpService.validateOtp(email, otp);
        if (!isValidateOtp) {
            throw new BadRequestException("The OTP entered is invalid or has expired. Please request a new OTP and try again.");
        }

        UserRepresentation userRepresentation = users.searchByEmail(email, true).getFirst();
        users.get(userRepresentation.getId()).resetPassword(buildPasswordCredential(newPassword));

    }

    @Override
    public void resendOtp(EmailRequest request) {
        final UsersResource users = keycloak.realm(realm).users();
        final String email = request.getEmail();

        if (!emailExists(users, request.getEmail())) {
            throw new NotFoundException("The email address provided is not registered. Please check and try again.");
        }

        final String otp = otpService.generateOtp(email);
        emailService.sendMail(otp, request.getEmail());
    }

    @Override
    public void passwordResetVerification(PasswordResetVerificationRequest request) {
        final UsersResource users = keycloak.realm(realm).users();
        final String email = request.getEmail();
        final String otp = request.getOtp();

        if (!emailExists(users, request.getEmail())) {
            throw new NotFoundException("The email address provided is not registered. Please check and try again.");
        }

        boolean isValidateOtp = otpService.verifyOtp(email, otp);
        if (!isValidateOtp) {
            throw new BadRequestException("The OTP entered is invalid or has expired. Please request a new OTP and try again.");
        }
    }

    @Override
    public Session sessions(SessionRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", clientId);
        if (!clientSecret.isBlank()) form.add("client_secret", clientSecret);
        form.add("username", request.getEmail());
        form.add("password", request.getPassword());
        form.add("scope", "openid email profile");
        return restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Session.class);
    }

    @Override
    public Session sessionRefresh(RefreshRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        if (!clientSecret.isBlank()) form.add("client_secret", clientSecret);
        form.add("refresh_token", request.getRefreshToken());
        return restClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Session.class);
    }

    @Override
    public void sessionLogout(RefreshRequest request) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        if (!clientSecret.isBlank()) form.add("client_secret", clientSecret);
        form.add("refresh_token", request.getRefreshToken());
        restClient.post()
                .uri(endSessionEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .toBodilessEntity();
    }

    private boolean emailExists(UsersResource users, String email) {
        final List<UserRepresentation> hits = users.searchByEmail(email, true);
        return hits != null && !hits.isEmpty();
    }

    private UserRepresentation buildUserRepresentation(RegistrationRequest request) {
        final UserRepresentation u = new UserRepresentation();
        u.setUsername(generateUsername(request.getFirstName(), request.getLastName()));
        u.setEmail(request.getEmail());
        u.setFirstName(request.getFirstName());
        u.setLastName(request.getLastName());

        u.setEnabled(false);
        u.setEmailVerified(false);

        return u;
    }

    private String generateUsername(String first, String last) {
        final String base = (nullToEmpty(first) + "." + nullToEmpty(last))
                .toLowerCase()
                .replaceAll("[^a-z0-9.]", "")
                .replaceAll("\\.+", ".")
                .replaceAll("^\\.|\\.$", "");

        final int suffix = RAND.nextInt(USERNAME_SUFFIX_MAX);
        return base + String.format("%03d", suffix);
    }

    private CredentialRepresentation buildPasswordCredential(String password) {
        final CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setTemporary(false);
        cred.setValue(password);
        return cred;
    }

    private void assignRealmRole(String userId, Role requestedRole) {
        final RolesResource roles = keycloak.realm(realm).roles();

        final RoleRepresentation roleRep = roles.get(requestedRole.name()).toRepresentation();
        if (roleRep == null) {
            throw new NotFoundException("Configured role not found in Keycloak client: " + requestedRole.name());
        }

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRep));
    }

    private Role mapRoleSafely(Role requested) {
        return requested;
    }

    private static String safeRead(Response response) {
        try {
            return Optional.ofNullable(response.readEntity(String.class)).orElse("");
        } catch (Exception ignored) {
            return "";
        }
    }

    private static String nullToEmpty(String s) {
        return Objects.toString(s, "");
    }

}
