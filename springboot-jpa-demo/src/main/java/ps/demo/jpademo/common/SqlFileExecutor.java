package ps.demo.jpademo.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL文件执行工具类
 * 用于解析并执行SQL文件中的所有SQL语句
 *
 * // 基本使用方式
 * boolean success = SqlFileExecutor.executeSqlFile("path/to/your/sqlfile.sql");
 *
 * // 遇到错误时继续执行其他SQL语句
 * boolean success = SqlFileExecutor.executeSqlFile("path/to/your/sqlfile.sql", true);
 *
 * // 预览将要执行的SQL语句
 * List<String> statements = SqlFileExecutor.previewSqlStatements("path/to/your/sqlfile.sql");
 * for (String sql : statements) {
 *     System.out.println(sql);
 * }
 *
 */
public class SqlFileExecutor {

    /**
     * 执行SQL文件中的所有SQL语句
     * 
     * @param sqlFilePath SQL文件路径
     * @return 执行结果，true表示全部执行成功，false表示有执行失败的情况
     */
    public static boolean executeSqlFile(String sqlFilePath) {
        return executeSqlFile(sqlFilePath, false);
    }

    /**
     * 执行SQL文件中的所有SQL语句
     * 
     * @param sqlFilePath SQL文件路径
     * @param continueOnError 遇到错误是否继续执行
     * @return 执行结果，true表示全部执行成功，false表示有执行失败的情况
     */
    public static boolean executeSqlFile(String sqlFilePath, boolean continueOnError) {
        List<String> sqlStatements = parseSqlFile(sqlFilePath);
        if (sqlStatements == null || sqlStatements.isEmpty()) {
            return false;
        }

        Connection connection = null;
        Statement statement = null;
        boolean allSuccess = true;

        try {
            connection = DbTool.getConnection();
            statement = connection.createStatement();

            for (String sql : sqlStatements) {
                try {
                    statement.execute(sql);
                    System.out.println("Successfully executed: " + sql);
                } catch (SQLException e) {
                    allSuccess = false;
                    System.err.println("Failed to execute SQL: " + sql);
                    System.err.println("Error: " + e.getMessage());
                    
                    if (!continueOnError) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            allSuccess = false;
            System.err.println("Database connection error: " + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }

        return allSuccess;
    }

    /**
     * 解析SQL文件，提取所有SQL语句
     * 
     * @param sqlFilePath SQL文件路径
     * @return SQL语句列表
     */
    public static List<String> parseSqlFile(String sqlFilePath) {
        List<String> sqlStatements = new ArrayList<>();
        StringBuilder currentSql = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 移除行首尾空白字符
                line = line.trim();
                
                // 跳过空行和注释行
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }
                
                // 处理多行注释 /* ... */
                if (line.startsWith("/*")) {
                    // 跳过多行注释直到结束标记
                    while (!line.contains("*/") && (line = reader.readLine()) != null) {
                        // 继续读取直到找到注释结束标记
                    }
                    continue;
                }
                
                // 添加当前行到SQL构建器
                currentSql.append(line).append(" ");
                
                // 如果当前行以分号结尾，说明是一个完整的SQL语句
                if (line.endsWith(";")) {
                    String sql = currentSql.toString().trim();
                    // 移除末尾的分号
                    if (sql.endsWith(";")) {
                        sql = sql.substring(0, sql.length() - 1).trim();
                    }
                    
                    // 只添加非空的SQL语句
                    if (!sql.isEmpty()) {
                        sqlStatements.add(sql);
                    }
                    
                    // 重置StringBuilder以准备下一个SQL语句
                    currentSql.setLength(0);
                }
            }
            
            // 处理最后一个没有分号结尾的SQL语句（如果有）
            String remainingSql = currentSql.toString().trim();
            if (!remainingSql.isEmpty()) {
                if (remainingSql.endsWith(";")) {
                    remainingSql = remainingSql.substring(0, remainingSql.length() - 1).trim();
                }
                if (!remainingSql.isEmpty()) {
                    sqlStatements.add(remainingSql);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
            return null;
        }
        
        return sqlStatements;
    }

    /**
     * 获取SQL文件中的SQL语句数量（用于预览）
     * 
     * @param sqlFilePath SQL文件路径
     * @return SQL语句数量
     */
    public static int getSqlStatementCount(String sqlFilePath) {
        List<String> statements = parseSqlFile(sqlFilePath);
        return statements != null ? statements.size() : 0;
    }

    /**
     * 预览SQL文件中的所有SQL语句
     * 
     * @param sqlFilePath SQL文件路径
     * @return SQL语句列表
     */
    public static List<String> previewSqlStatements(String sqlFilePath) {
        return parseSqlFile(sqlFilePath);
    }
}
