package ps.demo.jpademo.common;

public class ClientErrorException extends RuntimeException {
    private CodeEnum codeEnum;

    public CodeEnum getCodeEnum() {
        return this.codeEnum;
    }

    public ClientErrorException(CodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }

    public ClientErrorException(CodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getMsg(), cause);
        this.codeEnum = codeEnum;
    }
}
