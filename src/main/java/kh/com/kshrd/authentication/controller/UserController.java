package kh.com.kshrd.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.model.enums.BaseRole;
import kh.com.kshrd.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
            summary = "Get user profile",
            description = "Returns a user's profile by ID, including username, email, first/last name, role, and profile image.",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<APIResponse<User>> getUserInfoById(
            @Parameter(description = "User ID", in = ParameterIn.PATH)
            @PathVariable("user-id") UUID userId
    ) {
        return buildResponse(
                "Profile retrieved by userId successfully",
                userService.getUserInfoById(userId),
                HttpStatus.OK
        );
    }

    @PostMapping("/by-user-ids")
    @Operation(
            summary = "List users by IDs",
            description = "Returns all users whose IDs are provided in the request body. Order is not guaranteed.",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    public ResponseEntity<APIResponse<List<User>>> getAllUserByUserIds(
            @RequestBody @Valid UserIdsRequest request
    ) {
        return buildResponse(
                "Users retrieved by IDs",
                userService.getAllUserByUserIds(request),
                HttpStatus.OK
        );
    }

    @GetMapping("/base-role")
    @Operation(
            summary = "List users by base role",
            description = "Returns all users that have the specified base role.",
            tags = {"User"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid role value")
            }
    )
    public ResponseEntity<APIResponse<List<?>>> getAllUsersByBaseRole(
            @Parameter(description = "Base role to filter users", in = ParameterIn.QUERY, example = "ROLE_STUDENT")
            @RequestParam BaseRole baseRole
    ) {
        return buildResponse(
                "Users retrieved by base role",
                userService.getAllUsersByBaseRole(baseRole),
                HttpStatus.OK
        );
    }
}
