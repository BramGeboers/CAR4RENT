package be.ucll.se.team15backend.billing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.billing.model.Billing;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.billing.service.BillingService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private JwtService jwtService;

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public Page<Billing> getBilling(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
        @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Integer size = 5; // Set the page size
        Pageable pageable = PageRequest.of(page, size);
        String email = jwtService.extractEmail(token.substring(7));
        return billingService.getBilling(pageable, email);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public Billing getBillingById(@RequestHeader(name = "Authorization") @Parameter(hidden = true) String token,
            @PathVariable Long id) {
        String email = jwtService.extractEmail(token.substring(7));
        return billingService.getBillingById(id, email);
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
