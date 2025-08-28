package kh.com.kshrd.authentication.service;

import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.entity.User;

import java.util.UUID;

public interface ProfileService {
    User profileInfo();

    User updateProfileInfo(ProfileRequest request);

    void deleteProfileInfo();

    User profileInfoByUserId(UUID userId);
}
