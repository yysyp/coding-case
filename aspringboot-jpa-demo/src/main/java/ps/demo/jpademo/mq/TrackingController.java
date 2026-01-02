package ps.demo.jpademo.mq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
@Tag(name = "Tracking", description = "Message Tracking Management")
public class TrackingController {

    @Autowired
    private TrackingRepository trackingRepository;

    @GetMapping
    @Operation(summary = "Get all tracking records with pagination")
    public Page<TrackingEntity> getAllTrackingRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trackingRepository.findAll(pageable);
    }

    @GetMapping("/by-message-id/{messageId}")
    @Operation(summary = "Get tracking records by message ID")
    public Page<TrackingEntity> getTrackingRecordsByMessageId(
            @PathVariable Long messageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trackingRepository.findByMessageId(messageId, pageable);
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get tracking records by status")
    public Page<TrackingEntity> getTrackingRecordsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return trackingRepository.findByStatus(status, pageable);
    }
}