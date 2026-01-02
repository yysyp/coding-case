package pslab;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class MySocketClient {

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> argMap = argsToMap(args);
        String host = argMap.get("--host").get(0);
        int port = Integer.parseInt(argMap.get("--port").get(0));

        Socket client = new Socket(host, port);
        //client.setSendBufferSize(60);
        client.setTcpNoDelay(false);

        try (OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                log.info("read from system.in {}", line);
                if (line != null) {
                    out.write(line.getBytes());
                }
                byte[] bytes = new byte[10240];
                int len = in.read(bytes);
                log.info("Msg from server: {}", new String(bytes));
            }
        }

    }

    public static Map<String, List<String>> argsToMap(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        String key = null;
        for (int i = 0, n = args.length; i < n; i++) {
            String arg = args[i] + "";
            if (arg.startsWith("-")) {
                key = arg;
            } else {
                List<String> val = map.get(key);
                if (val == null) {
                    val = new ArrayList<>();
                }
                val.add(arg);
                map.put(key, val);
            }
        }
        return map;
    }

}
