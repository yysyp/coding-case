package ps.demo.jpademo.common;

import com.alibaba.excel.util.StringUtils;
import io.jsonwebtoken.lang.Maps;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CmdRunTool2 {

    public final static String OUT = "out";
    public final static String ERR = "err";
    public final static String EXITCODE = "exitCode";

    public static String[] cmdLinesSplitToCmdList(String ... lines) {
        List<String> cmds = new ArrayList<String>();
        for (String line : lines) {
            if (line == null || StringUtils.isBlank(line)) {
                continue;
            }
            String[] a = line.split(" ");
            for (String la : a) {
                cmds.add(la);
            }
        }
        return cmds.toArray(new String[0]);
    }

    public static void printCmdResult(Map<String, List<String>> result, PrintStream psout) {
        psout.println("ExitCode: "+result.get(CmdRunTool2.EXITCODE));
        List<String> out = result.get(CmdRunTool2.OUT);
        for (int i = 0, n = out.size(); i < n; i++) {
            psout.println("<"+i+">: " + out.get(i));
        }
        List<String> err = result.get(CmdRunTool2.ERR);
        for (int i = 0, n = err.size(); i < n; i++) {
            psout.println("<"+i+">: " + err.get(i));
        }
    }

    public static void printCmdResult(Map<String, List<String>> result, Logger logger) {
        logger.info("ExitCode: "+result.get(CmdRunTool2.EXITCODE));
        List<String> out = result.get(CmdRunTool2.OUT);
        for (int i = 0, n = out.size(); i < n; i++) {
            logger.info("<"+i+">: " + out.get(i));
        }
        List<String> err = result.get(CmdRunTool2.ERR);
        for (int i = 0, n = err.size(); i < n; i++) {
            logger.info("<"+i+">: " + err.get(i));
        }
    }

    public static Map<String, List<String>> runCmds(File directory, Map<String, String> envs, String... commands) {
        return runCmds(null, directory, envs, commands);
    }
    public static Map<String, List<String>> runCmds(String charset, File directory, Map<String, String> envs, String... commands) {
        //String charset = "GBK";
        if (StringUtils.isBlank(charset)) {
            charset = "UTF-8";
        }
        Map<String, List<String>> outErr = new HashMap<>();
        List<String> out = new ArrayList<>();
        List<String> err = new ArrayList<>();
        List<String> exitCode = new ArrayList<>();
        outErr.put(OUT, out);
        outErr.put(ERR, err);
        outErr.put(EXITCODE, exitCode);
        try {
            ProcessBuilder pb = new ProcessBuilder(commands);

            Map<String, String> env = pb.environment();
            if (envs != null && !envs.isEmpty()) {
                env.putAll(envs);
            }

            pb.directory(directory);
            pb.redirectErrorStream(true);

            log.info("Command will run in {}", directory);

            Process process = pb.start();

            // Read output
            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), charset));
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), charset));

            String line;
            while ((line = outputReader.readLine()) != null) {
                out.add(line);
            }

            while ((line = errorReader.readLine()) != null) {
                err.add(line);
            }

            int code = process.waitFor();
            exitCode.add(code+"");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return outErr;
    }


    private static String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
    private static File dir = new File(".");

    public static void initBashExeAndRunAtDir(String bashExe, File commandRunAt) {
        bash = bashExe;
        dir = commandRunAt;
    }

    public static Map<String, List<String>> run(String cmd) {
        Map<String, List<String>> result = CmdRunTool2.runCmds(
                dir,
                Map.of(),
                bash,
                "-c",
                cmd
        );
        return result;
    }

    public static void main(String[] args) throws IOException {
        //File commandRunAt = new File(FileUtilTool.getUserHomeDir());
        File commandRunAt = new File(".");
        log.info("Command will run in {}", commandRunAt.getCanonicalPath());
        String[] cmdLines = {"D:\\app\\Git\\bin\\bash.exe", "-c", "date >> date.log"};
        Map<String, List<String>> result = CmdRunTool2.runCmds(
                commandRunAt,
                Maps.of("MY_VAR", "value456").build(),
                cmdLines
        );
        CmdRunTool2.printCmdResult(result, System.out);

        log.info("Try another equivalent way...");
        CmdRunTool2.initBashExeAndRunAtDir("D:\\app\\Git\\bin\\bash.exe", new File("."));
        Map<String, List<String>> result2 = CmdRunTool2.run("date >> date.log");
        CmdRunTool2.printCmdResult(result2, log);
    }


}
