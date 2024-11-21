package be.ucll.se.team15backend.notification.repo;

import be.ucll.se.team15backend.notification.model.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findById(long id);

    List<Notification> deleteAllByRentId(long id);

}