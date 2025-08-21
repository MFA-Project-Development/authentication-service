package kh.com.kshrd.authentication.model.entity;

import kh.com.kshrd.authentication.model.dto.response.SessionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("not-before-policy")
    private long notBeforePolicy;

    @JsonProperty("session_state")
    private String sessionState;

    @JsonProperty("scope")
    private String scope;

    public SessionResponse toResponse(){
        return SessionResponse.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .idToken(this.idToken)
                .tokenType(this.tokenType)
                .expiresIn(this.expiresIn)
                .refreshExpiresIn(this.refreshExpiresIn)
                .notBeforePolicy(this.notBeforePolicy)
                .sessionState(this.sessionState)
                .scope(this.scope)
                .build();
    }
}
