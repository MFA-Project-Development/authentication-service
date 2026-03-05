package kh.com.kshrd.authentication.repository;

import kh.com.kshrd.authentication.model.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
    Optional<ActivityLog> findByActor(String actor);
}
