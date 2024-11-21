package be.ucll.se.team15backend.unit.CarTests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rental.model.Rental;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CarModelTests {

    private String validBrand = "Toyota";
    private String validModel = "Camry";
    private String validType = "Sedan";
    private String validLicensePlate = "ABC123";
    private int validNumberOfSeats = 5;
    private int validNumberOfChildSeats = 2;
    private boolean validFoldingRearSeat = true;
    private boolean validTowBar = false;
    private List<Rental> validRentals = new ArrayList<>();
    private double validMileage = 50000.0;
    private double validFuel = 50.0;
    private double validFuelCapacity = 60.0;
    private double validFuelEstimatedConsumption = 6.5;
    private double validPricePerKm = 0.2;
    private double validPricePerLiterFuel = 1.5;

    private Car car;

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
        car = Car.builder()
                .brand(validBrand)
                .model(validModel)
                .types(validType)
                .licensePlate(validLicensePlate)
                .numberOfSeats(validNumberOfSeats)
                .numberOfChildSeats(validNumberOfChildSeats)
                .foldingRearSeat(validFoldingRearSeat)
                .towBar(validTowBar)
                .rentals(validRentals)
                .mileage(validMileage)
                .fuel(validFuel)
                .fuelCapacity(validFuelCapacity)
                .fuelEstimatedConsumption(validFuelEstimatedConsumption)
                .pricePerKm(validPricePerKm)
                .pricePerLiterFuel(validPricePerLiterFuel)
                .build();
    }

    @Test
    public void givenValidCarDetails_whenCreatingCar_thenCarIsCreatedWithThoseDetails() {
        assertNotNull(car);
        assertEquals(validBrand, car.getBrand());
        assertEquals(validModel, car.getModel());
        assertEquals(validType, car.getTypes());
        assertEquals(validLicensePlate, car.getLicensePlate());
        assertEquals(validNumberOfSeats, car.getNumberOfSeats());
        assertEquals(validNumberOfChildSeats, car.getNumberOfChildSeats());
        assertEquals(validFoldingRearSeat, car.isFoldingRearSeat());
        assertEquals(validTowBar, car.isTowBar());
        assertEquals(validRentals, car.getRentals());
        assertEquals(validMileage, car.getMileage());
        assertEquals(validFuel, car.getFuel());
        assertEquals(validFuelCapacity, car.getFuelCapacity());
        assertEquals(validFuelEstimatedConsumption, car.getFuelEstimatedConsumption());
        assertEquals(validPricePerKm, car.getPricePerKm());
        assertEquals(validPricePerLiterFuel, car.getPricePerLiterFuel());
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenEmptyBrand_whenCreatingCar_thenBrandViolationMessageIsThrown() {
        // when
        String emptyBrand = "   ";
        car.setBrand(emptyBrand);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Brand can not be empty !", violation.getMessage());
        assertEquals("brand", violation.getPropertyPath().toString());
        assertEquals(emptyBrand, violation.getInvalidValue());
    }

    @Test
    public void givenEmptyType_whenCreatingCar_thenTypeViolationMessageIsThrown() {
        // when
        String emptyType = "   ";
        car.setTypes(emptyType);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Type can not be empty !", violation.getMessage());
        assertEquals("types", violation.getPropertyPath().toString());
        assertEquals(emptyType, violation.getInvalidValue());
    }

    @Test
    public void givenEmptyLicensePlate_whenCreatingCar_thenLicensePlateViolationMessageIsThrown() {
        // when
        String emptyLicensePlate = "   ";
        car.setLicensePlate(emptyLicensePlate);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("License Plate can not be empty !", violation.getMessage());
        assertEquals("licensePlate", violation.getPropertyPath().toString());
        assertEquals(emptyLicensePlate, violation.getInvalidValue());
    }

    @Test
    public void givenNumberOfSeatsLessThan1_whenCreatingCar_thenNumberOfSeatsViolationMessageIsThrown() {
        // when
        int invalidValue = 0;
        car.setNumberOfSeats(invalidValue);

        // then
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Number of seats must be at least 1", violation.getMessage());
        assertEquals("numberOfSeats", violation.getPropertyPath().toString());
        assertEquals(invalidValue, violation.getInvalidValue());
    }

}
