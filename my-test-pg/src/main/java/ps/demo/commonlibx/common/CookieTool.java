package ps.demo.commonlibx.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
public class CookieTool {
    /**
     * Add cookie info, not set valid time, by default it will invalid after browser close.
     *
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, -1, false, true, false);
    }

    /**
     *
     * secure true: cookie will only sending within https.
     * HttpOnly true: not allowed to access via JS script, Applet etc.
     *
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean isURLEncode, boolean isHttpOnly, boolean isSecure) {
        try {
            Cookie cookie = new Cookie(name, isURLEncode ? URLEncoder.encode(value, "UTF-8") : value);
            if (maxAge > 0) {
                cookie.setMaxAge(maxAge);
            }

            cookie.setPath("/");
            cookie.setHttpOnly(isHttpOnly);
            cookie.setSecure(isSecure);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {

            log.error("--->>addCookie occurs exception, caused by: ", e);
        }
    }

    /**
     *
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        Cookie cookie = getCookie(request, name);
        if (null != cookie) {
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * get cookie object
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     *
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     *
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean isURLEncode) {
        if (StringUtils.isEmpty(value)) {
            return;
        }

        addCookie(response, name, value, maxAge, isURLEncode, true, false);
    }

    /**
     *
     */
    public static String getCookieValue(HttpServletRequest request, String name, boolean isDecode) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        try {
            Cookie cookie = getCookie(request, name);
            if (cookie != null) {
                return isDecode ? URLDecoder.decode(cookie.getValue(), "UTF-8") : cookie.getValue();
            }
        } catch (UnsupportedEncodingException e) {
            log.error("--->>getCookieValue occurs exception, caused by: ", e);
        }
        return null;
    }
}
