// AuditLogController.java
package ps.demo.zglj.scheduledemo.controller;

import ps.demo.zglj.scheduledemo.dto.AuditLogRequest;
import ps.demo.zglj.scheduledemo.dto.DailyStatsResponse;
import ps.demo.zglj.scheduledemo.dto.ReportRequest;
import ps.demo.zglj.scheduledemo.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Management", description = "APIs for audit log management and daily reports")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping("/logs")
    @Operation(summary = "Record a new audit log entry")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Audit log accepted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> recordLog(@Valid @RequestBody AuditLogRequest request) {
        auditLogService.recordAuditLog(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/reports/daily")
    @Operation(summary = "Get daily statistics for a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics"),
        @ApiResponse(responseCode = "404", description = "No data found for the specified date"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DailyStatsResponse> getDailyStats(
            @Parameter(description = "Date for statistics (format: yyyy-MM-dd)", example = "2023-10-27")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        DailyStatsResponse stats = auditLogService.generateDailyStats(date.toString());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/reports/generate")
    @Operation(summary = "Generate daily report for a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Report generation started"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Report already exists for the date"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> generateReport(@Valid @RequestBody ReportRequest request) {
        auditLogService.generateAndSaveDailyReport(request.getReportDate().toString());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}