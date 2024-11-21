package be.ucll.se.team15backend;


import be.ucll.se.team15backend.chats.repo.RoomRepository;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class test {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void test() {

        UserModel user = userService.getUserById(4L);
        System.out.println(roomRepository.getUnreadMessages("general", 4L));

    }
}

