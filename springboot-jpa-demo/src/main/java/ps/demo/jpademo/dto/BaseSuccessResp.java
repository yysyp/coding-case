package ps.demo.jpademo.dto;


import brave.Tracer;
import lombok.*;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.commonlibx.common.ProjConstant;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseSuccessResp implements java.io.Serializable {

    //@Builder.Default
    protected String code = CodeEnum.SUCCESS.getCode();
    //@Builder.Default
    protected String message = CodeEnum.SUCCESS.getMsg();

    protected String traceId;
    protected String path;
    //@Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();

    public void initTracerId(Tracer tracer) {
        this.traceId = tracer.currentSpan().context().traceIdString();
    }

    public BaseSuccessResp(Tracer tracer) {
        this.initTracerId(tracer);
    }


}
