package be.ucll.se.team15backend.unit.NotificationTests;

import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.notification.repo.NotificationRepository;
import be.ucll.se.team15backend.mail.MailSenderService;
import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTests {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserService userService;
    @Mock
    private MailSenderService mailSenderService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNotification_whenCreateNotification_thenSendEmailsAndSave() {
        // Given
        Notification newNotification = new Notification();
        newNotification.setEmails(Arrays.asList("test@example.com", "another@example.com"));
        newNotification.setMessage("Test message");

        // When
        when(notificationRepository.save(newNotification)).thenReturn(newNotification);
        Notification savedNotification = notificationService.createNotification(newNotification, 1);

        // Then
        assertEquals(newNotification, savedNotification);
        verify(mailSenderService, times(1)).sendNewMail(eq("test@example.com"), eq("CAR4RENT Notification"),
                eq("Test message"));
        verify(mailSenderService, times(1)).sendNewMail(eq("another@example.com"), eq("CAR4RENT Notification"),
                eq("Test message"));
    }

    @Test
    void givenRentId_whenDeleteNotificationsByRentId_thenDeleteAndReturnNotifications() {
        // Given
        long rentId = 1L;
        List<Notification> deletedNotifications = new ArrayList<>();
        Notification notification1 = new Notification();
        Notification notification2 = new Notification();
        deletedNotifications.add(notification1);
        deletedNotifications.add(notification2);

        // When
        when(notificationRepository.deleteAllByRentId(rentId)).thenReturn(deletedNotifications);
        List<Notification> result = notificationService.deleteNotificationsByRentId(rentId);

        // Then
        assertEquals(deletedNotifications, result);
    }

    @Test
    void givenInvalidRentId_whenDeleteNotificationsByRentId_thenThrowIllegalArgumentException() {
        // Arrange
        long invalidRentId = -1L;
        when(notificationRepository.deleteAllByRentId(invalidRentId)).thenThrow(IllegalArgumentException.class);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.deleteNotificationsByRentId(invalidRentId);
        });
    }



    @Test
    void givenUserNotFound_whenGetAllNotifications_thenReturnEmptyList() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        when(userService.getUserByEmail(userEmail)).thenReturn(null);

        // When
        List<Notification> result = notificationService.getAllNotifications(userEmail);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNotificationWithNullEmails_whenCreateNotification_thenThrowIllegalArgumentException() {
        // Arrange
        Notification notificationWithNullEmails = new Notification();
        notificationWithNullEmails.setMessage("Test message");

        // When / Then
        assertThrows(NullPointerException.class, () -> {
            notificationService.createNotification(notificationWithNullEmails, 1);
        });
    }



    // void givenUserEmail_whenGetAllNotifications_thenRetrieveUserNotifications()
    // {}
}
