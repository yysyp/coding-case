package ps.demo.zglj.dto;

import brave.Tracer;
import lombok.*;


@ToString
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class JasyptResponse extends BaseSuccessResp {

    private String data;

    public JasyptResponse(String data) {
        this.data = data;
    }

}
