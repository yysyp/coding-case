// AuditLogService.java
package ps.demo.zglj.scheduledemo.service;

import ps.demo.zglj.scheduledemo.dto.AuditLogRequest;
import ps.demo.zglj.scheduledemo.dto.DailyStatsResponse;

public interface AuditLogService {
    void recordAuditLog(AuditLogRequest request);
    DailyStatsResponse generateDailyStats(String date);
    void generateAndSaveDailyReport(String date);
}