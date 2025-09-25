package ps.demo.jpademo.dto;

import brave.Tracer;
import lombok.*;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.commonlibx.common.ProjConstant;
import ps.demo.jpademo.error.BaseErrorException;

@Builder
//@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseErrorResp implements java.io.Serializable {

    protected String code = CodeEnum.INTERNAL_SERVER_ERROR.getCode();
    protected String message = CodeEnum.INTERNAL_SERVER_ERROR.getMsg();
    protected String detailMessage;

    protected String traceId;
    protected String path;
    //@Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();

    public void initTracerId(Tracer tracer) {
        this.traceId = tracer.currentSpan().context().traceIdString();
    }

    public BaseErrorResp(Tracer tracer, BaseErrorException exception) {
        initTracerId(tracer);
        this.code = exception.getCodeEnum().getCode();
        this.message = exception.getFormattedMessage();
        this.detailMessage = exception.getMessage();
    }

}
