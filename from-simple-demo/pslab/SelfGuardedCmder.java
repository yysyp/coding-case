package pslab;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeUnit;

public class SelfGuardedCmder {

    private static File startBatFile = new File("sgc.bat");
    private static File argsFile = new File("sgc.args");
    private static String defaultCmd = "cmd /c if %time:~0,2% geq 23 (shutdown -s -t 120) else if %time:~0,2% leq 5 (shutdown -s -t 120) && exit";
    private static String workLockName = ".sgc-lock-wk";
    private static String backupLockName = ".sgc-lock-bk";
    private static String toExitIndicator = ".sgc-lock.exit";
    private static File logFile = new File("sgc.log");
    private static int heartbeat = 1000;

    private static int executeInterval = 60;
    private static String command = defaultCmd;
    private static boolean logFlag = false;

    public static void main(String[] args) throws IOException {
        log(getProcessId() + "> -------------------------------------------");
        if (args.length < 2 && !argsFile.exists()) {
            usageTip();
            initiateStartBat();
            return;
        }
        if (argsFile.exists()) {
            loadArgsFile();
        }
        if (args.length >= 2) {
            parseArgs(args);
        }
        initiateStartBat();

        log(getProcessId() + "> The restart bat is:" + startBatFile.getAbsolutePath());
        File workLockFile = new File(workLockName);
        Guard workGuard = new Guard(workLockFile);
        Guard backupGuard = new Guard(new File(backupLockName));
        log(getProcessId() + "> Begin to loop with executeInterval=["
                + executeInterval + "] command=[" + command + "] logFlag=[" + logFlag + "]");
        doLoop(workGuard, backupGuard);

    }

    private static void initiateStartBat() throws IOException {
        if (!startBatFile.exists()) {
            startBatFile.createNewFile();
            writeFileContent(startBatFile, "java pslab.SelfGuardedCmder", "UTF-8");
        }
    }

    private static void doLoop(Guard workGuard, Guard backupGuard) {
        int countDown = executeInterval;
        while (true) {
            try {
                if (checkToExit()) {
                    log(getProcessId() + "> To exit.");
                    if (workGuard.lockedByMe()) {
                        workGuard.unlock();
                    }
                    if (backupGuard.lockedByMe()) {
                        backupGuard.unlock();
                    }
                    System.exit(0);
                }
                if (workGuard.lockedByMe()) {
                    if (backupGuard.lockedByMe()) {
                        backupGuard.unlock();
                        Thread.sleep(heartbeat);
                    }
                    log(getProcessId() + "> Run cmd:[" + command + "]");
                    countDown--;
                    if (countDown == 0) {
                        exec(command);
                        countDown = executeInterval;
                    }
                    //check whether backup process is still alive
                    if (backupGuard.tryLock()) {
                        backupGuard.unlock();
                        //start backup process...
                        startBackup();
                    }
                } else {
                    if (workGuard.tryLock()) {
                        log(getProcessId() + "> Got work lock");
                        //start backup process...
                        startBackup();
                    } else if (!backupGuard.lockedByMe()) {
                        log(getProcessId() + "> Trying backup lock.");
                        backupGuard.tryLock();
                    }
                }
            } catch (Exception e) {
                log(e);
            }
            try {
                Thread.sleep(heartbeat);
            } catch (InterruptedException e) {
            }
        }
    }

    private static void startBackup() throws IOException, InterruptedException {
        log(getProcessId() + "> Backup down, starting backup with bat:" + startBatFile.getAbsolutePath());
        exec("\"" + startBatFile + "\"");
    }

    private static void usageTip() {
        writeFileContent(argsFile, "3" + System.lineSeparator() + command
                , "UTF-8");

        logFlag = true;
        StringBuffer usage = new StringBuffer("#Usage:");
        usage.append("java pslab.SelfGuardedCmder ");
        usage.append("executeIntervalSeconds ");
        usage.append("command ");
        usage.append("[logFlag]");
        usage.append(System.lineSeparator());
        usage.append("i.e. ");
        usage.append("java pslab.SelfGuardedCmder ");
        usage.append("3 \"cmd /c echo %time%>>echo-per3s.log\" 1");
        usage.append(System.lineSeparator());
        usage.append("Place \".sgc-lock.exit\" file to stop.");
        log(usage.toString());

    }

    public static void loadArgsFile() {
        String[] args = readFileContent(argsFile, "UTF-8").split(System.lineSeparator());
        parseArgs(args);
    }

    private static void parseArgs(String[] args) {
        executeInterval = Integer.parseInt(args[0]);
        command = args[1];
        if (args.length > 2 && !args[2].trim().equals("")) {
            logFlag = true;
        }
    }

    private static boolean checkToExit() {
        File toExitFile = new File(toExitIndicator);
        return toExitFile.exists();
    }

    private static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

    public static void log(Throwable e) {
        log(stackTraceToString(e));
    }

    public static void log(String str) {
        if (!logFlag) {
            return;
        }
        try {
            str += System.lineSeparator();
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(logFile, true));
            // Writing on output stream
            out.write(str);
            // Closing the connection
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileContent(File file, String encoding) {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            String content = "";
            StringBuilder sb = new StringBuilder();
            while ((content = bf.readLine()) != null) {
                sb.append(content);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            //e.printStackTrace();
            log(e.getMessage());
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static void writeFileContent(File file, String content, String encoding) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            bw.write(content + System.lineSeparator());
        } catch (IOException e) {
            //log(e);
            log(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static String getProcessId() {
        String processName = java.lang.management.ManagementFactory
                .getRuntimeMXBean().getName();
        return processName.substring(0, processName.indexOf('@'));
    }


    private static boolean exec(String cmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd);
        handleStream(process.getInputStream());
        handleStream(process.getErrorStream());
        return process.waitFor(1, TimeUnit.SECONDS);
    }

    private static void handleStream(InputStream in) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(in))) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        log(line);
                    }
                } catch (IOException e) {
                    log(e);
                }
            }
        }).start();
    }

    static class Guard {

        private File file;
        private RandomAccessFile randomAccessFile = null;
        private FileChannel fileChannel;
        private FileLock fileLock;

        public Guard(File file) {
            this.file = file;
        }

        public boolean tryLock() {
            try {
                if (!this.file.exists()) {
                    this.file.createNewFile();
                }
                randomAccessFile = new RandomAccessFile(file, "rw");
                fileChannel = randomAccessFile.getChannel();
                fileLock = fileChannel.tryLock();
                return fileLock != null;
            } catch (IOException e) {
                log(e);
                return false;
            }

        }

        public boolean lockedByMe() {
            return fileLock != null;
        }

        public boolean unlock() {
            if (!lockedByMe()) {
                return false;
            }
            try {
                fileLock.release();
                fileLock = null;
                fileChannel.close();
                randomAccessFile.close();
                file.delete();
                return true;
            } catch (IOException e) {
                log(e);
                return false;
            }
        }


    }

}
