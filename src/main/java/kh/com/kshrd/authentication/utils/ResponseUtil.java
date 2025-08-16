package kh.com.kshrd.authentication.utils;

import kh.com.kshrd.authentication.model.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseUtil {

    public static <T> ResponseEntity<APIResponse<T>> buildResponse(String message, T payload, HttpStatus status) {
        APIResponse<T> response = new APIResponse<>(message, payload, status, Instant.now());
        return ResponseEntity.status(status).body(response);
    }

}
