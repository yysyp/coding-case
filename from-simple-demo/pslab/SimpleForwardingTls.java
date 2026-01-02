package pslab;

import lombok.extern.slf4j.Slf4j;
import ps.demo.util.MyArgsUtil;
import ps.demo.util.MyFileUtil;
import ps.demo.util.MyKeyStoreUtil;
import ps.demo.util.MyReadWriteUtil;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleForwardingTls {

    static int MAXCONN = 10;

    public static void main(String[] args) {
        runningCheck();

        Map<String, List<String>> argsMap = MyArgsUtil.argsToMap(args);
        if (argsMap.isEmpty() || argsMap.containsKey("-h") || argsMap.containsKey("--help")) {
            usage();
            System.exit(0);
        }

        String listenHost = argsMap.get("-listenHost").get(0);
        int listenPort = Integer.parseInt(argsMap.get("-listenPort").get(0));
        String remoteHost = argsMap.get("-remoteHost").get(0);
        int remotePort = Integer.parseInt(argsMap.get("-remotePort").get(0));
        //String cerFile = argsMap.get("-cerFile").get(0);
        String ksFile = argsMap.get("-ksFile").get(0);

        log.info("Listening on {}:{} and --> forwarding to {}:{}", listenHost, listenPort, remoteHost, remotePort);

        SSLServerSocket serverSocket = null;
        //ServerSocket serverSocket = null;
        ExecutorService service = Executors.newFixedThreadPool(MAXCONN);
        try {
            //System.getProperties().setProperty("javax.net.debug", "ssl");
            //System.getProperties().setProperty("https.cipherSuites", "TLS_RSA_WITH_AES_256_CBC_SHA");

            SSLContext sslContext = SSLContext.getInstance("TLS");
//            //KeyStore ks = MyKeyStoreUtil.getKeyStoreFromCer(new File(cerFile), "cer");
            KeyStore ks = MyKeyStoreUtil.getKeyStore(new File(ksFile), "changeit");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "changeit".toCharArray());
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(ks);
            TrustManager[] trustAllCerts = genTrustAllTM();
            sslContext.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());
            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(
                    listenPort, MAXCONN, InetAddress.getByName(listenHost));

            serverSocket.setUseClientMode(false);
            String[] pwdsuits = serverSocket.getSupportedCipherSuites();
            serverSocket.setEnabledCipherSuites(pwdsuits);
//            serverSocket = new ServerSocket(listenPort, MAXCONN, InetAddress.getByName(listenHost));

            while (true) {
                try {
                    Socket socket1 = serverSocket.accept();
                    log.info("Client connected, client={}:{} --> {}:{}", socket1.getInetAddress(), socket1.getPort(),
                            socket1.getLocalAddress(), socket1.getLocalPort());

//                    SSLContext sslContext2 = SSLContext.getInstance("TLS");//SSLContext.getInstance("TLSv1.2");
//                    KeyManagerFactory kmf2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//                    TrustManagerFactory tmf2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                    tmf2.init(ks);
//                    sslContext2.init(kmf2.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

                    SSLContext context = SSLContext.getInstance("TLS");
                    TrustManager[] trustAllCerts2 = genTrustAllTM();
                    context.init(null, trustAllCerts2, new SecureRandom());
                    SSLSocketFactory socketFactory = context.getSocketFactory();

//                    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
                    SSLSocket socket2 = (SSLSocket) socketFactory.createSocket(remoteHost, remotePort);
//                    socket2.setNeedClientAuth(false);
//                    String[] pwdsuits2 = socket2.getSupportedCipherSuites();
//                    socket2.setEnabledCipherSuites(pwdsuits2);

//                    Socket socket2 = new Socket(remoteHost, remotePort);

                    log.info("Connected to remote server, {}:{} --> {}:{}", socket2.getLocalAddress(), socket2.getLocalPort(),
                            socket2.getInetAddress(), socket2.getPort());
                    Thread proxy = new ConnProxy(socket1, socket2);
                    service.submit(proxy);

                    /* new ConnProxy(() -> {
                        Thread proxy = new ConnProxy(socket1, socket2);
                        proxy.start();
                        log.info("Waiting for proxy to end");
                        try {
                            proxy.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.info("Close proxy sockets");
                        MyReadWriteUtil.closeAll(socket1, socket2);
                    }); */
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("Main thread to close sockets.");
            MyReadWriteUtil.closeAll(serverSocket);
            service.shutdown();
        }

        log.info("End.");
    }

    private static TrustManager[] genTrustAllTM() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};
        return trustAllCerts;
    }

    private static void runningCheck() {
        File runningFile = MyFileUtil.getFileInAppPath("sf-running.txt");
        if (!runningFile.exists()) {
            try {
                runningFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!runningFile.exists()) {
                        System.exit(0);
                    }
                }
            }
        }).start();
    }

    public static void usage() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" -listenHost HOST");
        stringBuffer.append(" -listenPort PORT");
        stringBuffer.append(" -remoteHost HOST");
        stringBuffer.append(" -remotePort PORT");
        stringBuffer.append(" -ksFile xxx.keystore");

        log.info(stringBuffer.toString());
    }

    static class ConnProxy extends Thread {
        private SocketRelay relay1;
        private SocketRelay relay2;
        private CountDownLatch latch;

        public ConnProxy(Socket socket1, Socket socket2) {
            this.latch = new CountDownLatch(2);
            this.relay1 = new SocketRelay(socket1, socket2, latch);
            this.relay2 = new SocketRelay(socket2, socket1, latch);
        }

        @Override
        public void run() {
            this.relay1.start();
            this.relay2.start();
            log.info("Proxy latch awaiting");
            try {
                this.latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Proxy read write completed");
            MyReadWriteUtil.closeAll(relay1, relay2);
        }

    }

    static class SocketRelay extends Thread implements Closeable {
        private Socket from;
        private Socket to;
        private CountDownLatch latch;

        public SocketRelay(Socket from, Socket to, CountDownLatch latch) {
            this.from = from;
            this.to = to;
            this.latch = latch;
        }

        @Override
        public void run() {
            byte[] buf = new byte[1024];
            try (InputStream is = from.getInputStream();
                 OutputStream os = to.getOutputStream();) {
                while (!from.isClosed() && !to.isClosed()
                        && !from.isInputShutdown() && !to.isOutputShutdown()) {

                    int size = is.read(buf);
                    if (size > -1) {
                        os.write(buf, 0, size);
                        log.info("--->>data write: buf={}", new String(buf));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("Socket relay latch countDown");
            latch.countDown();
        }

        @Override
        public void close() throws IOException {
            MyReadWriteUtil.closeAll(to, from);
        }
    }


}
