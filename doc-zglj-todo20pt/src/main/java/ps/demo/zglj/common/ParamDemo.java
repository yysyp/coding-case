package ps.demo.zglj.common;

public class ParamDemo {

    public static void main(String[] args) {
        /*
        1. Environment variable:
            - (WINDOWS CMD: set APP_MODE=test or set "APP_MODE=test")
            - (WINDOWS POWERSHELL: $env:APP_MODE="test")
            - (UNIX/MACOS: export APP_MODE=test)

        IDE: SPRING_PROFILES_ACTIVE=dev;APP_MODE=test
         */
        String appMode = System.getenv("APP_MODE");
        System.out.println("APP_MODE: " + appMode);

        /*
        2. JVM argument:
        - java -Dapp.version=1.8.0_181 -Dfeature=xyz -jar app.jar

        - Windows:
        mvn clean test "-DargLine=-Xmx3072m" -pl :xxx-auto-app "-Dspring.profiles.active=dev" "-Dapp.version=1.8.0_181" "-Dfeature=xyz"
        - Linux:
        mvn clean test -DargLine=-Xmx3072m -pl :xxx-auto-app -Dspring.profiles.active=dev -Dapp.version=1.8.0_181 -Dfeature=xyz

         */
        String appVersion = System.getProperty("app.version");
        System.out.println("app.version: " + appVersion);

        /*
        3. Program argument:
        - java ParamDemo input.txt 50 verbose

         */
        if (args.length >= 3) {
            String inputFile = args[0];
            int maxLines = Integer.parseInt(args[1]);
            boolean verbose = Boolean.parseBoolean(args[2]);
            System.out.println("inputFile: " + inputFile);
            System.out.println("maxLines: " + maxLines);
            System.out.println("verbose: " + verbose);
        } else {
            System.out.println("Usage: java ParamDemo <inputFile> <maxLines> <verbose>");
        }
    }

}
