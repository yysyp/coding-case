Feature: Get a book by its id
  As a user
  I want to get a book by its id
  So that I can view its details

  Scenario: Get a book by valid id
    Given prepare data by sql: "data/get_book_by_id-prepare.sql"
    Given a book exists with id "1"
    When I request the book with id "1"
    Then the response status should be 403
    And the response should contain the book details
    And verify clean data by sql: "data/get_book_by_id-verify.sql"