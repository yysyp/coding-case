package ps.demo.jpademo.test;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ps.demo.commonlibx.common.JsonToolX;
import ps.demo.commonlibx.common.RestTemplateTool;
import ps.demo.jpademo.MainApplication;

@ContextConfiguration
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MainRestTest {
    static String baseUrl = "http://localhost:10001";

    @Test
    public void test_actuator() {
        String url = baseUrl+"/actuator";
        String body = RestTemplateTool.getInstance().getWithUriVariableObjectsForStr(url, "").getBody();
        Console.log("body = {}", body);
        Assertions.assertTrue(JsonToolX.isValidJson(body));
        //Assertions.assertEquals("http://localhost:10001/actuator/prometheus",
        //        JSONUtil.getByPath(JSONUtil.parseObj(body), "_links.prometheus.href"));

    }

    @Test
    public void test_swagger() {
        String url = baseUrl+"/swagger-ui/index.html";
        String body = RestTemplateTool.getInstance().getWithUriVariableObjectsForStr(url, "").getBody();
        Console.log("body = {}", body);

    }

    @Test
    public void test_api_docs() {
        String url = baseUrl+"/api-docs";
        String body = RestTemplateTool.getInstance().getWithUriVariableObjectsForStr(url, "").getBody();
        Assertions.assertTrue(JsonToolX.isValidJson(body));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        Assertions.assertNotNull(jsonObject.get("openapi"));

    }


    @Test
    public void test_api_books() {
        String url = baseUrl+"/api/books/";
        String body = RestTemplateTool.getInstance().getWithUriVariableObjectsForStr(url, "").getBody();
        Assertions.assertTrue(JsonToolX.isValidJson(body));
        JSONArray jsonArray = JSONUtil.parseArray(body);
        Assertions.assertEquals(3, jsonArray.size());

    }

    @Test
    public void test_api_book1() {
        String url = baseUrl+"/api/books/books/{x}";
        String body = RestTemplateTool.getInstance().getWithUriVariableObjectsForStr(url, "1").getBody();
        Assertions.assertTrue(JsonToolX.isValidJson(body));
        JSONObject jsonObject = JSONUtil.parseObj(body);
        Assertions.assertEquals("1", jsonObject.getStr("id"));
    }


}
