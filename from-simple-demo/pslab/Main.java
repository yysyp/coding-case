package pslab;

import lombok.extern.slf4j.Slf4j;
import ps.demo.dto.AddrInfo;
import ps.demo.util.*;

import java.io.File;
import java.util.*;

/**
 * This is the jar main entry.
 * Run mvn to build: mvn clean package -D spring-boot.repackage.skip=true -D fat.jar=true
 * java -jar target/simple-demo-1.0.0-jar-with-dependencies.jar
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        File appDir = MyFileUtil.getAppPath();
        log.info("Main app dir={}", appDir);
        //File mainLog = MyFileUtil.getFileInAppPath("main.log");
        //log.info("Main log={}", mainLog);
        Map<String, List<String>> argsMap = MyArgsUtil.argsToMap(args);
        if (argsMap.containsKey("--help") || argsMap.containsKey("-h")) {
            printUsage();
            return;
        }

        Runtime r = Runtime.getRuntime();
        r.addShutdownHook(new Thread() {
            public void run() {
                log.info("shutDown hook");

            }
        });

        if (argsMap.containsKey("-at") && (argsMap.containsKey("-c"))
                || argsMap.containsKey("-shutdown")) {
            atCommand(argsMap);
        }

    }

    private static void atCommand(Map<String, List<String>> argsMap) {
        String command = "";
        if (argsMap.containsKey("-shutdown")) {
            command = "shutdown -s -t ";
        } else {
            command = argsMap.get("-c").get(0);
        }
        final String cmd = command;
        String atHhmm = argsMap.get("-at").get(0);
        String beforeHhmm = "05:00";

        Timer timer = new Timer();
        long sleepms = 5 * 60 * 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String nowHhmm = MyTimeUtil.getNowStr("HH:mm");
                Date atDate = MyTimeUtil.toDate(atHhmm, "HH:mm");
                Date beforeDate = MyTimeUtil.toDate(beforeHhmm, "HH:mm");
                Date nowDate = MyTimeUtil.toDate(nowHhmm, "HH:mm");
                int wait = 30 - (int) (2 * MyTimeUtil.subtractMinutes(nowDate, atDate));
                wait = Math.max(Math.min(wait, 30), 0);
                if (nowDate.before(beforeDate)) {
                    wait = 0;
                }
                if (nowDate.after(atDate) || nowDate.before(beforeDate)) {
                    timer.cancel();
                    MyCmdRun myCmdRun = new MyCmdRun(cmd + wait);
                    myCmdRun.run();
                }
            }
        }, 0, sleepms);
    }

    public static void printUsage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The usage:" + System.lineSeparator());
        stringBuilder.append("'-at HH:mm -c cmd' to execute command at HH:mm. ex: -at 23:00 -c \"shutdown -s -t 30\" or -shutdown");
        System.out.println(stringBuilder);
    }

}
