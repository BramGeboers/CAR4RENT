package be.ucll.se.team15backend.unit.ManageTests;

import be.ucll.se.team15backend.manage.model.ManageException;
import be.ucll.se.team15backend.manage.service.ManageService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ManageServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private ManageService manageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenUserIdAndValidRole_whenUpdateRole_thenRoleUpdated() throws ManageException, UserException {
        // Given
        Long userId = 1L;
        String role = "ADMIN";
        UserModel user = new UserModel();
        user.setUserId(userId); // Setting user ID
        when(userService.getUserById(userId)).thenReturn(user);
        // Mock the behavior of userService.updateUser(user) to return a non-null
        // UserModel
        when(userService.updateUser(user)).thenReturn(user);

        // When
        UserModel updatedUser = manageService.updateRole(userId, role);

        // Then
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    public void givenInvalidUserId_whenUpdateRole_thenThrowManageException() throws UserException {
        // Given
        Long userId = 1L;
        String role = "ADMIN";
        when(userService.getUserById(userId)).thenReturn(null);

        // When / Then
        ManageException exception = assertThrows(ManageException.class, () -> {
            manageService.updateRole(userId, role);
        });

        assertEquals("userId", exception.getField());
    }

    @Test
    public void givenInvalidRole_whenUpdateRole_thenThrowManageException() throws ManageException, UserException {
        // Given
        Long userId = 1L;
        String role = "INVALID_ROLE";
        UserModel user = new UserModel();
        // Setting user ID using a field
        user.setUserId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        // When / Then
        ManageException exception = assertThrows(ManageException.class, () -> {
            manageService.updateRole(userId, role);
        });

        assertEquals("role", exception.getField());
    }
    @Test
    public void givenEmptyRole_whenUpdateRole_thenThrowManageException() throws ManageException, UserException {
        // Given
        Long userId = 1L;
        String role = ""; // Empty role
        UserModel user = new UserModel();
        user.setUserId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        // When / Then
        ManageException exception = assertThrows(ManageException.class, () -> {
            manageService.updateRole(userId, role);
        });

        assertEquals("role", exception.getField());
    }


    @Test
    public void givenNonexistentUserAndRole_whenUpdateRole_thenThrowManageException() throws UserException {
        // Given
        Long userId = 1L;
        String role = "ADMIN";
        when(userService.getUserById(userId)).thenReturn(null);

        // When / Then
        ManageException exception = assertThrows(ManageException.class, () -> {
            manageService.updateRole(userId, role);
        });

        assertEquals("userId", exception.getField());
    }

    @Test
    public void givenNonexistentRole_whenUpdateRole_thenThrowManageException() throws ManageException, UserException {
        // Given
        Long userId = 1L;
        String role = "NONEXISTENT_ROLE"; // Invalid role
        UserModel user = new UserModel();
        user.setUserId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        // When / Then
        ManageException exception = assertThrows(ManageException.class, () -> {
            manageService.updateRole(userId, role);
        });

        assertEquals("role", exception.getField());
    }
}
