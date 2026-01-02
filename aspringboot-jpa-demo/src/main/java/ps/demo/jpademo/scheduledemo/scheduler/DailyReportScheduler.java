// DailyReportScheduler.java
package ps.demo.jpademo.scheduledemo.scheduler;
import ps.demo.jpademo.scheduledemo.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyReportScheduler {

    private final AuditLogService auditLogService;

    @Value("${app.scheduler.locks.daily-report.lock-at-most-for}")
    private String lockAtMostFor;

    @Value("${app.scheduler.locks.daily-report.lock-at-least-for}")
    private String lockAtLeastFor;

    @Value("${app.scheduler.daily-report.cron}")
    private String dailyReportCron;

    @Value("${app.scheduler.weekly-report.cron}")
    private String weeklyReportCron;

    @Scheduled(cron = "${app.scheduler.daily-report.cron:0 0 1 * * ?}")
    @SchedulerLock(
            name = "${app.scheduler.locks.daily-report.name}",
            lockAtMostFor = "${app.scheduler.locks.daily-report.lock-at-most-for}",
            lockAtLeastFor = "${app.scheduler.locks.daily-report.lock-at-least-for}"
    )
    public void generateDailyReport() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String dateString = yesterday.format(DateTimeFormatter.ISO_DATE);

        log.info("Starting daily report generation for date: {}", dateString);
        log.debug("Lock configuration - AtMostFor: {}, AtLeastFor: {}", lockAtMostFor, lockAtLeastFor);

        try {
            auditLogService.generateAndSaveDailyReport(dateString);
            log.info("Daily report generation completed successfully for date: {}", dateString);
        } catch (Exception e) {
            log.error("Critical error in daily report generation for date: {}", dateString, e);
        }
    }

    @Scheduled(cron = "${app.scheduler.weekly-report.cron:0 0 2 * * MON}")
    @SchedulerLock(
            name = "${app.scheduler.locks.weekly-report.name}",
            lockAtMostFor = "${app.scheduler.locks.weekly-report.lock-at-most-for}",
            lockAtLeastFor = "${app.scheduler.locks.weekly-report.lock-at-least-for}"
    )
    public void generateWeeklyReport() {
        log.info("Starting weekly report generation");
        log.debug("Lock configuration - AtMostFor: {}, AtLeastFor: {}",
                "${app.scheduler.locks.weekly-report.lock-at-most-for}",
                "${app.scheduler.locks.weekly-report.lock-at-least-for}");
        // Implementation for weekly reports
    }
}