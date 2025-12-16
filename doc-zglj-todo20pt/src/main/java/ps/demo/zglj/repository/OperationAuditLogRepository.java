package ps.demo.zglj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.demo.zglj.entity.OperationAuditLog;

public interface OperationAuditLogRepository extends JpaRepository<OperationAuditLog, Long> {
}