package ps.demo.commonlibx.common;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {

    /**
     * Email
     */
    public static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String REG_SYMBOL = "[\\pP\\pM\\pZ\\pS\\pC\\n]";

    /**
     * /pP lowercase p means property，means Unicode property，used in Unicode regex prefix。
     *
     * P：punctuation
     * L：letter
     * M：Mark (normal not use separately)
     * Z：separator symbol (i.e. blank, newline)
     * S：symbol (i.e. math, currency)
     * N：number (i.e. Arabic number, Roman number )
     * C：Other character
     *
     */

    public static final Pattern PATTERN_CHINESE_BY_REG =Pattern.compile("[\\u4E00-\\u9FBF]+");
    public static final Pattern PATTERN_CHINESE =Pattern.compile("[\u4E00-\u9FA5]+");
    public static final Pattern PATTERN_MESSY_CODE =Pattern.compile("\\s*|\t*|\r*|\n*");

    public static String removeSymbols(String s) {
        return removeSymbolsByReg(s, REG_SYMBOL);
    }

    public static String removeSymbolsExceptAlpNumberHyphenUnderline(String s) {
        return removeSymbolsByReg(s, "[^(a-zA-Z0-9\\-\\_)]");
    }

    public static String removeChinese(String s) {
        if (s == null) {
            return s;
        }
        return s.replaceAll("[\u4E00-\u9FA5]+", "");
    }

    public static String removeSymbolsByReg(String s, String reg) {
        if (s == null) {
            return s;
        }
        return s.replaceAll(reg, "");
    }

    public static String regularString(String s) {
        if (s == null) {
            return null;
        }
        return removeSymbols(s);
    }

    public static String findByRegex(String content, String regex, int groupI) {
        if (groupI < 0) {
            groupI = 0;
        }
        Pattern pt = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher mt = pt.matcher(content);
        while (mt.find()) {
            String matchStr = mt.group(groupI);
            return matchStr;
        }
        return "";
    }

    /**
     * verify whether string is matching the given regular expression
     *
     * @param str the string
     * @param reg regular expression
     * @return is matching?
     */
    public final static boolean isMatche(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * get the counts of sub matching strings
     *
     * @param str the whole string
     * @param reg regular expression
     * @return matching times
     */
    public final static int countSubStrReg(String str, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int i = 0;
        while (m.find()) {
            i++;
        }
        return i;
    }

    /**
     * check whether email is valid
     *
     * @param email string
     * @return is valid
     */
    public final static boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile(REG_EMAIL);
        return pattern.matcher(email).matches();
    }


    /**
     * check whether string is matching CJK, * only partial of Chinese included.
     * @param str
     * @return
     */
    public  static boolean isChineseByReg(String str) {
        if (str == null) {
            return false;
        }
        return PATTERN_CHINESE_BY_REG.matcher(str.trim()).find();
    }

    //  only partial of Chinese included.
    public  static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // \\p mean include，\\P mean exclude
        // \\p{Cn} means Unicode not included characters; \\P{Cn} means Unicode already defined characters
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }


    /**
     * All Chinese included, is Chinese check
     * @param strName
     * @return
     */
    public  static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether it's Chinese
     *
     * @param c
     * @return
     */
    public  static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * Count by Chinese character
     */
    public  static int chineseLength(String str) {
        Matcher m = PATTERN_CHINESE.matcher(str);
        int i = 0;
        while (m.find()) {
            String temp = m.group(0);
            i += temp.length();
        }
        return i;
    }

    /**
     * Check whether the is messy encoding characters
     *
     * @param strName
     * @return
     */
    public  static float isMessyCode(String strName) {
        Matcher m = PATTERN_MESSY_CODE.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (char c : ch){
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }

        return count / chLength;
    }

}
