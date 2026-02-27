package kh.com.kshrd.authentication.service.serviceimpl;

import kh.com.kshrd.authentication.exception.NotFoundException;
import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.dto.response.InstructorResponse;
import kh.com.kshrd.authentication.model.dto.response.StudentResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.model.enums.BaseRole;
import kh.com.kshrd.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public User getUserInfoById(UUID userId) {
        UsersResource users = keycloak.realm(realm).users();
        String defaultRole = "default-roles-" + realm;

        try {
            UserResource userRes = users.get(userId.toString());
            UserRepresentation userRep = userRes.toRepresentation();

            List<RoleRepresentation> realmRoles = userRes.roles().realmLevel().listAll();

            String roleName = realmRoles.stream()
                    .map(RoleRepresentation::getName)
                    .filter(name -> !name.equals(defaultRole))
                    .findFirst()
                    .orElse(null);

            return User.toResponse(roleName, userRep);

        } catch (Exception e) {
            throw new NotFoundException("User not found: " + userId);
        }
    }

    @Override
    public List<User> getAllUserByUserIds(UserIdsRequest request) {
        UsersResource users = keycloak.realm(realm).users();
        String defaultRole = "default-roles-" + realm;

        List<User> userList = new ArrayList<>();

        for (UUID userId : request.getUserIds()) {
            try {
                UserResource userRes = users.get(userId.toString());
                UserRepresentation userRep = userRes.toRepresentation();

                List<RoleRepresentation> realmRoles = userRes.roles().realmLevel().listAll();

                String roleName = realmRoles.stream()
                        .map(RoleRepresentation::getName)
                        .filter(name -> !name.equals(defaultRole))
                        .findFirst()
                        .orElse(null);

                userList.add(User.toResponse(roleName, userRep));

            } catch (Exception e) {
                throw new NotFoundException("User not found: " + userId);
            }
        }

        return userList;
    }

    @Override
    public List<?> getAllUsersByBaseRole(BaseRole baseRole) {
        RealmResource realmResource = keycloak.realm(realm);
        RolesResource rolesResource = realmResource.roles();
        RoleResource roleResource = rolesResource.get(baseRole.getRoleName());

        if (roleResource == null) {
            throw new NotFoundException("Role not found: " + baseRole.getRoleName());
        }

        List<UserRepresentation> userRepresentations = roleResource.getUserMembers();

        if (userRepresentations.isEmpty()) {
            return List.of();
        }

        return switch (baseRole) {
            case ROLE_INSTRUCTOR -> userRepresentations.stream()
                    .map(userRep -> InstructorResponse.builder()
                            .instructorId(UUID.fromString(userRep.getId()))
                            .instructorEmail(userRep.getEmail())
                            .instructorName(buildFullName(userRep))
                            .build())
                    .toList();

            case ROLE_STUDENT -> userRepresentations.stream()
                    .map(userRep -> StudentResponse.builder()
                            .studentId(UUID.fromString(userRep.getId()))
                            .studentEmail(userRep.getEmail())
                            .studentName(buildFullName(userRep))
                            .build())
                    .toList();
        };
    }

    private String buildFullName(UserRepresentation userRep) {
        String firstName = userRep.getFirstName() != null ? userRep.getFirstName() : "";
        String lastName = userRep.getLastName() != null ? userRep.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }

}
