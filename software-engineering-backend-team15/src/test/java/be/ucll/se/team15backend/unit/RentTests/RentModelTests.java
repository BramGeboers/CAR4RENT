package be.ucll.se.team15backend.unit.RentTests;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rent.model.Rent;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RentModelTests {

    private Rent rent;
    private static Validator validator;

    private static ValidatorFactory validatorFactory;

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
        rent = new Rent();
        rent.setCar(new Car());
        rent.setPhoneNumber("123456789");
        rent.setNationalIdentificationNumber("123456789");
        rent.setBirthDate(LocalDate.of(1990, 1, 1));
        rent.setActive(true);
        rent.setDrivingLicenseNumber("ABC123");
        rent.setStartDate(LocalDateTime.of(2024, 3, 30, 10, 0));
        rent.setStartMileage(10000.0);
        rent.setStartFuel(50.0);
    }

    @Test
    public void givenValidRentDetails_whenCreatingRent_thenNoConstraintViolations() {
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertTrue(violations.isEmpty());
    }

//    @Test
//    public void givenEmptyEmail_whenCreatingRent_thenEmailViolationMessageIsThrown() {
//        rent.setEmail("");
//        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
//        assertFalse(violations.isEmpty());
//    }

    @Test
    public void givenEmptyNationalIdentificationNumber_whenCreatingRent_thenViolationMessageIsThrown() {
        rent.setNationalIdentificationNumber(null);
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertFalse(violations.isEmpty());

        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("Identification number of national register is required", violation.getMessage());
    }


    @Test
    public void givenNullBirthDate_whenCreatingRent_thenViolationMessageIsThrown() {
        rent.setBirthDate(null);
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void givenEmptyDrivingLicenseNumber_whenCreatingRent_thenViolationMessageIsThrown() {
        rent.setDrivingLicenseNumber(null);
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Rent> violation = violations.iterator().next();
        assertEquals("drivingLicenseNumber is required", violation.getMessage());

}



    @Test
    public void givenEndDateBeforeStartDate_whenCreatingRent_thenViolationMessageIsThrown() {
        Rent rent = new Rent();
        rent.setStartDate(LocalDateTime.of(2024, 3, 30, 10, 0));
        rent.setEndDate(LocalDateTime.of(2024, 3, 29, 10, 0));
        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void givenPriceZeroOrLess_whenCreatingRent_thenViolationMessageIsThrown() {
        Rent rent = new Rent();
        rent.setPrice(0);

        Set<ConstraintViolation<Rent>> violations = validator.validate(rent);

        assertFalse(violations.isEmpty());
    }

}
