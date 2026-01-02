package ps.demo.jpademo.urimatching;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * URI模式编译器
 * 支持：
 * - .    : 匹配单个路径段中的任意字符（除了/）
 * - *    : 匹配单个路径段
 * - **   : 匹配多个路径段（包括空段）
 * - {var}: 路径变量（可选功能）
 */
@Slf4j
public class PatternCompiler {

    // 预定义字符转义映射
    private static final String[][] ESCAPE_SEQUENCES = {
            {"\\", "\\\\"},
            {"^", "\\^"},
            {"$", "\\$"},
            {"+", "\\+"},
            {"?", "\\?"},
            {"{", "\\{"},
            {"}", "\\}"},
            {"(", "\\("},
            {")", "\\)"},
            {"[", "\\["},
            {"]", "\\]"},
            {"|", "\\|"}
    };

    private PatternCompiler() {
        // 工具类，禁止实例化
    }

    /**
     * 编译URI模式到正则表达式
     */
    public static UriPattern compile(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new PatternSyntaxException("Pattern cannot be null or empty", pattern);
        }

        try {
            String normalizedPattern = normalizePattern(pattern);
            boolean hasDoubleAsterisk = pattern.contains("**");
            int priority = calculatePriority(pattern);

            String regex = convertToRegex(normalizedPattern);
            Pattern compiledPattern = Pattern.compile(regex);

            log.debug("Compiled pattern: '{}' -> '{}'", pattern, regex);
            return new UriPattern(pattern, compiledPattern, priority, hasDoubleAsterisk);

        } catch (Exception e) {
            throw new PatternSyntaxException("Failed to compile pattern: " + e.getMessage(), pattern);
        }
    }

    /**
     * 批量编译模式
     */
    public static List<UriPattern> compile(List<String> patterns) {
        List<UriPattern> compiledPatterns = new ArrayList<>();
        for (String pattern : patterns) {
            compiledPatterns.add(compile(pattern));
        }
        return compiledPatterns;
    }

    /**
     * 规范化模式
     */
    private static String normalizePattern(String pattern) {
        if (pattern == null) {
            throw new PatternSyntaxException("Pattern cannot be null", "null");
        }

        String normalized = pattern.trim();

        // 特殊处理根路径模式
        if (normalized.equals("/")) {
            return "";
        }

        // 如果模式只有斜杠，返回空字符串
        if (normalized.equals("/")) {
            return "";
        }

        // 移除开头和结尾的斜杠
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }

    /**
     * 计算模式优先级
     * 优先级规则：具体模式 > 通配模式，单星号 > 双星号
     */
    private static int calculatePriority(String pattern) {
        int priority = 0;

        // 不含通配符的模式优先级最高
        if (!pattern.contains("*") && !pattern.contains(".")) {
            priority += 1000;
        }

        // 计算具体路径段的权重
        String[] segments = pattern.split("/");
        for (String segment : segments) {
            if (segment.equals("**")) {
                priority -= 100; // 双星号降低优先级
            } else if (segment.contains("*")) {
                priority -= 50;  // 单星号降低优先级
            } else if (segment.contains(".")) {
                priority -= 25;  // 点号降低优先级
            } else {
                priority += 10;  // 具体路径段增加优先级
            }
        }

        return priority;
    }

    /**
     * 将URI模式转换为正则表达式
     */
    private static String convertToRegex(String pattern) {
        if (pattern.isEmpty()) {
            return "^/*$";
        }

        // 处理特殊的全匹配模式
        if (pattern.equals("**")) {
            return "^.*$";
        }

        StringBuilder regex = new StringBuilder("^");
        String[] segments = pattern.split("/");
        List<String> processedSegments = new ArrayList<>();

        // 预处理段，合并连续的双星号
        for (String segment : segments) {
            if (segment.isEmpty()) continue;

            if (segment.equals("**")) {
                // 如果前一个段也是双星号，跳过（避免重复）
                if (processedSegments.isEmpty() || !processedSegments.get(processedSegments.size() - 1).equals("**")) {
                    processedSegments.add("**");
                }
            } else {
                processedSegments.add(segment);
            }
        }

        // 构建正则表达式
        for (int i = 0; i < processedSegments.size(); i++) {
            String segment = processedSegments.get(i);

            if (segment.equals("**")) {
                boolean isFirst = (i == 0);
                boolean isLast = (i == processedSegments.size() - 1);

                if (isFirst && isLast) {
                    // 只有双星号：匹配所有
                    regex.append(".*");
                } else if (isFirst) {
                    // 开头双星号：匹配任意前缀
                    regex.append("(.*/)?");
                } else if (isLast) {
                    // 结尾双星号：匹配任意后缀
                    regex.append("/.*");
                } else {
                    // 中间双星号：匹配任意中间路径
                    regex.append("(/.*)?/");
                }
            } else {
                if (i > 0 && !processedSegments.get(i - 1).equals("**")) {
                    regex.append("/");
                }
                regex.append(convertSegmentToRegex(segment));
            }
        }

        regex.append("$");
        return regex.toString();
    }

    /**
     * 转换单个路径段为正则表达式
     */
    private static String convertSegmentToRegex(String segment) {
        if (segment.equals("*")) {
            // 单星号：匹配非空路径段
            return "[^/]+";
        }

        StringBuilder regexSegment = new StringBuilder();
        boolean inEscape = false;

        for (int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);

            if (inEscape) {
                regexSegment.append(escapeRegexChar(c));
                inEscape = false;
                continue;
            }

            if (c == '\\') {
                inEscape = true;
                continue;
            }

            switch (c) {
                case '.':
                    // 点号：匹配单个字符（除了/）
                    regexSegment.append("[^/]");
                    break;
                case '*':
                    // 星号：匹配零个或多个字符（除了/）
                    regexSegment.append("[^/]*");
                    break;
                default:
                    regexSegment.append(escapeRegexChar(c));
                    break;
            }
        }

        if (inEscape) {
            throw new PatternSyntaxException("Dangling escape character", segment);
        }

        return regexSegment.toString();
    }

    /**
     * 转义正则表达式特殊字符
     */
    private static String escapeRegexChar(char c) {
        for (String[] escape : ESCAPE_SEQUENCES) {
            if (escape[0].charAt(0) == c) {
                return escape[1];
            }
        }
        return String.valueOf(c);
    }

    /**
     * 验证模式语法
     */
    public static boolean isValidPattern(String pattern) {
        try {
            compile(pattern);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    /**
     * 提取模式中的变量名（可选功能）
     */
    public static List<String> extractVariables(String pattern) {
        List<String> variables = new ArrayList<>();
        // 简单实现，支持 {variable} 格式
        java.util.regex.Pattern varPattern = java.util.regex.Pattern.compile("\\{([^}]+)\\}");
        java.util.regex.Matcher matcher = varPattern.matcher(pattern);

        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }
}