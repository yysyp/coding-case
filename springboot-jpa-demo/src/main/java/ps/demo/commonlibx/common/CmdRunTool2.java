package ps.demo.commonlibx.common;

import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
public class CmdRunTool2 {

    public final static String OUT = "out";
    public final static String ERR = "err";
    public final static String EXITCODE = "exitCode";


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

}
