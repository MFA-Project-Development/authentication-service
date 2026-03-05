package kh.com.kshrd.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kh.com.kshrd.authentication.model.annotation.AuditAction;
import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kh.com.kshrd.authentication.utils.ResponseUtil.buildResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "mfa")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(
            summary = "Get profile information",
            description = "Fetch the currently authenticated user's profile information including username, email, first name, last name, role, and profile image.",
            tags = "Profile"
    )
    public ResponseEntity<APIResponse<User>> profileInfo() {
        return buildResponse(
                "Profile retrieved successfully",
                profileService.profileInfo(),
                HttpStatus.OK
        );
    }

    @AuditAction("UPDATE_PROFILE")
    @PutMapping
    @Operation(
            summary = "Update profile information",
            description = "Update the currently authenticated user's profile details such as first name, last name, and profile image.",
            tags = "Profile"
    )
    public ResponseEntity<APIResponse<User>> updateProfileInfo(
            @RequestBody @Valid ProfileRequest request
    ) {
        return buildResponse(
                "Profile updated successfully",
                profileService.updateProfileInfo(request),
                HttpStatus.OK
        );
    }

    @AuditAction("DELETE_PROFILE")
    @DeleteMapping
    @Operation(
            summary = "Delete profile information",
            description = "Delete the currently authenticated user's profile. Depending on configuration, this may remove the user account entirely or clear profile details.",
            tags = "Profile"
    )
    public ResponseEntity<APIResponse<Void>> deleteProfileInfo() {
        profileService.deleteProfileInfo();
        return buildResponse(
                "Profile deleted successfully",
                null,
                HttpStatus.OK
        );
    }
}