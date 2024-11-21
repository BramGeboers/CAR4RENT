package be.ucll.se.team15backend.IntegrationTests.DatabaseTests;

import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.repo.ChatRepository;
import be.ucll.se.team15backend.chats.repo.RoomRepository;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DatabaseIntegrationTests {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void tearDown() {
        chatRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndRetrieveChat() {
        // Given
        Chat chat = new Chat();
        chat.setId("1");

        // When
        chatRepository.save(chat);
        Chat retrievedChat = chatRepository.findChatById("1");

        // Then
        assertNotNull(retrievedChat);
        assertEquals(chat.getId(), retrievedChat.getId());
    }
    @Test
    void testSaveAndRetrieveRoom() {
        // Given
        Room room = new Room();
        room.setRoomId("1");
        // When
        roomRepository.save(room);
        Room retrievedRoom = roomRepository.findRoomByRoomId("1");
        // Then
        assertNotNull(retrievedRoom);
        assertEquals(room.getRoomId(), retrievedRoom.getRoomId());
    }
    @Test
    void testSaveAndRetrieveUser() {
        // Given
        UserModel user = new UserModel();
        user.setEmail("loveleen@ucll.com");
        user.setPassword("loveleen");

        // When
        userRepository.save(user);
        Optional<UserModel> retrievedUserOptional = userRepository.findByEmail("loveleen@ucll.com");

        // Then
        assertTrue(retrievedUserOptional.isPresent());
        UserModel retrievedUser = retrievedUserOptional.get();
        assertEquals(user.getEmail(), retrievedUser.getEmail());
    }


}
