package ps.demo.jpademo.common;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LoggerFactoryTool {

    public static org.slf4j.Logger initLogger(Class clazz, String filePath) {
        String loggerName = clazz.getName();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(loggerName);
        logger.detachAndStopAllAppenders();

        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(encoder);
        consoleAppender.setName(loggerName + "-console");
        consoleAppender.start();

        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setContext(context);
        appender.setName(loggerName + "-file");
        appender.setFile(filePath);
        appender.setEncoder(encoder);
        appender.setAppend(true);
        appender.start();

        logger.addAppender(consoleAppender);
        logger.addAppender(appender);
        logger.setAdditive(false);
        return logger;

    }

}
