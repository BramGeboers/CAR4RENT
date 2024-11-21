package be.ucll.se.team15backend.unit.PlannerTests;

import be.ucll.se.team15backend.authentication.model.*;
import be.ucll.se.team15backend.authentication.repo.VerifyRepository;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.mail.MailSenderService;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.planner.model.PlannerRequest;
import be.ucll.se.team15backend.planner.service.PlannerService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PlannerService plannerService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private AuthService authService;

    @Mock
    private VerifyRepository verifyRepository;

    @Mock
    private MailSenderService mailSenderService;

    @Test
    void register_ValidRequest_ReturnsRegisterResponse() throws UserException {
        // Given
        RegisterRequest request = new RegisterRequest("test@example.com", "password", "RENTER");
        UserModel savedUser = UserModel.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.RENTER)
                .build();

        VerifyToken verifyToken = VerifyToken.builder()
                .email(request.getEmail())
                .token("token")
                .expirationDate(LocalDateTime.now().plusMinutes(15))
                .build();

        when(userService.checkEmail(request.getEmail())).thenReturn(false);
        when(userService.addUser(any(UserModel.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("token");
        when(verifyRepository.save(any())).thenReturn(verifyToken);

        // When
        RegisterResponse response = authService.register(request, "", "", false);

        // Then
        assertNotNull(response);
        assertEquals(savedUser, response.getUser());
        assertNotNull(response.getToken());
    }

    @Test
    void login_ValidCredentials_ReturnsLoginResponse() throws UserException {
        // Given
        LoginRequest loginRequest = new LoginRequest("loveleen@ucll.com", "loveleen");
        UserModel user = UserModel.builder()
                .email(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .role(Role.RENTER)
                .enabled(true)
                .locked(false)
                .build();
        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("token");

        // When
        LoginResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals(loginRequest.getEmail(), response.getEmail());
        assertNotNull(response.getToken());
        assertEquals(user.getRole(), response.getRole());
    }


}
