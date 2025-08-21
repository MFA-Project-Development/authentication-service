package kh.com.kshrd.authentication.service;

import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.entity.User;

public interface ProfileService {
    User profileInfo();

    User updateProfileInfo(ProfileRequest request);
}
