package be.ucll.se.team15backend.notification.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.notification.service.NotificationService;


@CrossOrigin(value = "*")
@RestController
@RequestMapping("/notifications")
public class NotificationController {


    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtService jwtService;


    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public List<Notification> getAllNotifications(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token){
        String email = jwtService.extractEmail(token.substring(7));
        return notificationService.getAllNotifications(email);
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
