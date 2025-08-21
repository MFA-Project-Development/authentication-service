package kh.com.kshrd.authentication.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionResponse {
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private long expiresIn;
    private Long refreshExpiresIn;
    private long notBeforePolicy;
    private String sessionState;
    private String scope;
}
