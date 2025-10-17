package ps.demo.jpademo.common;

import static org.junit.jupiter.api.Assertions.*;

class LoggerFactoryToolTest {

    public static void main(String[] args) {
        LoggerFactoryTool.initLogger(LoggerFactoryToolTest.class, "log/testmylog.log").info("Hello World!");
    }

}