package be.ucll.se.team15backend.complaint.service;

import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.complaint.model.ComplaintRequest;
import be.ucll.se.team15backend.complaint.repo.ComplaintRepository;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private final ComplaintRepository complaintRepository;
    @Autowired
    private final UserService userService;

    @Autowired
    private ChatService chatService;


    public ComplaintService(ComplaintRepository complaintRepository, UserService userService) {
        this.complaintRepository = complaintRepository;
        this.userService = userService;
    }

    public Complaint submitComplaint(ComplaintRequest complaintRequest) {
        Complaint complaint = new Complaint();
        complaint.setTitle(complaintRequest.getTitle());
        complaint.setDescription(complaintRequest.getDescription());
        complaint.setUserEmail(complaintRequest.getUserEmail());

        Complaint savedComplaint = complaintRepository.save(complaint);

        createComplaintRoom(savedComplaint);

        return savedComplaint;
    }

    public void createComplaintRoom(Complaint complaint) {
        Room complaintRoom = Room.builder()
                .roomName("Complaint: " + complaint.getTitle())
                .build();

        UserModel user = userService.getUserByEmail(complaint.getUserEmail());

        if (user == null) {
            return;
        }

        complaintRoom.addUser(user);

        List<UserModel> admins = userService.getAllAdmins();
        for (UserModel admin : admins) {
            complaintRoom.addUser(admin);
        }

        Chat startComplaintChat = Chat.builder()
                .data("Status: \"" + complaint.getTitle() + "\" has been submitted with id: " + complaint.getId())
                .sender(user)
                .room(complaintRoom)
                .build();

        Chat startComplaintChat2 = Chat.builder()
                .data("Complaint: " + complaint.getDescription())
                .sender(user)
                .room(complaintRoom)
                .build();

        Chat startComplaintChat3 = Chat.builder()
                .data("An admin will be with you shortly, please be patient.")
                .sender(user)
                .room(complaintRoom)
                .build();

        chatService.saveRoom(complaintRoom);
        chatService.saveChat(startComplaintChat);
        chatService.saveChat(startComplaintChat2);
        chatService.saveChat(startComplaintChat3);

        complaintRoom.addChat(startComplaintChat);
        complaintRoom.addChat(startComplaintChat2);
        complaintRoom.addChat(startComplaintChat3);

        chatService.saveRoom(complaintRoom);



    }


    public Page<Complaint> getAllComplaints(Pageable pageable) {
        return complaintRepository.findAll(pageable);
    }

}



