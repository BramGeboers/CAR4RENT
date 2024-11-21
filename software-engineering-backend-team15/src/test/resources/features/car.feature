Feature: Car

    Scenario: Getting all cars 
        Given some cars
        When  user requests all cars
        Then all cars are returned

    Scenario: Getting no cars
        Given no cars
        When user requests all cars but there are none
        Then no cars are returned
