package ps.demo.zglj.error;

import lombok.*;
import ps.demo.zglj.common.CodeEnum;


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

    public BaseErrorException(String message, Throwable cause) {
        super(message, cause);
        this.codeEnum = CodeEnum.BAD_REQUEST;
    }

    public BaseErrorException(String message, Throwable cause, Boolean serviceErr) {
        super(message, cause);
        if (serviceErr != null && serviceErr) {
            this.codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
        } else {
            this.codeEnum = CodeEnum.BAD_REQUEST;
        }
    }

    public BaseErrorException(CodeEnum codeEnum, Throwable cause) {
        super(cause);
        this.codeEnum = codeEnum;
    }

    public BaseErrorException(Throwable cause) {
        super(cause);
        this.codeEnum = CodeEnum.BAD_REQUEST;
    }

    public BaseErrorException(Throwable cause, Boolean serviceErr) {
        super(cause);
        if (serviceErr != null && serviceErr) {
            this.codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
        } else {
            this.codeEnum = CodeEnum.BAD_REQUEST;
        }
    }

    public static BaseErrorException of400(String message) {
        return new BaseErrorException(CodeEnum.BAD_REQUEST, message);
    }

    public static BaseErrorException of400(String message, Throwable cause) {
        return new BaseErrorException(CodeEnum.BAD_REQUEST, message, cause);
    }

    public static BaseErrorException of500(String message) {
        return new BaseErrorException(CodeEnum.INTERNAL_SERVER_ERROR, message);
    }

    public static BaseErrorException of500(String message, Throwable cause) {
        return new BaseErrorException(CodeEnum.INTERNAL_SERVER_ERROR, message, cause);
    }

//    public static BaseErrorException of400Fmt(String message, Object... args) {
//        String fmtMsg = MessageFormat.format(message, args);
//        return new BaseErrorException(CodeEnum.BAD_REQUEST, fmtMsg);
//    }
//    public static BaseErrorException of500Fmt(String message, Object... args) {
//        String fmtMsg = MessageFormat.format(message, args);
//        return new BaseErrorException(CodeEnum.BAD_REQUEST, fmtMsg);
//    }

}
