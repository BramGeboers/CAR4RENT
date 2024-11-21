package be.ucll.se.team15backend.authentication.controller;

import be.ucll.se.team15backend.authentication.model.LoginRequest;
import be.ucll.se.team15backend.authentication.model.LoginResponse;
import be.ucll.se.team15backend.authentication.model.RegisterRequest;
import be.ucll.se.team15backend.authentication.model.RegisterResponse;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.user.model.UserException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest loginRequest) throws UserException {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestHeader HttpHeaders headers,
            @Valid @RequestBody RegisterRequest registerRequest) throws UserException {
        String frontend = headers.getFirst("Origin");
        String backend = headers.getFirst("Host");
        return authService.register(registerRequest, frontend, backend, false);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(RedirectAttributes redirectAttributes, @RequestParam String token,
            @RequestParam String frontend) throws UserException {
        String redirectURL = authService.verify(token, frontend);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectURL).build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public String logout() {
        return "logout";
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
    @ExceptionHandler({ UserException.class })
    public Map<String, String> handleServiceExceptions(UserException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), String.format("%s: %s", ex.getField(), ex.getMessage()));
        return errors;
    }
}
