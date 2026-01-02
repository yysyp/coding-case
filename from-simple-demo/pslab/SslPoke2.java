package pslab;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SslPoke2 {
    private static javax.net.ssl.SSLSocketFactory getFactorySimple() throws Exception {
//        SSLContext context = SSLContext.getInstance("TLSv1");
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        tmf.init(MyKeyStoreUtil.getKeyStore(MyFileUtil.getFileInHomeDir("yysyp.cer"), "yysyp"));
//        context.init(null, tmf.getTrustManagers(), null);
//        context.init(null, null, null);

        SSLContext context = SSLContext.getInstance("TLS");
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        context.init(null, trustAllCerts, new SecureRandom());

        return context.getSocketFactory();

    }

    public static void main(String[] args) {
        //System.getProperties().setProperty("javax.net.debug", "ssl");
        //System.getProperties().setProperty("https.cipherSuites", "TLS_RSA_WITH_AES_256_CBC_SHA");

        try {
            javax.net.ssl.SSLSocketFactory sslSocketFactory = getFactorySimple();
            final SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 8443);
            System.out.println("--->>Connected: "+socket.isConnected());
            socket.startHandshake();
            socket.getOutputStream().write("GET / HTTP/1.1\r\nHost: www.baidu.com\r\n\r\n".getBytes());

            InputStream in = socket.getInputStream();
            while (in.available() > 0) {
                System.out.print(in.read());
            }

//            Thread tw = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String getReq = "GET / HTTP/1.1\n" +
//                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
//                            "Accept-Encoding: gzip, deflate, br\n" +
//                            "Accept-Language: en-US,en;q=0.9\n" +
//                            "Cache-Control: max-age=0\n" +
//                            "Connection: keep-alive\n" +
//                            "Cookie: Hm_lvt_3d8e7fc0de8a2a75f2ca3bfe128e6391=1650251768,1650252107,1650253499,1650601485; jenkins-timestamper-offset=-28800000; JSESSIONID=F5EFFBD8760AD216750B450023CF648E\n" +
//                            "Host: localhost:8443\n" +
//                            "Sec-Fetch-Dest: document\n" +
//                            "Sec-Fetch-Mode: navigate\n" +
//                            "Sec-Fetch-Site: none\n" +
//                            "Sec-Fetch-User: ?1\n" +
//                            "Upgrade-Insecure-Requests: 1\n" +
//                            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36\n" +
//                            "sec-ch-ua: \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"\n" +
//                            "sec-ch-ua-mobile: ?0\n" +
//                            "sec-ch-ua-platform: \"Windows\"";
//                    try (OutputStream out = socket.getOutputStream();
//                         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));) {
//                        bw.write(getReq);
//                        bw.flush();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            tw.start();
//
//            tw.join();
//
//            Thread tr = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try (InputStream in = socket.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
//
//                        String line = null;
//                        while ((line = br.readLine()) != null) {
//                            System.out.println("--->>line:" + line);
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            tr.start();
//
//            tr.join();

            System.out.println("Successfully connected");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
