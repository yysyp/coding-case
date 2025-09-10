// AuditDailyReportRepository.java
package ps.demo.jpademo.scheduledemo.repository;

import ps.demo.jpademo.scheduledemo.entity.AuditDailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AuditDailyReportRepository extends JpaRepository<AuditDailyReport, Long> {

    Optional<AuditDailyReport> findByReportDate(LocalDate reportDate);

    @Query("SELECT CASE WHEN COUNT(adr) > 0 THEN true ELSE false END " +
           "FROM AuditDailyReport adr WHERE adr.reportDate = :reportDate")
    boolean existsByReportDate(@Param("reportDate") LocalDate reportDate);
}