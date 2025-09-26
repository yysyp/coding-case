package ps.demo.jpademo.error;

import lombok.*;
import ps.demo.commonlibx.common.CodeEnum;

import java.text.MessageFormat;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class BaseErrorException extends RuntimeException {
    protected CodeEnum codeEnum;

    public BaseErrorException(CodeEnum codeEnum) {
        super(codeEnum.getCode());
        this.codeEnum = codeEnum;
    }

    public BaseErrorException(CodeEnum codeEnum, String message) {
        super(message);
        this.codeEnum = codeEnum;
    }

    public BaseErrorException(CodeEnum codeEnum, String message, Throwable cause) {
        super(message, cause);
        this.codeEnum = codeEnum;
    }

    public BaseErrorException(CodeEnum codeEnum, Throwable cause) {
        super(cause);
        this.codeEnum = codeEnum;
    }

    public static BaseErrorException of400(String message) {
        return new BaseErrorException(CodeEnum.BAD_REQUEST, message);
    }

    public static BaseErrorException of400(String message, Object... args) {
        String fmtMsg = MessageFormat.format(message, args);
        return new BaseErrorException(CodeEnum.BAD_REQUEST, fmtMsg);
    }

    public static BaseErrorException of500(String message) {
        return new BaseErrorException(CodeEnum.INTERNAL_SERVER_ERROR, message);
    }

    public static BaseErrorException of500(String message, Object... args) {
        String fmtMsg = MessageFormat.format(message, args);
        return new BaseErrorException(CodeEnum.BAD_REQUEST, fmtMsg);
    }

}
