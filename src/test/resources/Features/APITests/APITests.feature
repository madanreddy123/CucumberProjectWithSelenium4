@apitests
Feature: API Tests

  Scenario: Verify POST call response
    When user makes a POST api call
    Then verify response status code is 201