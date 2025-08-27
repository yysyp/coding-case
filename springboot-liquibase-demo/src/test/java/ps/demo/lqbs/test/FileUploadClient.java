package ps.demo.lqbs.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ps.demo.commonlibx.common.RestTemplateTool;

import java.io.File;
import java.io.FileOutputStream;

@Slf4j
public class FileUploadClient {

    public static void main(String[] args) {

        String uploadUrl = "http://xxx";
        File file = new File("xxx");

        RestTemplate restTemplate = RestTemplateTool.getRestTemplate();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        body.add("metadata", "xxxx");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "multipart/form-data");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, requestEntity, String.class);

        log.info("Response http code: {}", response.getStatusCode());
        log.info("Response body: {}", response.getBody());

    }


}
