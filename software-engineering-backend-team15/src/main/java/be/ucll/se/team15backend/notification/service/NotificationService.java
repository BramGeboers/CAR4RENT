package be.ucll.se.team15backend.notification.service;

import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.notification.repo.NotificationRepository;
import be.ucll.se.team15backend.mail.MailSenderService;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    public NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public MailSenderService mailSenderService;

    public Notification createNotification(Notification newNotification, long carId) {
        notificationRepository.save(newNotification);

        for (String email : newNotification.getEmails()) {
            // Send email to each email address
            mailSenderService.sendNewMail(email, "CAR4RENT Notification", newNotification.getMessage());
        }
        return newNotification;
    }

    public List<Notification> deleteNotificationsByRentId(long id) {
        return notificationRepository.deleteAllByRentId(id);
    }

    public List<Notification> getAllNotifications(String email) {
        UserModel user = userService.getUserByEmail(email);

        List<Notification> result = new ArrayList<>();
        List<Notification> allNotis = notificationRepository.findAll();

        for (Notification notification : allNotis) {
            if (user != null && notification.getRent() != null && notification.getRent().getRental() != null) {
                List<String> notificationEmails = notification.getEmails();
                if (notificationEmails != null) {
                    for (String emailnotification : notificationEmails) {
                        if (emailnotification.equals(user.getEmail())) {
                            result.add(notification);
                            break; // Break out of the inner loop once a match is found
                        }
                    }
                }
            }
        }

        if ("ditiseenoplossing".equals(email)) {
            return allNotis;
        }

        return result;
    }
}
