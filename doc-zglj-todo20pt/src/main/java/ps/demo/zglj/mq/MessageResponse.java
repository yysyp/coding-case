package ps.demo.zglj.mq;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private Long id;
    private String content;
    private String recipient;
    private String status;
    private Boolean ordered;
}