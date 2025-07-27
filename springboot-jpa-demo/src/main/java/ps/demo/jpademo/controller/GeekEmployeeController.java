package ps.demo.jpademo.controller;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ps.demo.jpademo.dto.BookDto;
import ps.demo.jpademo.dto.GeekEmployee;

import java.io.*;
import java.util.*;

import java.security.Key;
import java.util.Date;

@Slf4j
@RestController
public class GeekEmployeeController {

    @PostMapping("/token")
    public ResponseEntity<Object> token() {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "12345");
        claims.put("role", "admin");
        claims.put("email", "user@example.com");
        Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // Build the token
        String token = Jwts.builder()
                .setClaims(claims)          // Custom claims
                .setSubject("user123")         // Subject (usually user ID)
                .setIssuedAt(now)           // Issued at
                .setExpiration(expiration)   // Expiration time
                .signWith(SECRET_KEY)       // Sign with secret key
                .compact();

        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }

    @GetMapping("/testtoken")
    public ResponseEntity<Object> testtoken(@RequestHeader(value = "Authorization") String authorization) {

        log.info("Authorization: {}", authorization);
        return new ResponseEntity<>(Map.of("token", token()), HttpStatus.OK);
    }

    @PostMapping("/geekemployees")
    public ResponseEntity<GeekEmployee> saveEmployeeData(
            @Valid @RequestBody GeekEmployee geekEmployee) {
        log.info("Geek employee geekEmployee:{}", geekEmployee);
        return new ResponseEntity<GeekEmployee>(
                geekEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> streamingDownloadFile() {
        File file = new File("C:\\Users\\yysyp\\Downloads\\chromium-win64.zip");
        StreamingResponseBody streamingResponseBody = outputStream -> {

            
            try (InputStream inputStream = new FileInputStream(file)) {
                byte[] buf = new byte[8192];
                int byteRead;
                while ((byteRead = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, byteRead);
                }
            }

        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.zip")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(file.length()) //optional
                .body(streamingResponseBody);

    }




}
