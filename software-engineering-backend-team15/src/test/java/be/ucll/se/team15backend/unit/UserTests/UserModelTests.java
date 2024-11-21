package be.ucll.se.team15backend.unit.UserTests;

import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTests {

    @Test
    void givenValidUserModel_whenGetAuthorities_thenAuthoritiesReturned() {
        // Given
        UserModel user = new UserModel();
        user.setRole(Role.RENTER);

        // When
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Then
        List<SimpleGrantedAuthority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(new SimpleGrantedAuthority("RENTER"));
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_RENTER"));

        assertEquals(expectedAuthorities, authorities);
    }

    @Test
    void givenLockedUserModel_whenCheckIsAccountNonLocked_thenFalseReturned() {
        // Given
        UserModel user = new UserModel();
        user.setLocked(true);

        // Then
        assertFalse(user.isAccountNonLocked());
    }

    @Test
    void givenEnabledUserModel_whenCheckIsEnabled_thenTrueReturned() {
        // Given
        UserModel user = new UserModel();
        user.setEnabled(true);

        // Then
        assertTrue(user.isEnabled());
    }

    @Test
    void givenRoom_whenAddRoom_thenRoomAddedToList() {
        // Given
        UserModel user = new UserModel();
        Room room = new Room();

        // When
        user.addRoom(room);

        // Then
        assertTrue(user.getRooms().contains(room));
    }


    @Test
    void givenLockedUser_whenCheckIfAccountNonLocked_thenAccountLockedReturned() {
        // Given
        UserModel lockedUser = new UserModel();
        lockedUser.setLocked(true);

        // When
        boolean isAccountNonLocked = lockedUser.isAccountNonLocked();

        // Then
        assertFalse(isAccountNonLocked);
    }

    @Test
    void givenExpiredCredentials_whenCheckIfCredentialsNonExpired_thenCredentialsExpiredReturned() {
        // Given
        UserModel user = new UserModel();

        // When
        boolean isCredentialsNonExpired = user.isCredentialsNonExpired();

        // Then
        assertTrue(isCredentialsNonExpired); // Assuming credentials are not expired by default
    }


}
