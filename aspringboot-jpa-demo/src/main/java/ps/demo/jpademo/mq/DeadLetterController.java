package ps.demo.jpademo.mq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dead-letters")
@Tag(name = "DeadLetter", description = "Dead Letter Queue Management")
public class DeadLetterController {

    @Autowired
    private DeadLetterRepository deadLetterRepository;

    @GetMapping
    @Operation(summary = "Get all dead letters with pagination")
    public Page<DeadLetterEntity> getAllDeadLetters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deadLetterRepository.findAll(pageable);
    }

    @GetMapping("/by-message-id/{messageId}")
    @Operation(summary = "Get dead letters by message ID")
    public Page<DeadLetterEntity> getDeadLettersByMessageId(
            @PathVariable Long messageId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deadLetterRepository.findByMessageId(messageId, pageable);
    }
}