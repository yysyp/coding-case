package ps.demo.jpademo.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadWriteToolTest {

    @Test
    void readFileInClassPath() {
        String fileName = "application.yml";
        String content = ReadWriteTool.readFileInClassPath(fileName);
        System.out.println(content);
    }

//    @Test
//    void writeFileToClassPath() {
//        String fileName = "application.yml1";
//        String content = "test";
//        ReadWriteTool.writeFileToClassPath(fileName, content);
//    }
}