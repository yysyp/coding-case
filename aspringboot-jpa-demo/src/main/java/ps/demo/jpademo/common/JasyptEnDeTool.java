package ps.demo.jpademo.common;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JasyptEnDeTool {

    public static String encrypt(String jasyptPass, String plainText) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        String encrypted = encryptor.encrypt(plainText);
        return "ENC(" + encrypted + ")";
    }



    public static String decrypt(String jasyptPass, String encryptedText) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        if (encryptedText.startsWith("ENC(")) {
            encryptedText = StringUtils.removeStart(encryptedText, "ENC(");
            encryptedText = StringUtils.removeEnd(encryptedText, ")");
        }
        return encryptor.decrypt(encryptedText);
    }

    public static String base64AndEncrypt(String jasyptPass, byte[] plainBytes) {
        String plainText = new String(Base64.getEncoder().encode(plainBytes), StandardCharsets.UTF_8);
        return encrypt(jasyptPass, plainText);
    }

    public static byte[] decryptAndDeBase64(String jasyptPass, String encryptedText) {
        String plainText = decrypt(jasyptPass, encryptedText);
        return Base64.getDecoder().decode(plainText.getBytes(StandardCharsets.UTF_8));
    }

}
