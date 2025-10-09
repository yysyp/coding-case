package ps.demo.jpademo.dto;

import brave.Tracer;
import lombok.*;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.commonlibx.common.ProjConstant;
import ps.demo.jpademo.config.TraceIdContext;
import ps.demo.jpademo.error.BaseErrorException;

@Builder
//@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseErrorResp implements java.io.Serializable {

    protected String code;
    protected String message;
    protected String detailMessage;

    protected String traceId = TraceIdContext.getCurrentTraceId();
    protected String path;
    //@Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();

    public BaseErrorResp(BaseErrorException exception) {
        this.code = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMsg();
        this.detailMessage = exception.getMessage();
    }

}
