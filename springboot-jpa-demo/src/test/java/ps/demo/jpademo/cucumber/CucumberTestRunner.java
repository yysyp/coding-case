package ps.demo.jpademo.cucumber;


import org.junit.platform.suite.api.*;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ps.demo.jpademo.cucumber")
@ConfigurationParameter(key = "cucumber.plugin", value = "html:target/cucumber-html-report.html,json:target/cucumber.json,junit:target/cucumber.xml,pretty:target/cucumber-pretty.txt")
public class CucumberTestRunner {
}
//   windows: mvn clean test "-Dtest=ps.demo.jpademo.cucumber.CucumberTestRunner"
//   Linux:   mvn clean test -Dtest=ps.demo.jpademo.cucumber.CucumberTestRunner -Dcucumber.options="--tags @test"