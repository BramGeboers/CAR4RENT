package be.ucll.se.team15backend.car.controller;


import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.car.service.CarService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private JwtService jwtService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add")
    public Car addCar(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @Valid @RequestBody Car car){
        String email = jwtService.extractEmail(token.substring(7));
        return carService.addCar(car, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{id}")
    public Car deleteCar(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @PathVariable long id) {
        String email = jwtService.extractEmail(token.substring(7));
        return carService.deleteCar(id, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public Car getMethodName(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @PathVariable Long id) {
        String email = jwtService.extractEmail(token.substring(7));
        return carService.getCarByIdSecure(id, email);
    }

    //advanced search based on the parameters like brand, model, year, price, fueltype, transmission, seats, doors, color. all parameters are optional and can be used in any combination, they are by default null
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    public Page<Car> searchCars(
        @RequestHeader(name="Authorization") @Parameter(hidden = true) String token,
        @RequestParam(name = "brand", required = false) String brand,
        @RequestParam(name = "model", required = false) String model,
        @RequestParam(name = "types", required = false) String types,
        @RequestParam(name = "licensePlate", required = false) String licensePlate,
        @RequestParam(name = "numberOfSeats", required = false) Integer numberOfSeats,
        @RequestParam(name = "numberOfChildSeats", required = false) Integer numberOfChildSeats,
        @RequestParam(name = "foldingRearSeat", required = false) Boolean foldingRearSeat,
        @RequestParam(name = "towBar", required = false) Boolean towBar,
        @RequestParam(name = "mileage", required = false) Double mileage,
        @RequestParam(name = "fuel", required = false) Double fuel,
        @RequestParam(name = "fuelCapacity", required = false) Double fuelCapacity,
        @RequestParam(name = "fuelEstimatedConsumption", required = false) Double fuelEstimatedConsumption,
        @RequestParam(name = "pricePerKm", required = false) Double pricePerKm,
        @RequestParam(name = "pricePerLiterFuel", required = false) Double pricePerLiterFuel,
        @RequestParam(name = "page", defaultValue = "0") int page) {
        
        String email = jwtService.extractEmail(token.substring(7));
        int size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        return carService.searchCars(brand, model, types, licensePlate, numberOfSeats, numberOfChildSeats, foldingRearSeat, towBar, mileage, fuel, fuelCapacity, fuelEstimatedConsumption, pricePerKm, pricePerLiterFuel, pageable, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public Page<Car> getAllCars(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestParam(name = "page", defaultValue = "0") int page) {
        String email = jwtService.extractEmail(token.substring(7));
        int size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        return carService.getAllCarsPageable(pageable, email);
    }   


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceExceptions(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            RuntimeException.class })
    public Map<String, String> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
