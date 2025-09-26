package ps.demo.jpademo.entity.listener;

import jakarta.persistence.PrePersist;
import ps.demo.jpademo.entity.EndpointAuditLog;
import java.time.Instant;

public class EndpointAuditLogListener {

    @PrePersist
    public void prePersist(EndpointAuditLog endpointAuditLog) {
        if (endpointAuditLog.getCreatedAt() == null) {
            endpointAuditLog.setCreatedAt(Instant.now());
        }
    }
}