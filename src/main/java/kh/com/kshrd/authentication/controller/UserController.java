package kh.com.kshrd.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kh.com.kshrd.authentication.utils.ResponseUtil.buildResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "mfa")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user-id}")
    @Operation(
            summary = "Get profile information by userId",
            description = "Fetch the currently authenticated user's profile information including username, email, first name, last name, role, and profile image.",
            tags = "User"
    )
    public ResponseEntity<APIResponse<User>> getUserInfoById(@PathVariable("user-id") UUID userId) {
        return buildResponse("Profile retrieved by userId successfully", userService.getUserInfoById(userId), HttpStatus.OK);
    }

    @PostMapping("/by-user-ids")
    @Operation(
            summary = "",
            description = "",
            tags = "User"
    )
    public ResponseEntity<APIResponse<List<User>>> getAllUserByUserIds(@RequestBody UserIdsRequest request) {
        return buildResponse("", userService.getAllUserByUserIds(request), HttpStatus.OK);
    }

}
