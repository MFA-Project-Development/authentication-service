package kh.com.kshrd.authentication.service.serviceimpl;

import jakarta.servlet.http.HttpServletRequest;
import kh.com.kshrd.authentication.model.entity.LoginLog;
import kh.com.kshrd.authentication.model.enums.LoginEventType;
import kh.com.kshrd.authentication.repository.LoginLogRepository;
import kh.com.kshrd.authentication.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginLogServiceImpl implements LoginLogService {

    private final LoginLogRepository loginLogRepository;


    @Override
    @Transactional
    public void logSuccess(String email, String timezone, HttpServletRequest request) {

        ZoneId zoneId = ZoneId.of(timezone);
        Instant instant = Instant.now();

        LoginLog log = LoginLog.builder()
                .email(email)
                .loginEventType(LoginEventType.LOGIN_SUCCESS)
                .success(true)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .loginTime(instant.atZone(zoneId).toInstant())
                .timezone(timezone)
                .build();

        loginLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logFailure(String email, String reason, String timezone, HttpServletRequest request) {
        ZoneId zoneId = ZoneId.of(timezone);
        Instant instant = Instant.now();

        LoginLog log = LoginLog.builder()
                .email(email)
                .loginEventType(LoginEventType.LOGIN_FAILURE)
                .success(false)
                .reason(reason)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .loginTime(instant.atZone(zoneId).toInstant())
                .timezone(timezone)
                .build();

        loginLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logoutSuccess(String email, String timezone, HttpServletRequest request) {
        ZoneId zoneId = ZoneId.of(timezone);
        Instant instant = Instant.now();

        LoginLog log = LoginLog.builder()
                .email(email)
                .loginEventType(LoginEventType.LOGOUT_SUCCESS)
                .success(true)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .logoutTime(instant.atZone(zoneId).toInstant())
                .timezone(timezone)
                .build();

        loginLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logoutFailure(String email, String reason, String timezone, HttpServletRequest request) {
        ZoneId zoneId = ZoneId.of(timezone);
        Instant instant = Instant.now();

        LoginLog log = LoginLog.builder()
                .email(email)
                .loginEventType(LoginEventType.LOGOUT_FAILURE)
                .success(false)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .logoutTime(instant.atZone(zoneId).toInstant())
                .timezone(timezone)
                .build();

        loginLogRepository.save(log);
    }

    private String getClientIp(HttpServletRequest request) {

        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
