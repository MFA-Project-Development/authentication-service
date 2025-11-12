package kh.com.kshrd.authentication.service;

import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.model.enums.BaseRole;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserInfoById(UUID userId);

    List<User> getAllUserByUserIds(UserIdsRequest request);

    List<?> getAllUsersByBaseRole(BaseRole baseRole);
}
