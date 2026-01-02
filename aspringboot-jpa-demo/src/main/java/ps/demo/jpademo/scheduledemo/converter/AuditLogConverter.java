// AuditLogConverter.java
package ps.demo.jpademo.scheduledemo.converter;

import ps.demo.jpademo.scheduledemo.dto.AuditLogRequest;
import ps.demo.jpademo.scheduledemo.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditLogConverter {

    public AuditLog toEntity(AuditLogRequest request) {
        if (request == null) {
            return null;
        }

        AuditLog entity = new AuditLog();
        entity.setUserId(request.getUserId());
        entity.setAction(request.getAction());
        entity.setResourceType(request.getResourceType());
        entity.setResourceId(request.getResourceId());
        entity.setStatus(request.getStatus());
        entity.setIpAddress(request.getIpAddress());
        entity.setTimestamp(request.getTimestamp());
        entity.setDetails(request.getDetails());
        
        return entity;
    }
}