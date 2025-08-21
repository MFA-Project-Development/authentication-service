package kh.com.kshrd.authentication.service.serviceimpl;

import kh.com.kshrd.authentication.exception.NotFoundException;
import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.entity.User;
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
        UserRepresentation userRep;

        try {
            userRep = userRes.toRepresentation();
        } catch (Exception e) {
            throw new NotFoundException(
                    "User not found : " + userId
            );
        }

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
        UserRepresentation userRep;

        try {
            userRep = userRes.toRepresentation();
        } catch (Exception e) {
            throw new NotFoundException(
                    "User not found : " + userId
            );
        }

        userRep.setFirstName(request.getFirstName());
        userRep.setLastName(request.getLastName());
        userRep.singleAttribute("image", request.getProfileImage());

        userRes.update(userRep);

        List<RoleRepresentation> directRealmRoles = userRes.roles()
                .realmLevel()
                .listAll();

        String roleName = directRealmRoles.isEmpty()
                ? null
                : directRealmRoles.getLast().getName();

        return User.toResponse(roleName, userRep);
    }

    @Override
    public void deleteProfileInfo() {
        UsersResource users = keycloak.realm(realm).users();
        String userId = authenticationService.getJwt().getSubject();
        UserResource userRes = users.get(userId);
        try {
            userRes.toRepresentation();
            userRes.remove();
        } catch (Exception e) {
            throw new NotFoundException(
                    "User not found : " + userId
            );
        }
    }


}
