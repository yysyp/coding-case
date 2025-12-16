package ps.demo.zglj.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ps.demo.zglj.common.RestTemplateTool;


import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class FileDownloadClient {

    public static void main(String[] args) throws IOException {

        String url = "xxx";
        String fileName = "";
        RestTemplate restTemplate = RestTemplateTool.getRestTemplate();
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            byte[] fileContent = responseEntity.getBody();
            if (fileContent != null) {
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    for (int i = 0; i < fileContent.length; i++) {
                        fos.write(fileContent[i]);
                    }
                }
            }
        }

        log.info("Done!");

    }
}
