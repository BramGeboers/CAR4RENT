package be.ucll.se.team15backend.map.controller;


import be.ucll.se.team15backend.map.model.Address;
import be.ucll.se.team15backend.map.model.ClosestsResponse;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.model.MapException;
import be.ucll.se.team15backend.map.service.MapService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;




    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/coordinates")
    public Coordinates getCoordinates(@RequestParam String address) throws IOException, InterruptedException, MapException {
        return mapService.fetchCoordinates(address);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/address")
    public Address getAddress(@RequestParam double latitude, @RequestParam double longitude) throws IOException, MapException {
        return mapService.fetchAddress(latitude, longitude);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/coordinates")
    public Address getCoordinates(@RequestBody Address address) throws IOException, MapException {
        throw new MapException("Address", "Not implemented");
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/closest")
    public List<ClosestsResponse> getClosestAddress(@RequestParam double latitude, @RequestParam double longitude) {
        return mapService.getClosestRentals(latitude, longitude);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MapException.class})
    public Map<String, String> handleServiceExceptions(MapException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), String.format("%s: %s", ex.getField(), ex.getMessage()));
        return errors;
    }

}
