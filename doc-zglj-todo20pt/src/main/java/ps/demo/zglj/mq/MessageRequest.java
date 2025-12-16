package ps.demo.zglj.mq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    private String content;
    private String recipient;
    private Boolean ordered;
}