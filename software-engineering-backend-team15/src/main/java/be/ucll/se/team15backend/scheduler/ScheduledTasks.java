package be.ucll.se.team15backend.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import be.ucll.se.team15backend.mail.MailSenderService;

@Component
public class ScheduledTasks {

    @Autowired
    private MailSenderService senderService;

    // @Scheduled(cron = "0/30 * * * * *")
    // public void processBookCopiesByIfLoan() {
    //     System.out.println("start");
    //     // String email = "webix49692@bixolabs.com";
    //         System.out.println("User ex :" + "bramgeboers2@gmail.com");
    //         senderService.sendNewMail("bramgeboers2@gmail.com", "Book Loan", "Hello, \n\nThe loan of your book " + "test" + " expired since " + "datum" + "!\nReturn the book as soon as possible!\n\nCronos Book Library");
    // }
}