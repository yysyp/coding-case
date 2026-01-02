package ps.demo.jpademo.common;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TemporaryFileManagerTest {

    public static void main(String[] args) throws IOException {
        // 创建临时文件
        Path tempFile = TemporaryFileManager.createTempFile("myapp-", ".tmp");
        System.out.println("Created temporary file: " + tempFile);

        // 创建临时目录
        Path tempDir = TemporaryFileManager.createTempDirectory("myapp-temp-");
        System.out.println("Created temporary directory: " + tempDir);

    }

}