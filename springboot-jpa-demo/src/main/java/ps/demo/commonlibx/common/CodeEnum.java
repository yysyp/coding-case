package ps.demo.commonlibx.common;

import java.text.MessageFormat;

/**
 * Define all the error code, http status and error info.
 */
public enum CodeEnum {
    SUCCESS("200", 200, "success"),

    // Client Error
    BAD_REQUEST("400", 200, "Bad request"),
    INVALID_DATE("400", 200, "Invalid date time data"),
    INVALID_ID("400", 200, "Invalid ID"),

    CALL_LATER("400", 200, "Backend has is busy in processing calls, please retry later"),

    // Server Error
    INTERNAL_SERVER_ERROR("500", 200, "Server error"),
    DUPLICATED_KEY("10000", 200, "Duplicated key"),
    NO_ENOUGH_STOCK("10001", 200, "No enough stock"),
    CONCURRENT_OPERATION("10002", 200, "Operation conflicts"),
    MY_ERROR_MSG("10003", 200, "Error message is {0}");



    private String code;
    private int httpCode;
    private String msg;

    CodeEnum(String code, int httpStatus, String msg) {
        this.code = code;
        this.httpCode = httpStatus;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getMsg(String ... args) {
        return MessageFormat.format(this.msg, args);
    }


}
