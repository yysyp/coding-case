package ps.demo.commonlibx.common;

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
    CONCURRENT_OPERATION("10002", 200, "Operation conflicts");


    private String code;
    private int httpCode;
    private String detailedMessage;

    CodeEnum(String code, int httpStatus, String detailedMessage) {
        this.code = code;
        this.httpCode = httpStatus;
        this.detailedMessage = detailedMessage;
    }

    public String getCode() {
        return this.code;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getDetailedMessage() {
        return this.detailedMessage;
    }


}
