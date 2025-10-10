package ps.demo.jpademo.dto;


import brave.Tracer;
import lombok.*;

import ps.demo.jpademo.common.CodeEnum;
import ps.demo.jpademo.common.ProjConstant;
import ps.demo.jpademo.config.TraceIdContext;

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

    protected String traceId = TraceIdContext.getCurrentTraceId();
    protected String path;
    //@Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();



}
