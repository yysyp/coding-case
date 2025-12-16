// AuditLogRequest.java
package ps.demo.zglj.scheduledemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogRequest {

    @NotBlank(message = "User ID is mandatory")
    private String userId;

    @NotBlank(message = "Action is mandatory")
    private String action;

    private String resourceType;
    
    private String resourceId;

    @NotBlank(message = "Status is mandatory")
    private String status;

    private String ipAddress;

    @NotNull(message = "Timestamp is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String details;
}