package ps.demo.jpademo.cucumber.base;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class CcmbTool {

    @SneakyThrows
    public static String loadFileByClassPath(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);
        return resource.getContentAsString(StandardCharsets.UTF_8);
    }

    public static String replaceAll(String str, Map<String, Object> replaceMap) {
        for (String key : replaceMap.keySet()) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            Object val = replaceMap.get(key);
            if (val == null || val.toString().trim().length() == 0) {
                continue;
            }
            str = str.replaceAll(key, val.toString().trim());
        }
        return str;
    }



    public static List<String> advancedSqlSplit(String sql) {
        List<String> sqlList = new ArrayList<>();
        if (StringUtils.isBlank(sql)) {
            return sqlList;
        }

        StringBuilder currentSql = new StringBuilder();
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);

            if (i > 0 && sql.charAt(i - 1) == '\\' &&
                    (inSingleQuote || inDoubleQuote)) {
                currentSql.append(ch);
                continue;
            }

            if (!inSingleQuote && !inDoubleQuote && !inBlockComment &&
                    ch == '-' && i + 1 < sql.length() && sql.charAt(i + 1) == '-') {
                inLineComment = true;
                i++; //
                continue;
            }


            if (!inSingleQuote && !inDoubleQuote && !inLineComment &&
                    ch == '/' && i + 1 < sql.length() && sql.charAt(i + 1) == '*') {
                inBlockComment = true;
                i++;
                continue;
            }


            if (inBlockComment && ch == '*' && i + 1 < sql.length() &&
                    sql.charAt(i + 1) == '/') {
                inBlockComment = false;
                i++;
                continue;
            }


            if (inLineComment && (ch == '\n' || ch == '\r')) {
                inLineComment = false;
            }


            if (!inLineComment && !inBlockComment) {
                if (ch == '\'' && !inDoubleQuote) {
                    inSingleQuote = !inSingleQuote;
                } else if (ch == '"' && !inSingleQuote) {
                    inDoubleQuote = !inDoubleQuote;
                }
            }


            if (ch == ';' && !inSingleQuote && !inDoubleQuote &&
                    !inLineComment && !inBlockComment) {
                String statement = currentSql.toString().trim();
                if (StringUtils.isNotBlank(statement)) {
                    sqlList.add(statement);
                }
                currentSql.setLength(0);
                continue;
            }


            if (!inLineComment && !inBlockComment) {
                currentSql.append(ch);
            }
        }


        String lastStatement = currentSql.toString().trim();
        if (StringUtils.isNotBlank(lastStatement)) {
            sqlList.add(lastStatement);
        }

        return sqlList;
    }


    public static String loadToStringAndReplaceAll(String fileName, Map<String, Object> replaceMap) {
        return replaceAll(loadFileByClassPath(fileName), replaceMap);
    }

    public static List<String> loadToListAndReplaceAll(String fileName, Map<String, Object> replaceMap) {
        return advancedSqlSplit(replaceAll(loadFileByClassPath(fileName), replaceMap));
    }


}
