package be.ucll.se.team15backend.unit.RentalTests;

import be.ucll.se.team15backend.car.model.Car;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import be.ucll.se.team15backend.rental.model.Rental;
import java.time.LocalDateTime;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;


public class RentalModelTests {

    private Rental rental;

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setUp() {
        // Sample data for Rental
        LocalDateTime startDate = LocalDateTime.of(2024, 3, 30, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 4, 30, 10, 0);
        Car car = new Car();
        String city = "Sint-Truiden";
        String street = "Prins Albertlaan";
        String number = "50";
        String postal = "3800";
        String email = "loveleen@ucll.com";
        String phoneNumber = "1234567890";
        double longitude = 10.12345;
        double latitude = 20.54321;

        rental = Rental.builder()
                .startDate(startDate)
                .endDate(endDate)
                .car(car)
                .city(city)
                .street(street)
                .number(number)
                .postal(postal)
                .phoneNumber(phoneNumber)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    // Test for constructor happy case
    @Test
    public void givenValidRentalData_whenCreatingRental_thenRentalIsCreatedWithValidData() {
        assertNotNull(rental);
        assertEquals(LocalDateTime.of(2024, 3, 30, 10, 0), rental.getStartDate());
        assertEquals(LocalDateTime.of(2024, 4, 30, 10, 0), rental.getEndDate());
        assertNotNull(rental.getCar());
        assertEquals("Sint-Truiden", rental.getCity());
        assertEquals("1234567890", rental.getPhoneNumber());

        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertTrue(violations.isEmpty());
    }

    // Test for start date not null constraint
    @Test
    public void givenNullStartDate_whenCreatingRental_thenConstraintViolationThrown() {
        rental.setStartDate(null);
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Start date is required", violations.iterator().next().getMessage());
    }

    // Test for end date not null constraint
    @Test
    public void givenNullEndDate_whenCreatingRental_thenConstraintViolationThrown() {
        rental.setEndDate(null);
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("End date is required", violations.iterator().next().getMessage());
    }

    // Test for city not blank constraint
    @Test
    public void givenBlankCity_whenCreatingRental_thenConstraintViolationThrown() {
        rental.setCity("");
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("City is required", violations.iterator().next().getMessage());
    }

    // Test for email format constraint
//    @Test
//    public void givenBlankEmail_whenCreatingRental_thenConstraintViolationThrown() {
//        rental.setEmail("");
//        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
//        assertFalse(violations.isEmpty());
//        assertEquals(1, violations.size());
//        assertEquals("Email is required", violations.iterator().next().getMessage());
//    }

    // Test for phone number not blank constraint
    @Test
    public void givenBlankPhoneNumber_whenCreatingRental_thenConstraintViolationThrown() {
        rental.setPhoneNumber("");
        Set<ConstraintViolation<Rental>> violations = validator.validate(rental);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Phone number is required", violations.iterator().next().getMessage());
    }

}
