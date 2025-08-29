package ps.demo.jpademo.test;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.Scanner;

public class JasyptTest2 {

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        System.out.print("Input your Jasypt pass:");
        Scanner in = new Scanner(System.in);
        String jasyptPass = in.nextLine();
        System.out.println("Your input JasyptPass=[" + jasyptPass + "]");

        encryptor.setPassword(jasyptPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());

        System.out.print("Input your PLAIN TEXT for encrypting:");
        String plainText = in.nextLine();
        System.out.println("Your input PLAIN TEXT=[" + plainText + "]");
        String encrypted = encryptor.encrypt(plainText);
        System.out.println("==>>ENC(" + encrypted + ")");

        System.out.print("Input your encrypted text for decrypting:");
        String encryptedText = in.nextLine();
        in.close();
        System.out.println("Your input encrypted TEXT=[" + encryptedText + "]");
        if (encryptedText.startsWith("ENC(")) {
            encryptedText = StringUtils.removeStart(encryptedText, "ENC(");
            encryptedText = StringUtils.removeEnd(encryptedText, ")");
        }
        String decrypted = encryptor.decrypt(encryptedText);
        System.out.println("==>>" + decrypted);

    }

}
