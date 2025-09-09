package ps.demo.jpademo.dto;

import brave.Tracer;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JasyptResponse extends BaseResp {

    private String data;

    public static JasyptResponse withSuccessMsg(Tracer tracer, String data) {
        JasyptResponse resp = new JasyptResponse();
        resp.initTracerId(tracer);
        resp.setData(data);
        return resp;
    }


}
