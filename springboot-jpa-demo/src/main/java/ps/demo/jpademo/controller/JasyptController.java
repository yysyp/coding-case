package ps.demo.jpademo.controller;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ps.demo.jpademo.dto.BookDto;

import java.util.List;
import java.util.Scanner;

@Slf4j
@RestController
@RequestMapping("/api/v1/jasypt")
public class JasyptController {

    @Value("${jasypt.encryptor.password}")
    private String jasyptEncrytorPass;

    @Value("${encpwdtest.test1}")
    private String testValue;

    @GetMapping("/encrypt")
    String encrypt(@RequestParam String text) {
        log.info("---testValue: {}", testValue);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptEncrytorPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        return encryptor.encrypt(text);
    }

    @GetMapping("/decrypt")
    String decrypt(@RequestParam String text) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(jasyptEncrytorPass);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
        if (text.startsWith("ENC(")) {
            text = StringUtils.removeStart(text, "ENC(");
            text = StringUtils.removeEnd(text, ")");
        }
        return encryptor.decrypt(text);
    }

}
