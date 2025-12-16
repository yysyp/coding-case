package ps.demo.zglj.mq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message API", description = "API for managing messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    @Operation(summary = "Send a message")
    public MessageResponse sendMessage(@RequestBody MessageRequest request) {
        return messageService.sendMessage(request);
    }
}