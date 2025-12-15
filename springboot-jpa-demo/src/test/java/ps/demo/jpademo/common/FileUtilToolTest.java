package ps.demo.jpademo.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilToolTest {

    @Test
    void toValidFileName() {
    }

    @Test
    void getUserHomeDir() {
        System.out.println(FileUtilTool.getUserHomeDir());
    }

    @Test
    void getCurrentDir() {
        System.out.println("Current Project Dir:"+FileUtilTool.getCurrentDir());
    }

    @Test
    void getFileInHomeDir() {
        System.out.println(FileUtilTool.getFileInHomeDir("test.txt").getAbsolutePath());
    }

    @Test
    void getAppPath() {
        System.out.println(FileUtilTool.getAppPath());
    }
}