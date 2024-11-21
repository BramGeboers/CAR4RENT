package be.ucll.se.team15backend.unit.JwtTests;

import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTests {

    @Test
    public void generateTokenTest() {
        // Mock UserDetails
        User mockUser = mock(User.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> String.valueOf(Role.RENTER));
        when(mockUser.getUsername()).thenReturn("test@example.com");
        when(mockUser.getAuthorities()).thenReturn(authorities);

        JwtService jwtService = new JwtService();

        String token = jwtService.generateToken(mockUser);

        // Assert
        assertEquals("test@example.com", jwtService.extractUsername(token));
        assertEquals(1, jwtService.extractRoles(token).size());
        assertEquals(String.valueOf(Role.RENTER), jwtService.extractRoles(token).get(0).toString());
    }
    @Test
    public void generateToken_UserDetailsNull_ThrowsIllegalArgumentException() {
        // Given
        UserDetails userDetails = null;
        JwtService jwtService = new JwtService();

        // When / Then
        assertThrows(NullPointerException.class, () -> jwtService.generateToken(userDetails));
    }

}
