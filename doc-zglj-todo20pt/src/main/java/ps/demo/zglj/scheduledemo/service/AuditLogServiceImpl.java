// AuditLogServiceImpl.java
package ps.demo.zglj.scheduledemo.service;

import ps.demo.zglj.scheduledemo.dto.AuditLogRequest;
import ps.demo.zglj.scheduledemo.dto.DailyStatsResponse;
import ps.demo.zglj.scheduledemo.entity.AuditDailyReport;
import ps.demo.zglj.scheduledemo.entity.AuditLog;
import ps.demo.zglj.scheduledemo.repository.AuditDailyReportRepository;
import ps.demo.zglj.scheduledemo.repository.AuditLogRepository;
import ps.demo.zglj.scheduledemo.service.AuditLogService;
import ps.demo.zglj.scheduledemo.converter.AuditLogConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditDailyReportRepository auditDailyReportRepository;
    private final AuditLogConverter auditLogConverter;
    private final ObjectMapper objectMapper;

    @Override
    @Async("auditLogTaskExecutor")
    @Transactional
    public void recordAuditLog(AuditLogRequest request) {
        try {
            AuditLog auditLog = auditLogConverter.toEntity(request);
            
            String currentUser = "ANONYMOUS";
            auditLog.setCreatedBy(currentUser);
            auditLog.setUpdatedBy(currentUser);

            auditLogRepository.save(auditLog);
            log.debug("Audit log recorded successfully for user: {}", request.getUserId());
        } catch (Exception e) {
            log.error("Failed to record audit log for request: {}", request, e);
            throw new RuntimeException("Failed to record audit log", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DailyStatsResponse generateDailyStats(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        Long total = auditLogRepository.countByPeriod(startOfDay, endOfDay);
        Long success = auditLogRepository.countSuccessByPeriod(startOfDay, endOfDay);
        Long failure = auditLogRepository.countFailureByPeriod(startOfDay, endOfDay);

        List<Map<String, Object>> userCounts = auditLogRepository.findTopUsersByPeriod(startOfDay, endOfDay);
        List<Map<String, Object>> typeCounts = auditLogRepository.findOperationCountsByType(startOfDay, endOfDay);

        DailyStatsResponse response = new DailyStatsResponse();
        response.setReportDate(date);
        response.setTotalOperations(total != null ? total.intValue() : 0);
        response.setSuccessfulOperations(success != null ? success.intValue() : 0);
        response.setFailedOperations(failure != null ? failure.intValue() : 0);
        response.setGeneratedAt(LocalDateTime.now());
        response.setStatus("GENERATED");

        response.setTopUsers(userCounts.stream()
                .collect(Collectors.toMap(
                    m -> (String) m.get("userId"), 
                    m -> ((Long) m.get("count")).intValue()
                )));
        response.setOperationsByType(typeCounts.stream()
                .collect(Collectors.toMap(
                    m -> (String) m.get("action"), 
                    m -> ((Long) m.get("count")).intValue()
                )));

        return response;
    }

    @Override
    @Transactional
    public void generateAndSaveDailyReport(String date) {
        try {
            LocalDate reportDate = LocalDate.parse(date);
            
            // Check if report already exists
            if (auditDailyReportRepository.existsByReportDate(reportDate)) {
                log.warn("Daily report already exists for date: {}", date);
                return;
            }

            DailyStatsResponse stats = generateDailyStats(date);
            
            AuditDailyReport report = new AuditDailyReport();
            report.setReportDate(reportDate);
            report.setTotalOperations(stats.getTotalOperations());
            report.setSuccessfulOperations(stats.getSuccessfulOperations());
            report.setFailedOperations(stats.getFailedOperations());
            report.setGeneratedAt(stats.getGeneratedAt());
            report.setStatus("GENERATED");
            
            // Convert maps to JSON
            report.setTopUsersJson(objectMapper.writeValueAsString(stats.getTopUsers()));
            report.setOperationsByTypeJson(objectMapper.writeValueAsString(stats.getOperationsByType()));
            
            String currentUser = "SYSTEM";
            report.setCreatedBy(currentUser);
            report.setUpdatedBy(currentUser);

            auditDailyReportRepository.save(report);
            log.info("Daily report generated and saved successfully for date: {}", date);
            
        } catch (Exception e) {
            log.error("Failed to generate daily report for date: {}", date, e);
            throw new RuntimeException("Failed to generate daily report", e);
        }
    }
}