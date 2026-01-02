package ps.demo.jpademo.common;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

@Deprecated
public class CmdRunTool implements Runnable {

    private String command;
    private int exitCode;
    private Process process;
    Map<Integer, String> output = new LinkedHashMap<Integer, String>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, String> pEldest) {
            return size() > 1000;
        }
    };

    public CmdRunTool(String command) {
        this.command = command;
        this.exitCode = 0;
    }

    public boolean isAlive() {
        if (process == null) {
            return false;
        }
        return this.process.isAlive();
    }

    public void destroy() {
        this.process.destroy();
    }

    public int getExitCode() {
        if (isAlive()) {
            return this.exitCode;
        }
        return 0;
    }

    public Map<Integer, String> getOutput() {
        return this.output;
    }

    public String getOutputString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Integer, String> e : this.output.entrySet()) {
            sb.append(e.getValue());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public void run() {
        try {
            process = Runtime.getRuntime().exec(command);
            new RedirCmdStreamThread(process.getInputStream(), "INFO").start();
            new RedirCmdStreamThread(process.getErrorStream(), "ERR").start();
            exitCode = process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            //log.error(e.getMessage(), e);
        }
    }

    class RedirCmdStreamThread extends Thread {
        InputStream is;
        String printType;

        RedirCmdStreamThread(InputStream is, String printType) {
            this.is = is;
            this.printType = printType;
        }

        public void run() {
            try (
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);) {
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    output.put(i++, line);
                    System.out.println(printType + ">" + line);
                    //log.info(printType + ">" + line);
                }

            } catch (IOException e) {
                e.printStackTrace();
                //log.error(e.getMessage(), e);
            }
        }
    }

}
