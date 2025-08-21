package kh.com.kshrd.authentication.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kh.com.kshrd.authentication.model.dto.request.ProfileRequest;
import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import kh.com.kshrd.authentication.model.entity.User;
import kh.com.kshrd.authentication.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kh.com.kshrd.authentication.utils.ResponseUtil.buildResponse;


@Slf4j
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "mfa")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<APIResponse<User>> profileInfo(){
        return buildResponse("", profileService.profileInfo(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse<User>> updateProfileInfo(@RequestBody ProfileRequest request){
        return buildResponse("", profileService.updateProfileInfo(request), HttpStatus.OK);
    }

}
