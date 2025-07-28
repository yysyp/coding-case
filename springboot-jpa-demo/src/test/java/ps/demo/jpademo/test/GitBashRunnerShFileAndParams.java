package ps.demo.jpademo.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class GitBashRunnerShFileAndParams {
    public static void main(String[] args) throws IOException {
        //"springboot-jpa-demo/src/main/resources/ignore
//        String scriptPath = new File("springboot-jpa-demo/src/main/resources/ignore/myscript.sh").getAbsolutePath()
//                .replaceAll("\\\\", "/");
          String scriptPath = "./myscript.sh";
//        String param1 = "value1";
//        String param2 = "value2";
//        String commandLine = String.format("sh %s %s %s", scriptPath, param1, param2);

        //String commandLine = "C:/Users/p/Documents/GitHub/coding-case/springboot-jpa-demo/src/main/resources/ignore/myscript.sh";
        System.out.println("scriptPath: "+ scriptPath);
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\Git\\bin\\bash.exe",
                    "-c",
                    scriptPath);

            Map<String, String> env = pb.environment();
            env.put("MY_VAR", "value123");

            pb.directory(new File("springboot-jpa-demo/src/main/resources/ignore"));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output
            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String line;
            System.out.println(">>Script output:");
            while ((line = outputReader.readLine()) != null) {
                System.out.println(">"+line);
            }

            System.out.println("\n%%Error output:");
            while ((line = errorReader.readLine()) != null) {
                System.err.println("%"+line);
            }

            int exitCode = process.waitFor();
            System.out.println("\n##Exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
