package ps.demo.jpademo.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class StreamDownloadController {

    @GetMapping("/download1")
    public ResponseEntity<Void> downloadData(@RequestParam int pageSize, OutputStream responseStream) {
        String pageData1 = RandomUtil.randomString(1024);
        try {
            responseStream.write(pageData1.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=page.data")
                .contentType(MediaType.TEXT_PLAIN)
                .build();

    }

    @GetMapping("/download2")
    public ResponseEntity<StreamingResponseBody> downloadZip(@RequestParam int pageSize) {
        StreamingResponseBody responseBody = outputStream -> {

            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                zipOutputStream.putNextEntry(new ZipEntry("page.data"));

                zipOutputStream.write(RandomUtil.randomString(1024).getBytes());
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=page.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }


    @GetMapping("/download3")
    public ResponseEntity<StreamingResponseBody> downloadDataFile() {
        File file = new File("doc/lombok-plugin-install.png");
        StreamingResponseBody responseBody = outputStream -> {

            try (InputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int byteRead;
                while ((byteRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteRead);
                }
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=page.data")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(file.length())
                .body(responseBody);
    }


}
