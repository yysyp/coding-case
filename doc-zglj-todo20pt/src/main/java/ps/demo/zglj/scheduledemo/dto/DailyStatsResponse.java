// DailyStatsResponse.java
package ps.demo.zglj.scheduledemo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class DailyStatsResponse {
    private String reportDate;
    private Integer totalOperations;
    private Integer successfulOperations;
    private Integer failedOperations;
    private Map<String, Integer> topUsers;
    private Map<String, Integer> operationsByType;
    private LocalDateTime generatedAt;
    private String status;
}