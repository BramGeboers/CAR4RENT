package be.ucll.se.team15backend.unit.UserTests;

import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_ValidUser_UserAddedSuccessfully() {
        // Given
        String email = "test@example.com";
        String password = "password";
        Role role = Role.RENTER;
        UserModel newUser = UserModel.builder()
                .email(email)
                .password(password)
                .role(role)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            user.setUserId(1L); // Simulate saving in repository
            return user;
        });

        // When
        UserModel savedUser = null;
        try {
            savedUser = userService.addUser(newUser);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Then
        assertNotNull(savedUser);
        assertEquals(email, savedUser.getEmail());
        assertEquals(role, savedUser.getRole());
        assertTrue(BCrypt.checkpw(password, savedUser.getPassword())); // Check if password is hashed correctly
    }


    @Test
    void addUser_DuplicateEmail_ThrowsUserException() {
        // Given
        UserModel user = new UserModel();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When / Then
        assertThrows(UserException.class, () -> userService.addUser(user));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void givenNewUser_whenAddUser_thenUserAddedSuccessfully() {
        // Given
        UserModel newUser = UserModel.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.RENTER)
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenReturn(newUser);

        // When
        UserModel savedUser;
        try {
            savedUser = userService.addUser(newUser);
        } catch (Exception e) {
            savedUser = null;
        }

        // Then
        assertNotNull(savedUser);
        assertEquals(newUser.getEmail(), savedUser.getEmail());
        assertEquals(newUser.getRole(), savedUser.getRole());
    }

    @Test
    void addUser_UserWithEmailAlreadyExists_ThrowsUserException() {
        // Given
        String email = "test@example.com";
        UserModel existingUser = UserModel.builder()
                .email(email)
                .password("password")
                .role(Role.RENTER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When
        UserModel newUser = UserModel.builder()
                .email(email)
                .password("newPassword")
                .role(Role.RENTER)
                .build();

        // Then
        assertThrows(UserException.class, () -> userService.addUser(newUser));
    }


    @Test
    void addUser_DatabaseFailureDuringUserCreation_ReturnsNull() throws UserException {
        // Given
        String email = "test@example.com";
        UserModel newUser = UserModel.builder()
                .email(email)
                .password("password")
                .role(Role.RENTER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenThrow(new RuntimeException("Database failure"));

        // When
        UserModel savedUser = null;
        try {
            savedUser = userService.addUser(newUser);
        } catch (RuntimeException e) {
            // Database failure occurred, which is expected
        }

        // Then
        assertNull(savedUser);
    }

    @Test
    void updateUser_ValidUser_UserUpdatedSuccessfully() {
        // Given
        UserModel existingUser = UserModel.builder()
                .userId(1L)
                .email("test@example.com")
                .password("oldPassword")
                .role(Role.RENTER)
                .build();
        UserModel updatedUser = UserModel.builder()
                .userId(1L)
                .email("test@example.com")
                .password("newPassword")
                .role(Role.RENTER)
                .build();
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        // When
        UserModel result = userService.updateUser(updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("newPassword", result.getPassword());
    }


    @Test
    void updateUser_NonExistingUser_ReturnsNull() {
        // Given
        UserModel nonExistingUser = UserModel.builder()
                .userId(1L)
                .email("test@example.com")
                .password("newPassword")
                .role(Role.RENTER)
                .build();

        // When
        UserModel result = userService.updateUser(nonExistingUser);

        // Then
        assertNull(result);
    }


    @Test
    void updateUser_NullUser_ReturnsNull() {
        // Given
        UserModel nullUser = null;

        // When
        UserModel result = userService.updateUser(nullUser);

        // Then
        assertNull(result);
    }

    @Test
    void getUserByEmail_ExistingEmail_ReturnsUser() {
        // Given
        String email = "test@example.com";
        UserModel user = UserModel.builder()
                .userId(1L)
                .email(email)
                .password("password")
                .role(Role.RENTER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        UserModel result = userService.getUserByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getUserByEmail_NonExistingEmail_ReturnsNull() {
        // Given
        String nonExistingEmail = "nonexisting@example.com";
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // When
        UserModel result = userService.getUserByEmail(nonExistingEmail);

        // Then
        assertNull(result);
    }

}
