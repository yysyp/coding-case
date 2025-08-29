package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import ps.demo.commonlibx.common.CmdRunTool2;
import ps.demo.commonlibx.common.RegexTool;
import ps.demo.commonlibx.common.StringXTool;

import java.io.File;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GitBashRunnerShK8sRecreator {

    public static void main(String[] args) throws IOException {

        String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        File dir = new File("springboot-jpa-demo/src/main/resources/ignore");
        String ns = "ns1";
        String type = "secret";
        String key = "sec123";
        String id = getFirstResourceId(bash, dir, "kubectl -n "+ns+" get "+type+" | grep "+ key);
        System.out.println("Found Id = " + id );
        recreateK8sObj(bash, dir, ns, type, id);

    }

    public static String getFirstResourceId(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        System.out.println("Run: " + command);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap.get(CmdRunTool2.OUT).get(0).split(" ")[0].trim();
    }

    public static void recreateK8sObj(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS get "+type+" "+id+" -o yaml > ./"+id+".yaml";
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);

        cmd = "kubectl -n $NS delete "+type+" "+id;
        rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);

        cmd = "kubectl -n $NS apply -f ./"+id+".yaml";
        rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
    }

}
