package ps.demo.jpademo.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookStepDefinitions {

    @LocalServerPort
    private int port;

    private Response response;

    @Given("a book exists with id {string}")
    public void a_book_exists_with_id(String id) {
        // Assume a book with the given ID exists in the database
        // This can be set up in a setup method or via a test database
    }

    @When("I request the book with id {string}")
    public void i_request_the_book_with_id(String id) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RequestSpecification request = RestAssured.given();
        response = request.get("/api/books/" + id);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int statusCode) {
        assertEquals(statusCode, response.getStatusCode());
    }

    @Then("the response should contain the book details")
    public void the_response_should_contain_the_book_details() {
        // Add assertions for book details in the response
        // Example: assertEquals("Expected Title", response.jsonPath().getString("title"));
    }
}