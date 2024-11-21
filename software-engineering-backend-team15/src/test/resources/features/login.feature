Feature: Login

  Scenario: Login with valid credentials
    Given valid credentials
    When user logs in
    Then user is logged in

  Scenario: Login with wrong password
    Given credentials
    When user logs in with wrong password
    Then user is not logged in

  Scenario: Login with wrong email
    Given credentials 2
    When user logs in with wrong email
    Then user is not logged in 2

  Scenario: Login with account not enabled
    Given credentials 3
    When user logs in with account not enabled
    Then user is not logged in 3

  Scenario: Login with account locked
    Given credentials 4
    When user logs in with account locked
    Then user is not logged in 4
