package ps.demo.commonlibx.common;

public class ClientErrorException extends RuntimeException {
    private CodeEnum codeEnum;

    public CodeEnum getCodeEnum() {
        return this.codeEnum;
    }

    public ClientErrorException(CodeEnum codeEnum) {
        super(codeEnum.getDetailedMessage());
        this.codeEnum = codeEnum;
    }

    public ClientErrorException(CodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getDetailedMessage(), cause);
        this.codeEnum = codeEnum;
    }
}
