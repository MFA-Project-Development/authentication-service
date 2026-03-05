package kh.com.kshrd.authentication.model.entity;

import jakarta.persistence.*;
import kh.com.kshrd.authentication.model.enums.LoginEventType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "login_logs")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID loginLogId;

    private String email;

    @Enumerated(EnumType.STRING)
    private LoginEventType loginEventType;

    private Boolean success;

    private String reason;

    private String ipAddress;

    private String userAgent;

    private Instant loginTime;

    private Instant logoutTime;

    private String timezone;
}
