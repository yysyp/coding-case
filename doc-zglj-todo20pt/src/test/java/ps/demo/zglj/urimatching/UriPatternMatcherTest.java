package ps.demo.zglj.urimatching;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UriPatternMatcherTest {

    private UriPatternMatcher matcher;

    @BeforeEach
    void setUp() {
        matcher = UriPatternMatcher.getInstance();
        matcher.clearPatterns();
    }

    @Test
    void testBasicMatching() {
        assertTrue(UriPatternMatcher.match("api/users", "api/users"));
        assertFalse(UriPatternMatcher.match("api/users", "api/posts"));
    }

    @Test
    void testDotPattern() {
        // 点号匹配任意单个字符
        assertTrue(UriPatternMatcher.match("api/us.rs", "api/users"));
        assertTrue(UriPatternMatcher.match("api/us.rs", "api/usars"));
        assertFalse(UriPatternMatcher.match("api/us.rs", "api/user"));
    }

    @Test
    void testSingleAsterisk() {
        // 单星号匹配单个路径段
        assertTrue(UriPatternMatcher.match("api/*/detail", "api/users/detail"));
        assertTrue(UriPatternMatcher.match("api/*/detail", "api/posts/detail"));
        assertFalse(UriPatternMatcher.match("api/*/detail", "api/users/posts/detail"));
    }

    @Test
    void testDoubleAsterisk() {
        // 双星号匹配多个路径段
        assertTrue(UriPatternMatcher.match("api/**", "api/users/123/details"));
        assertTrue(UriPatternMatcher.match("api/**/detail", "api/users/123/detail"));
        assertTrue(UriPatternMatcher.match("**/detail", "api/users/detail"));
    }

    @Test
    void testMixedPatterns() {
        // 混合使用通配符
        assertTrue(UriPatternMatcher.match("api/*/u*rs/**", "api/v1/users/123/details"));
        assertTrue(UriPatternMatcher.match("*.html", "index.html"));
        assertTrue(UriPatternMatcher.match("static/**/*.css", "static/css/main.css"));
    }

    @Test
    void testPriorityMatching() {
        matcher.addPattern("api/**");          // 低优先级
        matcher.addPattern("api/users/*");     // 中优先级
        matcher.addPattern("api/users/123");   // 高优先级

        // 应该匹配最具体的模式
        assertEquals("api/users/123", matcher.findFirstMatchingPattern("api/users/123"));
        assertEquals("api/users/*", matcher.findFirstMatchingPattern("api/users/456"));
        assertEquals("api/**", matcher.findFirstMatchingPattern("api/posts/123"));
    }

    @Test
    void testEdgeCases() {
        // 空路径
        assertThrows(PatternSyntaxException.class, () -> UriPatternMatcher.match("", ""));
        assertThrows(PatternSyntaxException.class, () -> UriPatternMatcher.match("", "api"));

        // 根路径
        assertTrue(UriPatternMatcher.match("/", ""));
        assertTrue(UriPatternMatcher.match("/", "/"));

        // 双星号在开头
        assertTrue(UriPatternMatcher.match("**/api", "v1/api"));
        assertTrue(UriPatternMatcher.match("**/api", "api"));
    }

    @Test
    void testInvalidPatterns() {
        assertThrows(PatternSyntaxException.class, () -> UriPatternMatcher.match("api/\\", "api/test"));
        assertThrows(PatternSyntaxException.class, () -> UriPatternMatcher.match(null, "api/test"));
    }

    @Test
    void testSafeMatch() {
        assertTrue(UriPatternMatcher.safeMatch("api/users", "api/users"));
        assertFalse(UriPatternMatcher.safeMatch("invalid[pattern", "api/users")); // 不会抛出异常
    }

    @Test
    void testBatchMatching() {
        List<String> patterns = Arrays.asList("api/users/*", "api/posts/**", "static/*.css");

        assertTrue(UriPatternMatcher.anyMatch(patterns, "api/users/123"));
        assertTrue(UriPatternMatcher.anyMatch(patterns, "api/posts/123/comments"));
        assertTrue(UriPatternMatcher.anyMatch(patterns, "static/main.css"));
        assertFalse(UriPatternMatcher.anyMatch(patterns, "api/comments/123"));
    }

    @Test
    void testDoubleAsteriskPatterns() {
        // 开头双星号
        assertTrue(UriPatternMatcher.match("**/api", "v1/api"));
        assertTrue(UriPatternMatcher.match("**/api", "api"));
        assertTrue(UriPatternMatcher.match("**/api", "a/b/c/api"));

        // 结尾双星号
        assertTrue(UriPatternMatcher.match("api/**", "api/v1"));
        assertTrue(UriPatternMatcher.match("api/**", "api/v1/users"));
        assertFalse(UriPatternMatcher.match("api/**", "api")); // 需要至少一个路径段

        // 中间双星号
        assertTrue(UriPatternMatcher.match("api/**/detail", "api/detail"));
        assertTrue(UriPatternMatcher.match("api/**/detail", "api/v1/detail"));
        assertTrue(UriPatternMatcher.match("api/**/detail", "api/v1/v2/detail"));

        // 多个双星号
        assertTrue(UriPatternMatcher.match("**/api/**", "v1/api/v2"));
        assertTrue(UriPatternMatcher.match("a/**/b/**/c", "a/x/b/y/c"));

        // 全匹配
        assertTrue(UriPatternMatcher.match("**", "any/path"));
        assertTrue(UriPatternMatcher.match("**", ""));
    }
}