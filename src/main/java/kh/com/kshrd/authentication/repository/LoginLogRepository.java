package kh.com.kshrd.authentication.repository;

import kh.com.kshrd.authentication.model.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, UUID> {
    LoginLog findTopByEmailOrderByLoginTimeDesc(String email);
}
