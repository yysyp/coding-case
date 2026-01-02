package ps.demo.jpademo.common;

import io.jsonwebtoken.lang.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CmdRunK8sCRUD {

    private static String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
    private static File dir = new File(".");

    public static void initBashExeAndRunAtDir(String bashExe, File commandRunAt) {
        bash = bashExe;
        dir = commandRunAt;
    }

    /**
     * "kubectl -n $NS get "+type+" --sort-by='.lastTimestamp' | tac | grep " + key;
      * @param command
     * @return
     */
    public static String getFirstResourceId(String command) {
        return getFirstResourceId(bash, dir, command);
    }

    public static String getFirstResourceId(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        log.info("Run: " + command);
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
        return rstmap.get(CmdRunTool2.OUT).get(0).split(" ")[0].trim();
    }

    public static List<String> getAllResourceIds(String command) {
        return getAllResourceIds(bash, dir, command);
    }

    public static List<String> getAllResourceIds(String bash, File dir, String command) {
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, new HashMap<>(),
                bash,
                "-c",
                command);
        log.info("Run: " + command);
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
        List<String> lines = rstmap.get(CmdRunTool2.OUT);
        List<String> ids = new ArrayList<>();
        for (String line : lines) {
            ids.add(line.split(" ")[0].trim());
        }
        return ids;
    }


    public static Map<String, List<String>> execute(String bash, File dir, String ns, String cmd) {
        Map env = Maps.of("NS", ns).build();
        //String cmd = "kubectl -n $NS get "+type+" --sort-by='.lastTimestamp' | tac | grep " + key;
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        log.info("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
        return rstmap;
    }

    public static Map<String, List<String>> get(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS get "+type+" "+id+" -o yaml > ./ignore/"+id+".yaml";
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        log.info("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
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
        //CmdRunTool2.printCmdResult(rstmap, log);//System.out);
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
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
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
        CmdRunTool2.printCmdResult(rstmap, log);//System.out);
        return rstmap;
    }


    public static void reCreate(String bash, File dir, String ns, String type, String id) {
        Map env = Maps.of("NS", ns).build();
        String cmd = "kubectl -n $NS get "+type+" "+id+" -o yaml > ./ignore/"+id+".yaml";
        Map<String, List<String>> rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);//

        cmd = "kubectl -n $NS delete "+type+" "+id;
        rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);//

        cmd = "kubectl -n $NS apply -f ./ignore/"+id+".yaml";
        rstmap = CmdRunTool2.runCmds(dir, env,
                bash,
                "-c",
                cmd);
        System.out.println("Run: " + cmd);
        CmdRunTool2.printCmdResult(rstmap, log);//
    }


    public static void main(String[] args) throws IOException {
        String bash = "C:\\Program Files\\Git\\bin\\bash.exe";
        File dir = new File("springboot-jpa-demo/src/main/resources/ignore");
        initBashExeAndRunAtDir(bash, dir);
        String ns = "ns1";
//        String type = "secret";
        String type = "event";
        String key = "gateway-service";

        //String cmd = "kubectl -n $NS get "+type+" --sort-by='.lastTimestamp' | tac | grep " + key;
        execute(bash, dir, ns, "kubectl -n "+ns+" get "+type+" --sort-by='.lastTimestamp' | tac | grep " + key);

//        String latestId = getFirstResourceId(bash, dir, "kubectl -n "+ns+" get "+type+" --sort-by=.metadata.creationTimestamp | tac | grep "+ key);
//        List<String> ids = getAllResourceIds(bash, dir, "kubectl -n "+ns+" get "+type+" | grep "+ key);
//        log.info("Found Ids = " + ids );
//        for (String id : ids) {
//            describe(bash, dir, ns, type, id);
//        }

    }

}
