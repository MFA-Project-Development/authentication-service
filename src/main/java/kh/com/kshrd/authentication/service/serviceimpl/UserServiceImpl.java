package kh.com.kshrd.authentication.service.serviceimpl;

import kh.com.kshrd.authentication.exception.NotFoundException;
import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public User getUserInfoById(UUID userId) {
        UsersResource users = keycloak.realm(realm).users();

        UserResource userRes = users.get(String.valueOf(userId));
        UserRepresentation userRep;

        try {
            userRep = userRes.toRepresentation();
        } catch (Exception e) {
            throw new NotFoundException(
                    "User not found : " + userId
            );
        }

        String defaultRole = "default-roles-" + realm;

        List<RoleRepresentation> realmRoles = userRes.roles().realmLevel().listAll();

        String roleName = realmRoles.stream()
                .map(RoleRepresentation::getName)
                .filter(name -> !name.equals(defaultRole))
                .findFirst()
                .orElse(null);

        return User.toResponse(roleName, userRep);
    }

    @Override
    public List<User> getAllUserByUserIds(UserIdsRequest request) {
        UsersResource users = keycloak.realm(realm).users();

        Set<User> userSet = new HashSet<>();

        for (UUID userId : request.getUserIds()) {
            UserResource userRes = users.get(String.valueOf(userId));
            UserRepresentation userRep;

            try {
                userRep = userRes.toRepresentation();
            } catch (Exception e) {
                throw new NotFoundException(
                        "User not found : " + userId
                );
            }

            String defaultRole = "default-roles-" + realm;

            List<RoleRepresentation> realmRoles = userRes.roles().realmLevel().listAll();

            String roleName = realmRoles.stream()
                    .map(RoleRepresentation::getName)
                    .filter(name -> !name.equals(defaultRole))
                    .findFirst()
                    .orElse(null);

            userSet.add(User.toResponse(roleName, userRep));
        }


        return userSet.stream().toList();
    }


}
