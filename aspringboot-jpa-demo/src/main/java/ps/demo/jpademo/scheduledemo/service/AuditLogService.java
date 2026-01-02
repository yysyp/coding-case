// AuditLogService.java
package ps.demo.jpademo.scheduledemo.service;

import ps.demo.jpademo.scheduledemo.dto.AuditLogRequest;
import ps.demo.jpademo.scheduledemo.dto.DailyStatsResponse;

public interface AuditLogService {
    void recordAuditLog(AuditLogRequest request);
    DailyStatsResponse generateDailyStats(String date);
    void generateAndSaveDailyReport(String date);
}