package kh.com.kshrd.authentication.model.entity;

import kh.com.kshrd.authentication.model.enums.Gender;
import kh.com.kshrd.authentication.model.enums.GradedSchoolType;
import kh.com.kshrd.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private Gender gender;
    private String phone;
    private LocalDate dob;
    private String schoolName;
    private GradedSchoolType gradedSchoolType;
    private String parentPhone;

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
                .gender(Objects.equals(userRepresentation.getAttributes().get("gender").getFirst(), "N/A") ? Gender.OTHER : Gender.fromValue(userRepresentation.getAttributes().get("gender").getFirst()))
                .phone(userRepresentation.getAttributes().get("phone").getFirst())
                .dob(Objects.equals(userRepresentation.getAttributes().get("dob").getFirst(), "N/A") ? LocalDate.now() : LocalDate.parse(userRepresentation.getAttributes().get("dob").getFirst()))
                .schoolName(userRepresentation.getAttributes().get("schoolName").getFirst())
                .gradedSchoolType(Objects.equals(userRepresentation.getAttributes().get("gradedSchoolType").getFirst(), "N/A") ? GradedSchoolType.OTHER : GradedSchoolType.fromValue(userRepresentation.getAttributes().get("gradedSchoolType").getFirst()))
                .parentPhone(userRepresentation.getAttributes().get("parentPhone").getFirst())
                .build();
    }
}
