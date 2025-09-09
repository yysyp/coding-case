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

    public static BaseResp withErrorMsg(Tracer tracer, CodeEnum c) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(c.getCode());
        resp.setMessage(c.getDetailedMessage());
        return resp;
    }

    public static BaseResp withErrorMsg(Tracer tracer, CodeEnum c, Exception e) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(c.getCode());
        resp.setMessage(c.getDetailedMessage(e.getMessage()));
        return resp;
    }

    public static BaseResp withErrorMsg(Tracer tracer, CodeEnum c, String...msgArgs) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(c.getCode());
        resp.setMessage(c.getDetailedMessage(msgArgs));
        return resp;
    }

    public static BaseResp withErrorMsg(Tracer tracer, Exception e) {
        BaseResp resp = new BaseResp();
        resp.initTracerId(tracer);
        resp.setCode(CodeEnum.MY_ERROR_MSG.getCode());
        resp.setMessage(CodeEnum.MY_ERROR_MSG.getDetailedMessage(e.getMessage()));
        return resp;
    }

}
