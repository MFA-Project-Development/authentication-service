package kh.com.kshrd.authentication.model.entity;

import kh.com.kshrd.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String profileImage;

    public static User toResponse(String role, UserRepresentation userRepresentation) {

        String profileImage = null;
        Map<String, List<String>> attrs = userRepresentation.getAttributes();
        if (attrs != null) {
            List<String> images = attrs.get("image");
            if (images != null && !images.isEmpty()) {
                profileImage = images.getFirst();
            }
        }

        return User.builder()
                .userId(UUID.fromString(userRepresentation.getId()))
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .role(Role.valueOf("ROLE_" + role))
                .profileImage(profileImage)
                .build();
    }
}
