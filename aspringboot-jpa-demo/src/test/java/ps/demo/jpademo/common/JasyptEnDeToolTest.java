package ps.demo.jpademo.common;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JasyptEnDeToolTest {

    @Test
    void encrypt() {
        String encrypted = JasyptEnDeTool.encrypt("123456", "hello world1!");
        System.out.println(encrypted);
        String textBt = "我爱我的家乡I love my beautiful hometown !!!";
        String baeStr = JasyptEnDeTool.base64AndEncrypt("123456", textBt.getBytes(StandardCharsets.UTF_8));
        System.out.println("Encrpted:" + baeStr);
        String decryptStr = new String(JasyptEnDeTool.decryptAndDeBase64("123456", baeStr), StandardCharsets.UTF_8);
        System.out.println("DecryptStr:" + decryptStr);
        assertTrue(textBt.equals(decryptStr));

    }


    @Test
    void decrypt() {
        String decrypted = JasyptEnDeTool.decrypt("123456", "ENC(p+vvVFdxcFk6xyLiac1Q5owC1Jk4MbgR0LSPObQPox6Q4Cf37g678mUT/wuw63jW)");
        System.out.println(decrypted);
    }
}