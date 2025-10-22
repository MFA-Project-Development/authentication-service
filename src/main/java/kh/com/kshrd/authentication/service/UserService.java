package kh.com.kshrd.authentication.service;

import kh.com.kshrd.authentication.model.dto.request.UserIdsRequest;
import kh.com.kshrd.authentication.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUserInfoById(UUID userId);

    List<User> getAllUserByUserIds(UserIdsRequest request);
}
