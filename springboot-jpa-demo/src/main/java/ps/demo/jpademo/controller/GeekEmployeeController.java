package ps.demo.jpademo.controller;

import com.google.common.net.HttpHeaders;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ps.demo.jpademo.dto.GeekEmployee;

import java.io.*;

@Slf4j
@RestController
public class GeekEmployeeController {

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
