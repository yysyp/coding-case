package ps.demo.jpademo.test;

public class SetProxy {

    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "googleapis.xxx");
        System.setProperty("http.proxyPort", "3128");

        System.setProperty("https.proxyHost", "googleapis.xxx");
        System.setProperty("https.proxyPort", "3128");

        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "C:\\Users\\patrick\\xxx.json");

    }
}
