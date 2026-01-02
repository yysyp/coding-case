// AuditLogRepository.java
package ps.demo.jpademo.scheduledemo.repository;

import ps.demo.jpademo.scheduledemo.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("SELECT NEW map(al.userId as userId, COUNT(al) as count) " +
           "FROM AuditLog al " +
           "WHERE al.timestamp >= :start AND al.timestamp < :end " +
           "GROUP BY al.userId " +
           "ORDER BY count DESC")
    List<Map<String, Object>> findTopUsersByPeriod(@Param("start") LocalDateTime start, 
                                                  @Param("end") LocalDateTime end);

    @Query("SELECT NEW map(al.action as action, COUNT(al) as count) " +
           "FROM AuditLog al " +
           "WHERE al.timestamp >= :start AND al.timestamp < :end " +
           "GROUP BY al.action")
    List<Map<String, Object>> findOperationCountsByType(@Param("start") LocalDateTime start, 
                                                       @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(al) FROM AuditLog al WHERE al.timestamp >= :start AND al.timestamp < :end")
    Long countByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(al) FROM AuditLog al WHERE al.timestamp >= :start AND al.timestamp < :end AND al.status = 'SUCCESS'")
    Long countSuccessByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(al) FROM AuditLog al WHERE al.timestamp >= :start AND al.timestamp < :end AND al.status = 'FAILURE'")
    Long countFailureByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}