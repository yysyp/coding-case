package ps.demo.lqbs.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitBashRunner {
    public static void main(String[] args) {
        try {

            // Create ProcessBuilder for Git Bash
            ProcessBuilder pb = new ProcessBuilder(
                    "C:\\Program Files\\Git\\bin\\bash.exe",
                    "-c",
                    "echo 'Hello from Git Bash' && pwd");

            // Redirect error stream to output stream
            pb.redirectErrorStream(true);

            // Start the process
            Process process = pb.start();

            // Read the output
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}