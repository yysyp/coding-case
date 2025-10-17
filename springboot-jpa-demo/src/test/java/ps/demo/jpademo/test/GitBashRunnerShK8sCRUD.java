package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import lombok.extern.slf4j.Slf4j;
import ps.demo.jpademo.common.CmdRunTool2;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GitBashRunnerShK8sCRUD {

    public static void main(String[] args) throws IOException {

        String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        File dir = new File("springboot-jpa-demo/src/main/resources/ignore");
        String ns = "ns1";
        String type = "secret";
        String key = "sec123";

        String latestId = getFirstResourceId(bash, dir, "kubectl -n "+ns+" get "+type+" --sort-by=.metadata.creationTimestamp | tac | grep "+ key);
        List<String> ids = getAllResourceIds(bash, dir, "kubectl -n "+ns+" get "+type+" | grep "+ key);
        log.info("Found Ids = " + ids );
        for (String id : ids) {
            describe(bash, dir, ns, type, id);
        }

    }

    public static String getFirstResourceId(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        log.info("Run: " + command);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap.get(CmdRunTool2.OUT).get(0).split(" ")[0].trim();
    }
    
    public static List<String> getAllResourceIds(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        log.info("Run: " + command);
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
        log.info("Run: " + cmd);
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
        //log.info("Run: " + cmd);
        //CmdRunTool2.printCmdResult(rstmap, System.out);
        log.info("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);
        return rstmap;
    }

    public static Map<String, List<String>> delete(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS delete "+type+" "+id;
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        log.info("Run: " + cmd);
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
        log.info("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, System.out);
        return rstmap;
    }


}
