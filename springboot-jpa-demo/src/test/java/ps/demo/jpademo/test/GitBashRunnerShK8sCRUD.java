package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import ps.demo.jpademo.common.CmdRunTool2;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitBashRunnerShK8sCRUD {

    public static void main(String[] args) throws IOException {

        String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        File dir = new File("springboot-jpa-demo/src/main/resources/ignore");
        String ns = "ns1";
        String type = "secret";
        String key = "sec123";
        List<String> ids = getFirstResourceIds(bash, dir, "kubectl -n "+ns+" get "+type+" | grep "+ key);
        System.out.println("Found Ids = " + ids );
        for (String id : ids) {
            describe(bash, dir, ns, type, id);
        }

    }

    public static List<String> getFirstResourceIds(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        System.out.println("Run: " + command);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        List<String> lines = rstmap.get(CmdRunTool2.OUT);
        List<String> ids = new ArrayList<>();
        for (String line : lines) {
            ids.add(line.split(" ")[0].trim());
        }
        return ids;
    }


    public static Map<String, List<String>> get(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS get "+type+" "+id+" -o yaml > ./ignore/"+id+".yaml";
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap;
    }

    public static Map<String, List<String>> describe(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS describe "+type+" "+id;
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap;
    }

    public static Map<String, List<String>> delete(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS delete "+type+" "+id;
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap;
    }

    public static Map<String, List<String>> create(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS apply -f ./ignore/"+id+".yaml";
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap;
    }


}
