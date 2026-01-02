package pslab;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MySocketService {

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> argMap = argsToMap(args);
        List<SocketChannel> clients = new ArrayList<>();

        int port = Integer.parseInt(argMap.get("--port").get(0));
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));

        ssc.configureBlocking(false);

        while (true) {
            SocketChannel client = ssc.accept();
            if (client == null) {
                //log.info("ssc.accept() null");
            } else {
                clients.add(client);
                client.configureBlocking(false);
                InetAddress clientAddress = client.socket().getInetAddress();
                int clientPort = client.socket().getPort();
                log.info("New client connection, client ip:port={}:{}", clientAddress.getHostAddress(), clientPort);
            }
            ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
            for (SocketChannel c : clients) {
                int num = c.read(buffer);
                if (num > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String msg = new String(bytes);
                    log.info("Client{}:{}, txt:{}", c.socket().getInetAddress().getHostAddress(),
                            c.socket().getPort(), msg);

                    String response = null;
                    if ("hello".equals(msg)) {
                        long ms = System.currentTimeMillis();
                        response = "hello at:" + ms;

                    } else {
                        response = "NULL";
                    }
                    ByteBuffer resBuf = ByteBuffer.allocateDirect(10240);
                    resBuf.put(response.getBytes(StandardCharsets.UTF_8));
                    resBuf.flip();
                    c.write(resBuf);
                }
                buffer.clear();
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
