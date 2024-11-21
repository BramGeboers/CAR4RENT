package be.ucll.se.team15backend.rental.controller;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.rental.model.Rental;

import be.ucll.se.team15backend.rental.service.RentalService;
import be.ucll.se.team15backend.security.JwtService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*")

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private JwtService jwtService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add/{id}")
    public Rental addRental(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @Valid @RequestBody Rental newRental, @PathVariable long id ) throws ServiceException, IOException, InterruptedException {
        return rentalService.addRental(newRental, id);
    }

    @GetMapping("")
    public Page<Rental> getAllRentals(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        Integer size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        return rentalService.getAllRentals(pageable);
    }   

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/remove/{id}")
    public Rental cancelRentalById(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @Valid @PathVariable long id) throws ServiceException {
        String email = jwtService.extractEmail(token.substring(7));
        return rentalService.cancelRental(id, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public Rental getRentalById(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @Valid @PathVariable long id) throws ServiceException {
        return rentalService.getRentalById(id);
    }

     @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    public Page<Rental> searchRentals(
        @RequestHeader(name="Authorization") @Parameter(hidden = true) String token,
        @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
        @RequestParam(name = "city", required = false) String city,
        @RequestParam(name = "street", required = false) String street,
        @RequestParam(name = "postal", required = false) String postal,
        @RequestParam(name = "page", defaultValue = "0") int page) {
        
        String email = jwtService.extractEmail(token.substring(7));
        int size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        
        return rentalService.searchRental(startDate,endDate,city,street,postal, pageable, email);
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
