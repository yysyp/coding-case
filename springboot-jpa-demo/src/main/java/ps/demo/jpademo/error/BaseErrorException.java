package ps.demo.jpademo.error;

import lombok.*;
import ps.demo.commonlibx.common.CodeEnum;

@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class BaseErrorException extends RuntimeException {
    protected CodeEnum codeEnum;
    protected String[] args;

    public BaseErrorException(CodeEnum codeEnum) {
        super(codeEnum.getCode());
        this.codeEnum = codeEnum;
    }

    public BaseErrorException(CodeEnum codeEnum, String ... args) {
        super(codeEnum.getCode());
        this.codeEnum = codeEnum;
        this.args = args;
    }



    public BaseErrorException(String message) {
        super(message);
        this.codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
    }

    public BaseErrorException(Throwable throwable) {
        super(throwable);
        this.codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
    }

    public BaseErrorException(String message, Throwable throwable) {
        super(message, throwable);
        this.codeEnum = CodeEnum.INTERNAL_SERVER_ERROR;
    }

    public String getFormattedMessage() {
        if (this.args != null && this.args.length > 0) {
            return this.codeEnum.getMsg(this.args);
        }
        return this.codeEnum.getMsg();
    }

}
