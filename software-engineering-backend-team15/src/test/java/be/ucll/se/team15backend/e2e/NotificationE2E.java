package be.ucll.se.team15backend.e2e;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rent.model.Rent;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NotificationE2E extends HelperTest {



    @Test
    public void testGetNotifications() throws ServiceException {


        client.get()
                .uri("notifications")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenReinOwner)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].message").exists()
                .jsonPath("$[0].emails").isArray()
                .jsonPath("$[0].rent").exists()
                .jsonPath("$[0].car").exists();
    }

    @Test
    public void testGetNotificationsRenter() throws ServiceException {
        client.get()
                .uri("notifications")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenAxelRenter)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].message").exists()
                .jsonPath("$[0].emails").isArray()
                .jsonPath("$[0].rent").exists()
                .jsonPath("$[0].car").exists();
    }

    @Test
    public void testGetNotificationsAccountant() throws ServiceException {
        client.get()
                .uri("notifications")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tokenBramAccountant)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }




}
