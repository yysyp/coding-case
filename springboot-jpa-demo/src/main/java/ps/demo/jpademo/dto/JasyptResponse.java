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

    public static JasyptResponse withSuccessMsg(String data, Tracer tracer) {
        JasyptResponse resp = new JasyptResponse();
        resp.initTracerId(tracer);
        resp.setData(data);
        return resp;
    }


}
