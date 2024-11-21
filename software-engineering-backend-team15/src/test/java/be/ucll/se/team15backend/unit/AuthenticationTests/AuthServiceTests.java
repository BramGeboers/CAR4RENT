package be.ucll.se.team15backend.unit.AuthenticationTests;

import be.ucll.se.team15backend.authentication.model.*;
import be.ucll.se.team15backend.authentication.repo.VerifyRepository;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.mail.MailSenderService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void register_ValidRequest_ReturnsRegisterResponse() throws UserException {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("loveleen@ucll.com")
                .password("loveleen")
                .role("ADMIN")
                .build();
        when(userService.checkEmail(request.getEmail())).thenReturn(false);
        when(userService.addUser(any(UserModel.class))).thenReturn(new UserModel());
        when(jwtService.generateToken(any(UserModel.class))).thenReturn("token");

        // When
        RegisterResponse response = authService.register(request, "", "", true);

        // Then
        assertEquals("token", response.getToken());
        // Add more assertions as needed
    }

    @Test
    public void login_ValidCredentials_ReturnsLoginResponse() throws UserException {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("loveleen@ucll.com");
        request.setPassword("loveleen");
        UserModel user = new UserModel();
        user.setEmail("loveleen@ucll.com");
        user.setLocked(false);
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        when(userService.getUserByEmail(request.getEmail())).thenReturn(user);
        when(jwtService.generateToken(any(UserModel.class))).thenReturn("token");

        // When
        LoginResponse response = authService.login(request);

        // Then
        assertEquals("loveleen@ucll.com", response.getEmail());
        assertEquals("token", response.getToken());
    }

    @Test
    public void login_LockedAccount_ThrowsUserException() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        UserModel lockedUser = new UserModel();
        lockedUser.setEmail("test@example.com");
        lockedUser.setLocked(true);
        lockedUser.setEnabled(true);
        lockedUser.setRole(Role.OWNER);
        when(userService.getUserByEmail(request.getEmail())).thenReturn(lockedUser);

        // When / Then
        assertThrows(UserException.class, () -> authService.login(request));
    }

    @Test
    public void register_ExistingEmail_ThrowsUserException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("existing@example.com")
                .password("password")
                .role("USER")
                .build();
        when(userService.checkEmail(request.getEmail())).thenReturn(true); // Email already exists

        // When / Then
        assertThrows(UserException.class, () -> authService.register(request, "", "", false));
    }

    @Test
    public void login_DisabledAccount_ThrowsUserException() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("disabled@example.com");
        request.setPassword("password");
        UserModel disabledUser = new UserModel();
        disabledUser.setEmail("disabled@example.com");
        disabledUser.setLocked(false);
        disabledUser.setEnabled(false); // User account is disabled
        disabledUser.setRole(Role.RENTER);
        when(userService.getUserByEmail(request.getEmail())).thenReturn(disabledUser);

        // When / Then
        assertThrows(UserException.class, () -> authService.login(request));
    }

    @Test
    public void register_InvalidRole_ThrowsUserException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .role("INVALID_ROLE")
                .build();

        // When / Then
        assertThrows(UserException.class, () -> authService.register(request, "", "", false));
    }

    @Test
    public void register_AdminRole_ThrowsUserException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("admin@example.com")
                .password("password")
                .role("ADMIN")
                .build();

        // When / Then
        assertThrows(UserException.class, () -> authService.register(request, "", "", false));
    }

    @Test
    public void register_AccountantRole_ThrowsUserException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("accountant@example.com")
                .password("password")
                .role("ACCOUNTANT")
                .build();

        // When / Then
        assertThrows(UserException.class, () -> authService.register(request, "", "", false));
    }

    @Test
    public void register_BotRole_ThrowsUserException() {
        // Given
        RegisterRequest request = RegisterRequest.builder()
                .email("bot@example.com")
                .password("password")
                .role("BOT")
                .build();

        // When / Then
        assertThrows(UserException.class, () -> authService.register(request, "", "", false));
    }

    @Test
    public void login_BotUser_ThrowsUserException() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("bot@example.com");
        request.setPassword("password");
        UserModel botUser = new UserModel();
        botUser.setEmail("bot@example.com");
        botUser.setLocked(false);
        botUser.setEnabled(true);
        botUser.setRole(Role.BOT);
        when(userService.getUserByEmail(request.getEmail())).thenReturn(botUser);

        // When / Then
        assertThrows(UserException.class, () -> authService.login(request));
    }

    @Test
    public void GivenValidInput_ValidToken_ReturnsValidURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(false)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(verifyToken));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.checkEmail(email)).thenReturn(true);

        // When

        String response = authService.verify(token, frontend);

        // Then

        assertEquals("http://localhost:3000/directlogin?email=test@ucll.be&role=null&id=null&token=null", response);

    }

    @Test
    public void whenGivenExpiredToken_validateToken_ReturnsInValidURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().minusHours(2))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(false)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(verifyToken));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.checkEmail(email)).thenReturn(true);

        // When

        String response = authService.verify(token, frontend);

        // Then

        assertEquals("http://localhost:3000/directlogin?message=Token expired", response);

    }

    @Test
    public void GivenEnabledUser_ValidateToken_ReturnsInvalitURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(true)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(verifyToken));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.checkEmail(email)).thenReturn(true);

        // When

        String response = authService.verify(token, frontend);

        // Then

        assertEquals("http://localhost:3000/directlogin?message=Account already verified", response);

    }

    @Test
    public void GivenInvalidUser_ValidateToken_ReturnsInvalitURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(false)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(verifyToken));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(null);
        when(userService.checkEmail(email)).thenReturn(true);

        // When

        String response = authService.verify(token, frontend);

        // Then
        assertEquals("http://localhost:3000/directlogin?message=User not found", response);

    }

    @Test
    public void GivenInvalidToken_ValidateToken_ReturnsInvalitURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(false)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(null));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.checkEmail(email)).thenReturn(true);

        // When

        String response = authService.verify(token, frontend);

        // Then
        assertEquals("http://localhost:3000/directlogin?message=Invalid token", response);

    }

    @Test
    public void GivenInvalidEmail_ValidateToken_ReturnsInvalitURL() {
        // Given
        String token = "token";
        String email = "test@ucll.be";
        String frontend = "http://localhost:3000";

        VerifyToken verifyToken = VerifyToken.builder()
                .email(email)
                .expirationDate(LocalDateTime.now().plusHours(1))
                .token(token)
                .build();

        UserModel user = UserModel.builder()
                .enabled(false)
                .build();

        when(verifyRepository.findByToken(token)).thenReturn(Optional.ofNullable(verifyToken));
        when(userService.checkEmail(email)).thenReturn(false);
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(userService.checkEmail(email)).thenReturn(false);

        // When

        String response = authService.verify(token, frontend);

        // Then
        assertEquals("http://localhost:3000/directlogin?message=Invalid email", response);

    }

}
