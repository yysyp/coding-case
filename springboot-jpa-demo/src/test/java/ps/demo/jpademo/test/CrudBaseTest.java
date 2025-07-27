package ps.demo.jpademo.test;

import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ps.demo.commonlibx.common.JsonXTool;
import ps.demo.commonlibx.common.RestTemplateTool;

import java.util.Map;

@Slf4j
public class CrudBaseTest {

    Setting setting = new Setting("ignore/setting.txt");
    protected String env = "";
    protected String baseUrl = "";

    @BeforeAll
    static void beforeAll() {
        log.info("beforeAll");
    }

    @Test
    public void testGet() {
        ResponseEntity<String> responseEntity = RestTemplateTool.getInstance().getWithUriVariableObjectsForT(baseUrl, new ParameterizedTypeReference<String>() {}, "");
        log.info("Response httpCode:{}", responseEntity.getStatusCode());
        log.info("Response Body:{}", responseEntity.getBody());
    }


    @Test
    public void testPost() {
        Map<String, String> params = Map.of("p1", "v1", "p2", "v2");
        ResponseEntity<String> responseEntity = RestTemplateTool.getInstance().postJsonStrForT(baseUrl, JSONUtil.toJsonStr(params), new ParameterizedTypeReference<String> () {});
        log.info("Response httpCode:{}", responseEntity.getStatusCode());
        log.info("Response Body:{}", responseEntity.getBody());
        Assertions.assertTrue(JsonXTool.isValidJson(responseEntity.getBody()));
    }

    @Test
    public void testPut() {
        Map<String, String> params = Map.of("p1", "v1", "p2", "v2");
        ResponseEntity<String> responseEntity = RestTemplateTool.getInstance().putBodyObjectForStr(baseUrl, params);
        log.info("Response httpCode:{}", responseEntity.getStatusCode());
        log.info("Response Body:{}", responseEntity.getBody());
        Assertions.assertTrue(JsonXTool.isValidJson(responseEntity.getBody()));
    }

    @Test
    public void testDelete() {
        RestTemplate restTemplate = RestTemplateTool.getRestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>("", headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

        log.info("Response httpCode:{}", responseEntity.getStatusCode());
        log.info("Response Body:{}", responseEntity.getBody());


    }
}
