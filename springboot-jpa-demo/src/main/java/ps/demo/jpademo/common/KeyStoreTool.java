package ps.demo.jpademo.common;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class KeyStoreTool {

    public static List<X509Certificate> getRootDefaultCaCertificates() {
        List<X509Certificate> certificateList = new ArrayList<>();
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    for (X509Certificate cer : ((X509TrustManager) trustManager).getAcceptedIssuers()) {
                        certificateList.add(cer);
                    }
                }
            }
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return certificateList;
    }

    public static KeyStore createKeyStoreWithDefaultCa() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            //
            List<X509Certificate> certificateList = getRootDefaultCaCertificates();
            for (X509Certificate certificate : certificateList) {
                keyStore.setCertificateEntry(certificate.getSubjectDN().getName(), certificate);
            }
            return keyStore;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyStore getKeyStoreFromCer(File cerFile, String alias) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);//Make an empty store

        try (InputStream fis = new FileInputStream(cerFile);
             BufferedInputStream bis = new BufferedInputStream(fis);) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                trustStore.setCertificateEntry(alias + bis.available(), cert);
            }
        }
        return trustStore;
    }

    public static KeyStore getKeyStore(File ksFile, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream in = new FileInputStream(ksFile)) {
            if (password == null) {
                keystore.load(in, null);
            } else {
                keystore.load(in, password.toCharArray());
            }
        }
        return keystore;
    }

    public static byte[] trimPemToDer(byte[] pem) {
        String pemStr = new String(pem, StandardCharsets.UTF_8).trim();
        if (pemStr.startsWith("-----BEGIN") || pemStr.endsWith("-----")) {
            pemStr.replaceAll("-----BEGIN [^-]+-----", "")
                    .replaceAll("-----END [^-]+-----", "")
                    .replaceAll("\\s+", "");
            try {
                return Base64.getDecoder().decode(pemStr.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.out.println("pem error " + e.getMessage());
                return pemStr.getBytes(StandardCharsets.UTF_8);
            }
        } else {
            return pem;
        }
    }


}
