// AuditDailyReport.java
package ps.demo.zglj.scheduledemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "audit_daily_reports")
public class AuditDailyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_date", nullable = false, unique = true)
    private LocalDate reportDate;

    @Column(name = "total_operations", nullable = false)
    private Integer totalOperations;

    @Column(name = "successful_operations", nullable = false)
    private Integer successfulOperations;

    @Column(name = "failed_operations", nullable = false)
    private Integer failedOperations;

    @Column(name = "top_users_json", nullable = false, columnDefinition = "TEXT")
    private String topUsersJson;

    @Column(name = "operations_by_type_json", nullable = false, columnDefinition = "TEXT")
    private String operationsByTypeJson;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false, length = 20)
    private String status = "GENERATED";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy = "SYSTEM";

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy = "SYSTEM";
}