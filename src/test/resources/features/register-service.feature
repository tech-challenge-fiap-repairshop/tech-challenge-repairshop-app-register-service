Feature: Register service customer and vehicle management
  The service stores customer and vehicle data and protects these operations
  with attendant authentication.

  Scenario: Register and retrieve a customer
    Given an authenticated attendant
    When the attendant registers a customer
    Then the response status is 201
    And the response contains a customer identifier
    When the attendant retrieves the registered customer
    Then the response status is 200
    And the response contains customer name "BDD Customer"

  Scenario: Reject a duplicate customer document
    Given an authenticated attendant
    When the attendant registers a customer
    Then the response status is 201
    When the attendant registers the same customer document again
    Then the response status is 409
    And the response error is "Duplicate entity"

  Scenario: Reject a customer with an invalid name
    Given an authenticated attendant
    When the attendant submits a customer with a blank name
    Then the response status is 400
    And the response error is "Validation error"

  Scenario: Register and retrieve a vehicle for a customer
    Given an authenticated attendant
    When the attendant registers a customer
    Then the response status is 201
    When the attendant registers a vehicle for that customer
    Then the response status is 201
    And the vehicle response belongs to the registered customer
    When the attendant retrieves the registered vehicle
    Then the response status is 200
    And the vehicle response belongs to the registered customer

  Scenario: Return not found for an unknown customer
    Given an authenticated attendant
    When the attendant requests a customer that does not exist
    Then the response status is 404
    And the response error is "Resource not found"

  Scenario: Reject an unauthenticated customer list request
    Given an authenticated attendant
    When an unauthenticated caller requests the customer list
    Then the response status is 403
