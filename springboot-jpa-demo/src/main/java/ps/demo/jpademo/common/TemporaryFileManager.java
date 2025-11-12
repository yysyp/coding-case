package ps.demo.jpademo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 临时文件管理器
 * 提供创建临时文件的功能，并在JVM退出时自动清理这些文件
 */
public class TemporaryFileManager {
    
    // 存储所有创建的临时文件路径
    private static final List<Path> temporaryFiles = new ArrayList<>();
    
    // 静态初始化块，注册关闭钩子
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cleanupTemporaryFiles();
        }));
    }
    
    /**
     * 在指定目录下创建临时文件
     * 
     * @param directory 文件目录路径
     * @param prefix 文件名前缀
     * @param suffix 文件扩展名
     * @return 创建的临时文件路径
     * @throws IOException 如果创建文件失败
     */
    public static Path createTempFile(String directory, String prefix, String suffix) throws IOException {
        Path dirPath = Paths.get(directory);
        
        // 如果目录不存在，则创建目录
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // 创建临时文件
        Path tempFile = Files.createTempFile(dirPath, prefix, suffix);
        
        // 将文件添加到跟踪列表
        synchronized (temporaryFiles) {
            temporaryFiles.add(tempFile);
        }
        
        return tempFile;
    }
    
    /**
     * 在系统默认临时目录创建临时文件
     * 
     * @param prefix 文件名前缀
     * @param suffix 文件扩展名
     * @return 创建的临时文件路径
     * @throws IOException 如果创建文件失败
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return createTempFile(System.getProperty("java.io.tmpdir"), prefix, suffix);
    }
    
    /**
     * 创建无前缀和后缀的临时文件
     * 
     * @return 创建的临时文件路径
     * @throws IOException 如果创建文件失败
     */
    public static Path createTempFile() throws IOException {
        return createTempFile(null, null);
    }
    
    /**
     * 在指定目录下创建临时目录
     * 
     * @param directory 父目录路径
     * @param prefix 目录名前缀
     * @return 创建的临时目录路径
     * @throws IOException 如果创建目录失败
     */
    public static Path createTempDirectory(String directory, String prefix) throws IOException {
        Path dirPath = Paths.get(directory);
        
        // 如果父目录不存在，则创建父目录
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // 创建临时目录
        Path tempDir = Files.createTempDirectory(dirPath, prefix);
        
        // 将目录添加到跟踪列表
        synchronized (temporaryFiles) {
            temporaryFiles.add(tempDir);
        }
        
        return tempDir;
    }
    
    /**
     * 在系统默认临时目录创建临时目录
     * 
     * @param prefix 目录名前缀
     * @return 创建的临时目录路径
     * @throws IOException 如果创建目录失败
     */
    public static Path createTempDirectory(String prefix) throws IOException {
        return createTempDirectory(System.getProperty("java.io.tmpdir"), prefix);
    }
    
    /**
     * 手动清理所有临时文件和目录
     */
    public static void cleanupTemporaryFiles() {
        synchronized (temporaryFiles) {
            for (Path path : temporaryFiles) {
                try {
                    if (Files.exists(path)) {
                        if (Files.isDirectory(path)) {
                            deleteDirectoryRecursively(path);
                        } else {
                            Files.deleteIfExists(path);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Failed to delete temporary file/directory: " + path + " - " + e.getMessage());
                }
            }
            temporaryFiles.clear();
        }
    }
    
    /**
     * 递归删除目录及其内容
     * 
     * @param directory 要删除的目录路径
     * @throws IOException 如果删除失败
     */
    private static void deleteDirectoryRecursively(Path directory) throws IOException {
        if (Files.isDirectory(directory)) {
            // 删除目录中的所有文件和子目录
            Files.list(directory).forEach(child -> {
                try {
                    if (Files.isDirectory(child)) {
                        deleteDirectoryRecursively(child);
                    } else {
                        Files.deleteIfExists(child);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + child + " - " + e.getMessage());
                }
            });
        }
        // 删除空目录本身
        Files.deleteIfExists(directory);
    }
    
    /**
     * 获取当前跟踪的临时文件数量
     * 
     * @return 临时文件数量
     */
    public static int getTrackedFileCount() {
        synchronized (temporaryFiles) {
            return temporaryFiles.size();
        }
    }
    
    /**
     * 手动添加需要跟踪的文件路径
     * 
     * @param filePath 要跟踪的文件路径
     */
    public static void trackFile(Path filePath) {
        synchronized (temporaryFiles) {
            if (!temporaryFiles.contains(filePath)) {
                temporaryFiles.add(filePath);
            }
        }
    }


}
