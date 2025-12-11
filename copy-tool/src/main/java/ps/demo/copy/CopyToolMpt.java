package ps.demo.copy;

import com.hubspot.jinjava.Jinjava;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class CopyToolMpt {


    //Run: ps.demo.copy.CopyTool my-test-pg ps.demo.pg
    public static void main(String[] args) throws IOException {

        // Try jinjava
        String jinTemplate = """
                Hello {{name}}
                """;
        Jinjava jinjava = new Jinjava();
        String renderString = jinjava.render(jinTemplate, Map.of("name", "Patrick"));
        System.out.println("Render string = " + renderString);

        // Project template copy to generate a new project.
        String templateProjectName = "quick-poc-mpt";
        String templatePackageName = "com.poc.mpt";
        String randomNameStr = RandomStringUtils.randomAlphabetic(5);
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String input = readFromConsole("Please enter new project name and new package name, (eg: poc-new-proj com.example.newprj) : ");

//        if (input.trim().equals("")) {
//            System.out.println("Aborted");
//            System.exit(0);
//        }
        if (input.trim().equals("")) {
            input = "poc-" + randomNameStr + "-" + nowStr + " com.poc." + randomNameStr;
        }
        String[] a = input.trim().split("\\s+");
        String newProjectName = a[0].trim();
        String newPackageName = a[1].trim();

        System.out.println("\n---------------------------------------------");
        System.out.println("New Project Name : [" + newProjectName + "]");
        System.out.println("New Package Name : [" + newPackageName + "]");
        System.out.println("---------------------------------------------");


        Path sourcePath = Path.of(templateProjectName);
        Path targetPath = Path.of(newProjectName);

        String regularNewAppName = StringUtils.capitalize(
                newProjectName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase()
        );

        Map<String, String> replacementMap = Map.of(templateProjectName, newProjectName,
                templatePackageName, newPackageName,
                "PocDemoApplication", regularNewAppName,
                templatePackageName.replace(".", "\\"), newPackageName.replace(".", "\\"),
                "0001", 1000 + new Random().nextInt(9999 - 1000) + "");


        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }
        System.out.println("Copy files begin...");
        Files.walkFileTree(sourcePath, new CopyFileVisitor(targetPath, replacementMap));
        System.out.println("Copy files end.");
    }

    public static String readFromConsole(String prompt) {
        System.out.print(prompt);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            return br.readLine();
        } catch (Exception e) {
            System.err.println("Reading error: "+e.getMessage());
        }
        return "";
    }

    public static class CopyFileVisitor extends SimpleFileVisitor<Path> {
        private final Path targetPath;
        private Path sourcePath = null;
        private Map<String, String> replacementMap;

        public CopyFileVisitor(Path targetPath, Map<String, String> replacementMap) {
            this.targetPath = targetPath;
            this.replacementMap = replacementMap;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (sourcePath == null) {
                sourcePath = dir;
            } else {
                Path newDir = Path.of(replaceStrings(dir.toString()));
                Files.createDirectories(newDir);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String fileName = file.toFile().getName();
            if (fileName.endsWith(".class") || fileName.endsWith(".jar")
                    || fileName.endsWith(".jar.original")) {
                return FileVisitResult.CONTINUE;
            }
            System.out.println("Visit file " + file);

            Path newFile = Path.of(replaceStrings(file.toString()));
            Path targetFile = targetPath.resolve(sourcePath.relativize(newFile));
            //Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
            fileContentReplace(targetFile);
            return FileVisitResult.CONTINUE;
        }

        private String replaceStrings(String origStr) {
            for (Map.Entry<String, String> entry : replacementMap.entrySet()) {
                origStr = origStr.replace(entry.getKey(), entry.getValue());
            }
            return origStr;
        }

        private void fileContentReplace(Path path) throws IOException {
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), charset);
            content = replaceStrings(content);
            Files.write(path, content.getBytes(charset));
        }

    }

}
