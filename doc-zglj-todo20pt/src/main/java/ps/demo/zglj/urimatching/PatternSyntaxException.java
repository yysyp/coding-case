package ps.demo.zglj.urimatching;


/**
 * URI模式语法异常
 */
public class PatternSyntaxException extends RuntimeException {
    private final String pattern;
    private final String message;

    public PatternSyntaxException(String message, String pattern) {
        super(message + " Pattern: " + pattern);
        this.pattern = pattern;
        this.message = message;
    }

    public String getPattern() {
        return pattern;
    }

    public String getErrorMessage() {
        return message;
    }
}