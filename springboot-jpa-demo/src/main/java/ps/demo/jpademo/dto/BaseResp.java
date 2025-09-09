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
public class BaseResp implements java.io.Serializable {

    @Builder.Default
    protected String code = CodeEnum.SUCCESS.getCode();
    @Builder.Default
    protected String message = CodeEnum.SUCCESS.getDetailedMessage();
    protected String detail;
    protected String traceId;
    protected String instance;
    protected String path;
    @Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();

    public void initTracerId(Tracer tracer) {
        this.traceId = tracer.currentSpan().context().traceIdString();
    }

    public static BaseResp withSuccessMsg(Tracer tracer) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        return resp;
    }

    public static BaseResp withErrorMsg(CodeEnum e, Tracer tracer) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(e.getCode());
        resp.setMessage(e.getDetailedMessage());
        return resp;
    }

    public static BaseResp withErrorMsg(Exception e, Tracer tracer) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(CodeEnum.INTERNAL_SERVER_ERROR.getCode());
        resp.setMessage(e.getMessage());
        return resp;
    }

}
