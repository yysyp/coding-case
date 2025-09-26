package ps.demo.jpademo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ps.demo.jpademo.entity.listener.AutoFill;
import ps.demo.jpademo.entity.listener.AutoFillListener;
import java.time.Instant;

@Getter
@Setter
@ToString
@Entity
@Table(name = "operation_audit_log")
public class OperationAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "action")
    private String action;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "parameters")
    private String parameters;

    @Column(name = "result")
    private String result;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (createdBy == null) {
            createdBy = "SYSTEM";
        }
    }

}