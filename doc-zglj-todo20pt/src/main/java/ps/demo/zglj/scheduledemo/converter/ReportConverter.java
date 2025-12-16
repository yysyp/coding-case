// ReportConverter.java
package ps.demo.zglj.scheduledemo.converter;

import ps.demo.zglj.scheduledemo.dto.DailyStatsResponse;
import ps.demo.zglj.scheduledemo.entity.AuditDailyReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportConverter {

    private final ObjectMapper objectMapper;

    public DailyStatsResponse toResponse(AuditDailyReport report) {
        if (report == null) {
            return null;
        }

        try {
            DailyStatsResponse response = new DailyStatsResponse();
            response.setReportDate(report.getReportDate().toString());
            response.setTotalOperations(report.getTotalOperations());
            response.setSuccessfulOperations(report.getSuccessfulOperations());
            response.setFailedOperations(report.getFailedOperations());
            response.setGeneratedAt(report.getGeneratedAt());
            response.setStatus(report.getStatus());
            
            // Parse JSON fields
            Map<String, Integer> topUsers = objectMapper.readValue(
                report.getTopUsersJson(), 
                new TypeReference<Map<String, Integer>>() {}
            );
            Map<String, Integer> operationsByType = objectMapper.readValue(
                report.getOperationsByTypeJson(), 
                new TypeReference<Map<String, Integer>>() {}
            );
            
            response.setTopUsers(topUsers);
            response.setOperationsByType(operationsByType);
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert report to response", e);
        }
    }
}