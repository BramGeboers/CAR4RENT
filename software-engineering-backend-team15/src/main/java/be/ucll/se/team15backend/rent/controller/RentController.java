package be.ucll.se.team15backend.rent.controller;

import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rent.model.CheckInRequest;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.rent.service.RentService;
import be.ucll.se.team15backend.rental.model.Rental;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*")

@RestController
@RequestMapping("/rents")
public class RentController {
    @Autowired
    private CarService carService;
    @Autowired
    private RentService rentService;

    @Autowired
    private JwtService jwtService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add/{rentId}")
    public Rent addRent(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @PathVariable Long rentId) {
        String email = jwtService.extractEmail(token.substring(7));
        return rentService.confirmRent(rentId, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public Rent getMethodName(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @PathVariable Long id) {
        // String email = jwtService.extractEmail(token.substring(7));
        return rentService.getRentById(id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/pay/{rentId}")
    public Rent payRent(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @PathVariable Long rentId) {
        String email = jwtService.extractEmail(token.substring(7));

        return rentService.payRent(rentId, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/addRequest/{carId}/{rentalId}")
    public Rent rentRequest(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @PathVariable Long carId, @PathVariable Long rentalId, @RequestBody Rent rent) throws ServiceException {
        Car car = carService.getCarById(carId);
        rent.setCar(car);
        String email = jwtService.extractEmail(token.substring(7));
        return rentService.rentRequest(rent, carId, rentalId, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    public Page<Rent> showRent(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Integer size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        return rentService.showRent(pageable);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public Page<Rent> getRentForEmail(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Integer size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        String email = jwtService.extractEmail(token.substring(7));
        return rentService.getRent(pageable, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/remove/{id}")
    public Rent cancelRentalById(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @Valid @PathVariable long id) throws ServiceException {
        return rentService.cancelRent(id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    public List<Rent> searchRentsByEmail(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @RequestParam String email) throws ServiceException {
        return rentService.searchRentsByEmail(email);
    }

     @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search/filter")
    public Page<Rent> searchRents(
        @RequestHeader(name="Authorization") @Parameter(hidden = true) String token,
        @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
        @RequestParam(name = "city", required = false) String city,
        @RequestParam(name = "licenseplate", required = false) String licenseplate,
        @RequestParam(name = "email", required = false) String email,
        @RequestParam(name = "page", defaultValue = "0") int page) {
        
        String emailOwner = jwtService.extractEmail(token.substring(7));
        int size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        
        return rentService.searchRent(startDate,endDate,city,licenseplate,email, pageable, emailOwner);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/cancelRequest/{id}")
    public Rent cancelRentRequest(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @Valid @PathVariable long id) throws ServiceException {
        return rentService.cancelRentRequest(id);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/checkIn")
    public Rent checkIn(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @Valid @RequestBody CheckInRequest checkInRequest) throws ServiceException {
        return rentService.checkIn(checkInRequest);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/checkOut")
    public Rent checkOut(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @Valid @RequestBody CheckInRequest checkInRequest) throws ServiceException {
        return rentService.checkOut(checkInRequest);
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
