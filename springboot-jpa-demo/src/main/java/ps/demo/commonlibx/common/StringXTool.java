package ps.demo.commonlibx.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class StringXTool {
    public static final char UNDERLINE = '_';
    public static final char HYPHEN = '-';
    private static final int FILE_NAME_LENGTH = 128;

    public static final String DIR_SEPERATOR = "/";

    public static String randomAscii(int bytes) {
        return RandomStringUtils.randomAscii(bytes);
    }

    public static String randomAlphabetic(int bytes) {
        return RandomStringUtils.randomAlphabetic(bytes);
    }

    public static String toValidFileName(String input) {
        return StringUtils.abbreviate(input.replaceAll("[:\\\\/*\"?|<>']",
                "-"), FILE_NAME_LENGTH);
    }

    /**
     * A - 1, B - 2, .. Z - 26, AA - 27
     * @param name
     * @return
     */
    public static int excelColToNum(String name) {
        int number = 0;
        for (int i = 0; i < name.length(); i++) {
            number = number * 26 + (name.charAt(i) - ('A' - 1));
        }
        return number;
    }

    /**
     * 1 - A, 2 - B, .. 26 - Z, 27 - AA
     * @param number
     * @return
     */
    public static String excelNumToCol(int number) {
        StringBuilder sb = new StringBuilder();
        while (number-- > 0) {
            sb.append((char)('A' + (number % 26)));
            number /= 26;
        }
        return sb.reverse().toString();
    }

    public static String toJavaName(String dbName) {
        if (StringUtils.isBlank(dbName)) {
            return dbName;
        }
        int len = dbName.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = dbName.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(dbName.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toDbName(String javaAttr) {
        return camelCaseToNewChar(javaAttr, UNDERLINE);
    }

    public static String toUriName(String javaAttr) {
        return camelCaseToNewChar(javaAttr, HYPHEN);
    }

    public static String camelCaseToNewChar(String camelVariable, char ch) {
        if (StringUtils.isBlank(camelVariable)) {
            return camelVariable;
        }
        String uncCamelVriable = StringUtils.uncapitalize(camelVariable);
        int len = uncCamelVriable.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = uncCamelVriable.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(ch);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String getLowerCaseBeanName(Class klass) {
        if (klass == null) {
            return null;
        }
        String s = klass.getName();
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        return StringUtils.lowerCase("" + s.charAt(0)).concat(s.substring(1));
    }

    public static String lowerCaseFirstChar(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }



    private static boolean _eitherContains(String x, String y) {
        if (StringUtils.isBlank(x) || StringUtils.isBlank(y)) {
            return false;
        }
        return x.contains(y) || y.contains(x);
    }

    public static int getLevenshteinDistance(String x, String y) {
        int m = x.length();
        int n = y.length();

        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }

        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = x.charAt(i - 1) == y.charAt(j - 1) ? 0 : 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }

        return T[m][n];
    }



    public static int getLongestCommonSequence(String x, String y) {

        int m = x.length(), n = y.length();

        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = 0;
            }
        }

        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[m][n];
    }




    public static Date tryStrToDate(String dateStr, String... fmts) {
        if (fmts == null || fmts.length == 0) {

            fmts = new String[]{"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "yyyy-MM-dd'T'HH:mm:ss",
                    "yyyy-MM-dd HH:mm:ss",
                    "yyyy-MM-dd",
                    "yyyy-MM",
                    "HH:mm:ss",
                    "HH:mm"};
        }
        for (String fmt : fmts) {
            try {
                return new SimpleDateFormat(fmt).parse(dateStr);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public static String getNowDateStrOnly() {
        return getNowStr("yyyy-MM-dd");
    }

    public static String getNowTimeStrOnly() {
        return getNowStr("HHmmss");
    }

    public static String getNowTimeWithMsStrOnly() {
        return getNowStr("HHmmssSSS");
    }

    public static String getNowStr() {
        return getNowStr("yyyy-MM-dd_HHmmss");
    }

    public static String getNowStr(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static void printOut(Collection c) {
        printOut(c, System.out);
    }

    public static void printOut(Collection c, PrintStream out) {
        if (CollectionUtils.isEmpty(c)) {
            out.println("[]");
        }
        int i = 0;
        for (Object o : c) {
            out.println("["+i+++"] " + o);
        }
    }

    public static String readLineFromSystemIn(String prompt) {
        if (StringUtils.isBlank(prompt)) {
            prompt = "Please input: ";
        }
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        scanner.close();
        return line;
    }



}
