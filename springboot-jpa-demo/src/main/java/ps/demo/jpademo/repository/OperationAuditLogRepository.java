package ps.demo.jpademo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.jpademo.entity.OperationAuditLog;

public interface OperationAuditLogRepository extends JpaRepository<OperationAuditLog, Long> {
}