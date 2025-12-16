package ps.demo.zglj.cucumber.steps;

import cn.hutool.db.Db;
import com.alibaba.excel.util.StringUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import ps.demo.zglj.cucumber.base.CcmbTool;
import ps.demo.zglj.cucumber.base.ScenarioContext;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonStepdefs {

    private final javax.sql.DataSource dataSource;

    private final ScenarioContext scenarioContext;

    public CommonStepdefs(DataSource dataSource, ScenarioContext scenarioContext) {
        this.dataSource = dataSource;
        this.scenarioContext = scenarioContext;
    }
    @Given("prepare data by sql: {string}")
    public void prepareDataBySql(String sqlFile) {
        String randomId = scenarioContext.getContextAsString("#randomId");
        if (StringUtils.isBlank(randomId)) {
            randomId = RandomStringUtils.randomAlphanumeric(6);
            scenarioContext.setContext("#randomId", randomId);
        }

        String sqlContent = CcmbTool.loadToStringAndReplaceAll(sqlFile, Map.of("#randomId", randomId));
        try (Connection connection = dataSource.getConnection()) {
            Resource btResource = new ByteArrayResource(sqlContent.getBytes());
            log.info("Executing SQL script: {}", sqlFile);
            ScriptUtils.executeSqlScript(connection, btResource);
        } catch (Exception e) {
            log.error("Error executing SQL script: {}", e.getMessage());
        }
    }

    @And("verify clean data by sql: {string}")
    public void verifyCleanDataBySql(String sqlFile) throws SQLException {
        List<String> sqlList =CcmbTool.loadToListAndReplaceAll(sqlFile, Map.of("#randomId", scenarioContext.getContext("#randomId")));

        for (String sql : sqlList) {
            int i = Db.use(dataSource).execute(sql);
            //TODO: Assertions.assertTrue(i > 0);
        }

    }

}
