package kh.com.kshrd.authentication.model.entity;

import kh.com.kshrd.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
