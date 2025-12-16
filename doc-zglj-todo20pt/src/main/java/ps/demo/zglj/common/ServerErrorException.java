package ps.demo.zglj.common;

public class ServerErrorException extends RuntimeException {
    private CodeEnum codeEnum;

    public CodeEnum getCodeEnum() {
        return this.codeEnum;
    }

    public ServerErrorException(CodeEnum codeEnum) {
        super(codeEnum.getMsg());
        this.codeEnum = codeEnum;
    }
    public ServerErrorException(CodeEnum codeEnum, Throwable cause) {
        super(codeEnum.getMsg(), cause);
        this.codeEnum = codeEnum;
    }

}
