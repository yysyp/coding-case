package ps.demo.jpademo.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ps.demo.jpademo.cucumber.base.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStepDefinitions {

    @LocalServerPort
    private int port;

    private Response response;
    private final ScenarioContext scenarioContext;

    //@Value("${encpwdtest.test1}")
    private String encpwdtest;

    public BookStepDefinitions(ScenarioContext scenarioContext, @Value("${encpwdtest.test1}") String encpwdtest) {
        this.scenarioContext = scenarioContext;
        this.encpwdtest = encpwdtest;
    }

    @Given("a book exists with id {string}")
    public void a_book_exists_with_id(String id) {
        scenarioContext.setContext("bookId", id);
        log.info("==>>encpwdtest: {}, #randomId: {}", encpwdtest, scenarioContext.getContext("#randomId"));

        // Assume a book with the given ID exists in the database
        // This can be set up in a setup method or via a test database

        RestAssured.baseURI = "https://baidu.com";
        RestAssured.port = 443;
        RequestSpecification request = RestAssured.given();
        response = request.get("/");
        log.info("==>>baidu: {}", response.getBody().asString());

    }

    @When("I request the book with id {string}")
    public void i_request_the_book_with_id(String id) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RequestSpecification request = RestAssured.given();
        response = request.get("/api/books/" + id);
        scenarioContext.setContext("response", response);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
        log.info("==>>bookId: {}", scenarioContext.getContext("bookId"));
    }

    @Then("the response should contain the book details")
    public void the_response_should_contain_the_book_details() {
        // Add assertions for book details in the response
        // Example: assertEquals("Expected Title", response.jsonPath().getString("title"));
    }


}