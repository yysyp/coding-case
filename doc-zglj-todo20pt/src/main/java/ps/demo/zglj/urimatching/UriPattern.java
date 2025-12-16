package ps.demo.zglj.urimatching;

import lombok.Data;
import java.util.regex.Pattern;

/**
 * URI模式实体
 */
@Data
public class UriPattern {
    /**
     * 原始模式字符串
     */
    private final String originalPattern;

    /**
     * 编译后的正则表达式
     */
    private final Pattern compiledPattern;

    /**
     * 模式优先级（用于匹配顺序）
     */
    private final int priority;

    /**
     * 是否包含双星号（**）模式
     */
    private final boolean hasDoubleAsterisk;

    public UriPattern(String originalPattern, Pattern compiledPattern, int priority, boolean hasDoubleAsterisk) {
        this.originalPattern = originalPattern;
        this.compiledPattern = compiledPattern;
        this.priority = priority;
        this.hasDoubleAsterisk = hasDoubleAsterisk;
    }

    /**
     * 匹配URI
     */
    public boolean matches(String uri) {
        return compiledPattern.matcher(uri).matches();
    }
}
