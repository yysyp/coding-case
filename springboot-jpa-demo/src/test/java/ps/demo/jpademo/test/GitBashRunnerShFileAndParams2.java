package ps.demo.jpademo.test;

import io.jsonwebtoken.lang.Maps;
import ps.demo.commonlibx.common.CmdRunTool2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class GitBashRunnerShFileAndParams2 {

    public static void main(String[] args) throws IOException {

        Map result = CmdRunTool2.runCmds(new File("springboot-jpa-demo/src/main/resources/ignore"),
                Maps.of("MY_VAR", "value456").build(),
                "C:\\Program Files\\Git\\bin\\bash.exe",
                "-c",
                "./myscript.sh");

        System.out.println(result);
    }
}
