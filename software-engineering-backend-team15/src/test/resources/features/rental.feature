Feature: Rental

  Scenario: Getting all Rentals
    Given Some rentals
    When  User requests all rentals
    Then The rentals are returned
