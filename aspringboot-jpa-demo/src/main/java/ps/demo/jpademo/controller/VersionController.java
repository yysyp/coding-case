package ps.demo.jpademo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.*;
import java.util.Properties;

@Slf4j
@RestController
@RequestMapping("/version")
public class VersionController {

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/git")
    public Properties getGitInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Get git info");
        Properties properties = new Properties();
        properties.put("spring.application.name", applicationName);

        //git.json  or  git.properties
        Resource resource = new ClassPathResource("classpath:git");
        if (resource.exists()) {
            String s = "";
            try (InputStream is = resource.getInputStream();
                 InputStreamReader inr = new InputStreamReader(is, "UTF-8");
                 BufferedReader br = new BufferedReader(inr);
            ) {
                StringBuffer content = new StringBuffer();
                while ((s = br.readLine()) != null) {
                    content = content.append(s);
                }
                properties.put("git", content.toString());
                return properties;
            } catch (Exception e) {
                return properties;
            }
        } else {
            return properties;
        }
    }
}