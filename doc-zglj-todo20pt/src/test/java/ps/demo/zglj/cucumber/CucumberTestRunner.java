package ps.demo.zglj.cucumber;


import org.junit.platform.suite.api.*;
import org.springframework.test.context.TestPropertySource;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

/**
 RUN with: mvn clean test "-Dspring.profiles.active=dev" "-Dtest=ps.demo.zglj.cucumber.CucumberTestRunner" "-Djasypt.encryptor.password="
 */
@TestPropertySource("classpath:application-jasypt.yml")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ps.demo.zglj.cucumber")
@ConfigurationParameter(key = "cucumber.plugin", value = "html:target/cucumber-html-report.html,json:target/cucumber.json,junit:target/cucumber.xml,pretty:target/cucumber-pretty.txt")
public class CucumberTestRunner {
}
//   windows: mvn clean test "-Dtest=ps.demo.zglj.cucumber.CucumberTestRunner"
//   Linux:   mvn clean test -Dtest=ps.demo.zglj.cucumber.CucumberTestRunner -Dcucumber.options="--tags @test"