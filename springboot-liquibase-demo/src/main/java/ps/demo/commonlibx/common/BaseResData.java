package ps.demo.commonlibx.common;


import lombok.*;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseResData<T> implements java.io.Serializable {

    private T data;

    @Builder.Default
    protected String code = CodeEnum.SUCCESS.getCode();
    @Builder.Default
    protected String message = CodeEnum.SUCCESS.getDetailedMessage();
    protected String detail;
    protected String trace;
    protected String correlationId;
    protected String instance;
    @Builder.Default
    protected String timestamp = ProjConstant.getNowDateStr();;
    protected String path;


}
