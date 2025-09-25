package ps.demo.jpademo.dto;

import brave.Tracer;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JasyptResponse extends BaseSuccessResp {

    private String data;

    public JasyptResponse(Tracer tracer, String data) {
        super(tracer);
        this.data = data;
    }

}
