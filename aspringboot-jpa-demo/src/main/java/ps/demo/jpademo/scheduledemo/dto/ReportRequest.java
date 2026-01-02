// ReportRequest.java
package ps.demo.jpademo.scheduledemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReportRequest {
    
    @NotNull(message = "Report date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;
}