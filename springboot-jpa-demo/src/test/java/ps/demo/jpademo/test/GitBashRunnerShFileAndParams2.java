package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import ps.demo.commonlibx.common.CmdRunTool2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitBashRunnerShFileAndParams2 {

    public static void main(String[] args) throws IOException {

        Map<String, List<String>> result = CmdRunTool2.runCmds(new File("springboot-jpa-demo/src/main/resources/ignore"),
                Maps.of("MY_VAR", "value456").build(),
                "C:\\Program Files\\Git\\bin\\bash.exe",
                "-c",
                "./myscript.sh");

        printCmdResult(result);

        Map<String, List<String>> result2 = CmdRunTool2.runCmds("GBK", new File("springboot-jpa-demo/src/main/resources/ignore"),
                new HashMap<>(),
                "cmd.exe",
                "/c",
                "ipconfig /all");

        printCmdResult(result2);

        //Linux processBuilder.command("ls", "-al");
        Map<String, List<String>> result3 = CmdRunTool2.runCmds("GBK", new File("springboot-jpa-demo/src/main/resources/ignore"),
                new HashMap<>(),
                "C:\\Program Files\\Git\\bin\\bash.exe",
                "ls",
                "-al");

        printCmdResult(result3);

    }

    public static void printCmdResult(Map<String, List<String>> result) {
        System.out.println("ExitCode: "+result.get(CmdRunTool2.EXITCODE));
        List<String> out = result.get(CmdRunTool2.OUT);
        for (int i = 0, n = out.size(); i < n; i++) {
            System.out.println("<"+i+">: " + out.get(i));
        }
        List<String> err = result.get(CmdRunTool2.ERR);
        for (int i = 0, n = err.size(); i < n; i++) {
            System.out.println("<"+i+">: " + err.get(i));
        }
    }
}
