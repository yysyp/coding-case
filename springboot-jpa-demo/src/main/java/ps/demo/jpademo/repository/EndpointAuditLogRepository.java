package ps.demo.jpademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.jpademo.entity.EndpointAuditLog;

public interface EndpointAuditLogRepository extends JpaRepository<EndpointAuditLog, Long> {
}