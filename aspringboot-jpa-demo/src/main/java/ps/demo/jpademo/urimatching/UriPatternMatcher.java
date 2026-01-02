package ps.demo.jpademo.urimatching;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * URI模式匹配器工具类
 * 支持高性能的模式匹配和缓存
 */
@Slf4j
public class UriPatternMatcher {

    // 模式缓存
    private final Map<String, UriPattern> patternCache = new ConcurrentHashMap<>();

    // 预编译的模式列表（按优先级排序）
    private final List<UriPattern> compiledPatterns = new CopyOnWriteArrayList<>();

    // 单例实例
    private static final UriPatternMatcher INSTANCE = new UriPatternMatcher();

    private UriPatternMatcher() {
        // 私有构造函数
    }

    public static UriPatternMatcher getInstance() {
        return INSTANCE;
    }

    /**
     * 添加模式
     */
    public void addPattern(String pattern) {
        UriPattern compiled = compileAndCache(pattern);
        compiledPatterns.add(compiled);
        // 按优先级排序（高优先级在前）
        compiledPatterns.sort((p1, p2) -> Integer.compare(p2.getPriority(), p1.getPriority()));
    }

    /**
     * 批量添加模式
     */
    public void addPatterns(Collection<String> patterns) {
        for (String pattern : patterns) {
            addPattern(pattern);
        }
    }

    /**
     * 移除模式
     */
    public boolean removePattern(String pattern) {
        UriPattern compiled = patternCache.get(pattern);
        if (compiled != null) {
            compiledPatterns.remove(compiled);
            patternCache.remove(pattern);
            return true;
        }
        return false;
    }

    /**
     * 清空所有模式
     */
    public void clearPatterns() {
        compiledPatterns.clear();
        patternCache.clear();
    }

    /**
     * 匹配URI（返回第一个匹配的模式）
     */
    public boolean matches(String uri) {
        return findFirstMatchingPattern(uri) != null;
    }

    /**
     * 匹配URI（返回所有匹配的模式）
     */
    public List<String> findAllMatchingPatterns(String uri) {
        List<String> matchingPatterns = new ArrayList<>();
        for (UriPattern pattern : compiledPatterns) {
            if (pattern.matches(uri)) {
                matchingPatterns.add(pattern.getOriginalPattern());
            }
        }
        return matchingPatterns;
    }

    /**
     * 查找第一个匹配的模式
     */
    public String findFirstMatchingPattern(String uri) {
        for (UriPattern pattern : compiledPatterns) {
            if (pattern.matches(uri)) {
                return pattern.getOriginalPattern();
            }
        }
        return null;
    }

    /**
     * 编译并缓存模式
     */
    private UriPattern compileAndCache(String pattern) {
        return patternCache.computeIfAbsent(pattern, PatternCompiler::compile);
    }

    /**
     * 获取所有已编译的模式
     */
    public List<String> getAllPatterns() {
        return new ArrayList<>(patternCache.keySet());
    }

    /**
     * 获取模式数量
     */
    public int getPatternCount() {
        return compiledPatterns.size();
    }

    public static String normalizeUri(String uri) {
        if (uri == null) return "";

        String normalized = uri.trim();

        // 处理根路径
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
     * 静态工具方法：直接匹配
     */
    public static boolean match(String pattern, String uri) {
        try {
            UriPattern compiled = PatternCompiler.compile(pattern);
            String normalizedUri = normalizeUri(uri);
            return compiled.matches(normalizedUri);
        } catch (PatternSyntaxException e) {
            //log.warn("Invalid pattern: {}", pattern, e);
            throw e;
        }
    }

    /**
     * 静态工具方法：批量匹配
     */
    public static boolean anyMatch(Collection<String> patterns, String uri) {
        for (String pattern : patterns) {
            if (match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 静态工具方法：安全匹配（不抛出异常）
     */
    public static boolean safeMatch(String pattern, String uri) {
        try {
            return match(pattern, uri);
        } catch (Exception e) {
            log.debug("Pattern matching failed for pattern: {}, uri: {}", pattern, uri, e);
            return false;
        }
    }

    /**
     * 预编译模式并返回匹配器
     */
    public static UriPatternMatcher precompile(Collection<String> patterns) {
        UriPatternMatcher matcher = new UriPatternMatcher();
        matcher.addPatterns(patterns);
        return matcher;
    }
}