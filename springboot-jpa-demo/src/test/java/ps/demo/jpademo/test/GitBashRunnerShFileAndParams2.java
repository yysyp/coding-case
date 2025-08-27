package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import ps.demo.commonlibx.common.CmdRunTool2;
import ps.demo.commonlibx.common.RegexTool;
import ps.demo.commonlibx.common.StringXTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitBashRunnerShFileAndParams2 {

    public static void main(String[] args) throws IOException {

        System.out.println("RUN command: C:\\Program Files\\Git\\bin\\bash.exe -c ./myscript.sh");
        Map<String, List<String>> result = CmdRunTool2.runCmds(new File("springboot-jpa-demo/src/main/resources/ignore"),
                Maps.of("MY_VAR", "value456").build(),
                "C:\\Program Files\\Git\\bin\\bash.exe",
                "-c",
                "./myscript.sh");

        printCmdResult(result);

        System.out.println("RUN command: cmd.exe /c ipconfig /all");
        Map<String, List<String>> result2 = CmdRunTool2.runCmds("GBK", new File("springboot-jpa-demo/src/main/resources/ignore"),
                new HashMap<>(),
                "cmd.exe",
                "/c",
                "ipconfig /all");

        printCmdResult(result2);

        String command = "ipconfig /all";
        System.out.println("RUN command: "+ command);
        Map<String, List<String>> result22 = CmdRunTool2.runCmds("GBK", new File("springboot-jpa-demo/src/main/resources/ignore"),
                new HashMap<>(),
                command.split(" "));

        printCmdResult(result22);

        String oneString = StringXTool.listToOneString(result22.get(CmdRunTool2.OUT));
        System.out.println("oneString = " + oneString);
        String found0 = RegexTool.findByRegex(oneString, "^.*IPv4.*(\\b(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b).*$", 0);
        String found1 = RegexTool.findByRegex(oneString, "^.*IPv4.*(\\b(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b).*$", 1);
        System.out.println("found0 = " + found0 + "\t found1 = " + found1);
        System.out.println("found2 = " + findIPAddresses(oneString));


        command = "C:\\Program Files\\Git\\bin\\bash.exe -c ls -al";
        System.out.println("RUN command: "+ command);
        Map<String, List<String>> result3 = CmdRunTool2.runCmds("GBK", new File("springboot-jpa-demo/src/main/resources/ignore"),
                new HashMap<>(),
                command.split(" "));

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

    // 严格的 IPv4 正则表达式
    private static final String IPV4_PATTERN =
            "\\b(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b";

    // 简单的 IPv6 正则表达式
    private static final String IPV6_PATTERN =
            "\\b(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\b";

    // 组合模式：匹配 IPv4 或 IPv6
    private static final String IP_PATTERN =
            IPV4_PATTERN + "|" + IPV6_PATTERN;

    /**
     * 从文本中提取所有 IP 地址
     */
    public static List<String> findIPAddresses(String text) {
        List<String> ipAddresses = new ArrayList<>();
        Pattern pattern = Pattern.compile(IP_PATTERN);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            ipAddresses.add(matcher.group());
        }

        return ipAddresses;
    }
}
