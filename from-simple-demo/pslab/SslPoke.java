package pslab;

import ps.demo.util.MyFileUtil;
import ps.demo.util.MyKeyStoreUtil;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SslPoke {
    private static javax.net.ssl.SSLSocketFactory getFactorySimple() throws Exception {
//        SSLContext context = SSLContext.getInstance("TLSv1");
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        tmf.init(MyKeyStoreUtil.getKeyStore(MyFileUtil.getFileInHomeDir("yysyp.cer"), "yysyp"));
//        context.init(null, tmf.getTrustManagers(), null);
//        context.init(null, null, null);

        SSLContext context = SSLContext.getInstance("TLS");
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }};
        context.init(null, trustAllCerts, new SecureRandom());

        return context.getSocketFactory();

    }

    public static void main(String[] args) {
        //System.getProperties().setProperty("javax.net.debug", "ssl");
        //System.getProperties().setProperty("https.cipherSuites", "TLS_RSA_WITH_AES_256_CBC_SHA");

        try {
            String urlStr = "https://localhost:6666";
            //String urlStr = "https://www.baidu.com";//
            URL url = new URL(urlStr);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            javax.net.ssl.SSLSocketFactory sslSocketFactory = getFactorySimple();

            connection.setSSLSocketFactory(sslSocketFactory);

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            connection.setHostnameVerifier(allHostsValid);
            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

//            while (in.available() > 0) {
//                System.out.print(in.read());
//            }
            System.out.println("Successfully connected");
            br.close();
            in.close();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
