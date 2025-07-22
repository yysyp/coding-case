package ps.demo.commonlibx.common;

public class ServerErrorException extends RuntimeException {
    private CodeEnum codeEnum;

    public CodeEnum getCodeEnum() {
        return this.codeEnum;
    }

    public ServerErrorException(CodeEnum codeEnum) {
        super(codeEnum.getDetailedMessage());
        this.codeEnum = codeEnum;
    }
    public ServerErrorException(CodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getDetailedMessage(), cause);
        this.codeEnum = codeEnum;
    }

}
