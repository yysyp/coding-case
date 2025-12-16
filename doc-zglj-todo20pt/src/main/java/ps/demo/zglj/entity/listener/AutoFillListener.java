package ps.demo.zglj.entity.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

public class AutoFillListener {

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof AutoFill) {
            AutoFill autoFillEntity = (AutoFill) entity;
            Instant now = Instant.now();
            if (autoFillEntity.getCreatedAt() == null) {
                autoFillEntity.setCreatedAt(now);
            }
            if (autoFillEntity.getCreatedBy() == null) {
                autoFillEntity.setCreatedBy("system"); // Replace with actual user from security context
            }
            autoFillEntity.setUpdatedAt(now);
            autoFillEntity.setUpdatedBy("system"); // Replace with actual user from security context
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof AutoFill) {
            AutoFill autoFillEntity = (AutoFill) entity;
            autoFillEntity.setUpdatedAt(Instant.now());
            autoFillEntity.setUpdatedBy("system"); // Replace with actual user from security context
        }
    }
}