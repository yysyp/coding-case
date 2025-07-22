package ps.demo.copy;

import com.hubspot.jinjava.Jinjava;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class CopyTool {


    public static void main(String[] args) throws IOException {

        // Try jinjava
        String jinTemplate = """
                Hello {{name}}
                """;
        Jinjava jinjava = new Jinjava();
        String renderString = jinjava.render(jinTemplate, Map.of("name", "Patrick"));
        System.out.println("Render string = " + renderString);

        // Project template copy to generate a new project.
        String templateProjectName = "springboot-jpa-demo";
        String templatePackageName = "ps.demo.jpademo";
        String input = readFromConsole("Please enter new project name and new package name, (eg: default-new-proj ps.demo.newprj) : ");
        String[] a = input.trim().split("\\s+");
        if (input.trim().equals("") || a.length != 2) {
            System.out.println("Aborted");
            System.exit(0);
        }
        String newProjectName = a[0].trim();
        String newPackageName = a[1].trim();

        System.out.println("\n---------------------------------------------");
        System.out.println("New Project Name : [" + newProjectName + "]");
        System.out.println("New Package Name : [" + newPackageName + "]");
        System.out.println("---------------------------------------------");


        Path sourcePath = Path.of(templateProjectName);
        Path targetPath = Path.of(newProjectName);

        Map<String, String> replacementMap = Map.of(templateProjectName, newProjectName,
                templatePackageName, newPackageName,
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
            Files.copy(file, targetFile);
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
