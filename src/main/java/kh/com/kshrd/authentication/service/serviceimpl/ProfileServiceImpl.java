package kh.com.kshrd.authentication.service.serviceimpl;

import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.model.enums.Role;
import kh.com.kshrd.authentication.service.AuthenticationService;
import kh.com.kshrd.authentication.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final Keycloak keycloak;
    private final AuthenticationService authenticationService;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public User profileInfo() {
        UsersResource users = keycloak.realm(realm).users();
        String userId = authenticationService.getJwt().getSubject();

        UserResource userRes = users.get(userId);
        UserRepresentation userRep = userRes.toRepresentation();

        List<RoleRepresentation> directRealmRoles = userRes.roles()
                .realmLevel()
                .listAll();

        String roleName = directRealmRoles.isEmpty()
                ? null
                : directRealmRoles.getLast().getName();

        return User.toResponse(roleName, userRep);
    }

    @Override
    public User updateProfileInfo(ProfileRequest request) {
        UsersResource users = keycloak.realm(realm).users();
        String userId = authenticationService.getJwt().getSubject();

        UserResource userRes = users.get(userId);
        UserRepresentation userRep = userRes.toRepresentation();

        userRep.setFirstName(request.getFirstName());
        userRep.setLastName(request.getLastName());
        userRep.singleAttribute("image", request.getProfileImage());

        userRes.update(userRep);

        userRep = userRes.toRepresentation();

        List<RoleRepresentation> directRealmRoles = userRes.roles()
                .realmLevel()
                .listAll();

        String roleName = directRealmRoles.isEmpty()
                ? null
                : directRealmRoles.getLast().getName();

        return User.toResponse(roleName, userRep);
    }

}
