package be.ucll.se.team15backend.authentication.service;

import be.ucll.se.team15backend.authentication.model.*;
import be.ucll.se.team15backend.authentication.repo.VerifyRepository;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.mail.MailSenderService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ChatService chatService;

    @Autowired
    private VerifyRepository verifyRepository;

    @Autowired
    private MailSenderService mailSenderService;

    public RegisterResponse register(RegisterRequest request, String frontend, String backend, Boolean allRoles)
            throws UserException {
        if (userService.checkEmail(request.getEmail())) {
            throw new UserException("Email", "Email already in use");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserException("Role", "Please provide a valid role (OWNER, RENTER, ACCOUNTANT, ADMIN)");
        }

        if (!allRoles) {
            if (role.equals(Role.ADMIN)) {
                throw new UserException("Role", "You cannot register as an admin");
            }
            if (role.equals(Role.ACCOUNTANT)) {
                throw new UserException("Role", "You cannot register as an accountant");
            }
            if (role.equals(Role.BOT)) {
                throw new UserException("Role", "You cannot register as a bot");
            }
        }

        UserModel user = UserModel.builder()
                .email(request.getEmail().toLowerCase())
                .password(request.getPassword())
                .locked(false)
                .enabled(false)
                .role(role)
                .build();
        UserModel savedUser = userService.addUser(user);

        chatService.initRooms(savedUser);

        String token = jwtService.generateToken(savedUser);

        String verificationToken = generateToken();

        VerifyToken verifyToken = VerifyToken.builder()
                .token(verificationToken)
                .email(savedUser.getEmail())
                .expirationDate(LocalDateTime.now().plusHours(1))
                .build();

        verifyRepository.save(verifyToken);

        String verifyLink = "http://" + backend + "/auth/verify?token=" + verificationToken + "&frontend=" + frontend;

        String message = "Please click the link below to verify your account: \n" + verifyLink;

        mailSenderService.sendNewMail(savedUser.getEmail(), "Verify your account", message);

        return RegisterResponse.builder()
                .user(savedUser)
                .token(token)
                .build();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LoginResponse login(LoginRequest loginRequest) throws UserException {
        String email = loginRequest.getEmail().toLowerCase();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        UserModel user = userService.getUserByEmail(email);

        if (user.getLocked()) {
            throw new UserException("Account", "Your account is locked");
        }

        if (!user.getEnabled()) {
            throw new UserException("Account", "Your account is not enabled");
        }

        if (user.getRole().equals(Role.BOT)) {
            throw new UserException("Account", "You cannot login as a bot");
        }

        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .email(user.getEmail())
                .token(token)
                .role(user.getRole())
                .id(user.getUserId())
                .build();

    }

    public String verify(String token, String frontend) {
        if (verifyRepository.findByToken(token).orElse(null) == null) {

            return convertURL(frontend, "Invalid token");
        }
        VerifyToken verifyToken = verifyRepository.findByToken(token).orElse(null);
        String email = verifyToken.getEmail();
        if (!userService.checkEmail(email)) {
            return convertURL(frontend, "Invalid email");
        }
        LocalDateTime expirationDate = verifyToken.getExpirationDate();
        if (LocalDateTime.now().isAfter(expirationDate)) {
            return convertURL(frontend, "Token expired");
        }
        UserModel user = userService.getUserByEmail(email);

        if (user == null) {
            return convertURL(frontend, "User not found");
        }
        
        if (user.getEnabled()) {
            return convertURL(frontend, "Account already verified");
        }
        user.setEnabled(true);
        userService.updateUser(user);
        String jwtToken = jwtService.generateToken(user);
        return frontend + "/directlogin?email=" + email + "&role=" + user.getRole()
                + "&id=" + user.getUserId() + "&token=" + jwtToken;
    }

    public String convertURL(String frontend, String message) {
        return frontend + "/directlogin?message=" + message;
    }

}
