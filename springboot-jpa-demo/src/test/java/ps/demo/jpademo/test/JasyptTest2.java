package ps.demo.jpademo.test;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.util.Scanner;

public class JasyptTest2 {

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        System.out.print("Input your Jasypt pass:");
        Scanner in = new Scanner(System.in);
        String jasyptPass = in.nextLine();
        in.close();
        System.out.println("Your input JasyptPass=["+jasyptPass+"]");

        encryptor.setPassword(jasyptPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());

        String encrypted = encryptor.encrypt("Hello world ---== !");
        System.out.println("==>>ENC(" + encrypted + ")");
    }

}
