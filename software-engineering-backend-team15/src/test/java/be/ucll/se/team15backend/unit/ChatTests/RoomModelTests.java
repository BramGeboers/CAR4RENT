package be.ucll.se.team15backend.unit.ChatTests;

import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RoomModelTests {

    @Test
    void givenValidRoom_whenCreateRoom_thenRoomCreatedSuccessfully() {
        // Given
        Room room = Room.builder()
                .roomId("1")
                .roomName("Test Room")
                .chats(new ArrayList<>())
                .users(new ArrayList<>())
                .build();

        // Then
        assertEquals("1", room.getRoomId());
        assertEquals("Test Room", room.getRoomName());
        assertEquals(0, room.getChats().size());
        assertEquals(0, room.getUsers().size());
    }

    @Test
    void givenValidChat_whenCreateChat_thenChatCreatedSuccessfully() {
        // Given
        UserModel sender = UserModel.builder()
                .email("sender@example.com")
                .password("password")
                .role(Role.RENTER) // Updated to RENTER
                .enabled(true)
                .locked(false)
                .rooms(new ArrayList<>())
                .build();
        Room room = Room.builder()
                .roomId("1")
                .roomName("Test Room")
                .chats(new ArrayList<>())
                .users(new ArrayList<>())
                .build();
        Chat chat = Chat.builder()
                .id("1")
                .timestamp(LocalDateTime.now())
                .sender(sender)
                .room(room)
                .data("Test Message")
                .readBy(new ArrayList<>())
                .build();

        // Then
        assertEquals("1", chat.getId());
        assertEquals("Test Message", chat.getData());
        assertEquals(sender, chat.getSender());
        assertEquals(room, chat.getRoom());
        assertEquals(0, chat.getReadBy().size());
    }


    @Test
    void givenNullUser_whenGetUnreadMessages_thenZeroReturned() {
        // Given
        Room room = new Room();

        // When
        int unreadMessages = room.getUnreadMessages(null);

        // Then
        assertEquals(0, unreadMessages);
    }

    @Test
    void givenUserNotInRoom_whenGetUnreadMessages_thenZeroReturned() {
        // Given
        Room room = new Room();
        UserModel user = new UserModel();

        // When
        int unreadMessages = room.getUnreadMessages(user);

        // Then
        assertEquals(0, unreadMessages);
    }

    @Test
    void givenEmptyRoom_whenGetLastMessage_thenEmptyStringReturned() {
        // Given
        Room room = new Room();

        // When
        String lastMessage = room.getLastMessage();

        // Then
        assertEquals("", lastMessage);
    }

    @Test
    void givenRoomWithChats_whenGetLastMessage_thenLastMessageReturned() {
        // Given
        Room room = new Room();
        Chat chat1 = Chat.builder().data("Message 1").build();
        Chat chat2 = Chat.builder().data("Message 2").build();
        room.setChats(Arrays.asList(chat1, chat2));

        // When
        String lastMessage = room.getLastMessage();

        // Then
        assertEquals("Message 2", lastMessage);
    }

    @Test
    void givenEmptyRoom_whenGetLastMessageSender_thenEmptyStringReturned() {
        // Given
        Room room = new Room();

        // When
        String lastMessageSender = room.getLastMessageSender();

        // Then
        assertEquals("", lastMessageSender);
    }

    @Test
    void givenRoomWithChats_whenGetLastMessageSender_thenLastMessageSenderReturned() {
        // Given
        Room room = new Room();
        UserModel sender = UserModel.builder().email("test@ucll.be").build();
        Chat chat1 = Chat.builder().sender(sender).build();
        Chat chat2 = Chat.builder().sender(sender).build();
        room.setChats(Arrays.asList(chat1, chat2));

        // When
        String lastMessageSender = room.getLastMessageSender();

        // Then
        assertEquals("test@ucll.be", lastMessageSender);
    }

    @Test
    void givenEmptyRoom_whenGetLastMessageTimestamp_thenEmptyStringReturned() {
        // Given
        Room room = new Room();

        // When
        String lastMessageTimestamp = room.getLastMessageTimestamp();

        // Then
        assertEquals("", lastMessageTimestamp);
    }

    @Test
    void givenRoomWithChats_whenGetLastMessageTimestamp_thenLastMessageTimestampReturned() {
        // Given
        Room room = new Room();
        LocalDateTime timestamp = LocalDateTime.now();
        Chat chat1 = Chat.builder().timestamp(timestamp.minusMinutes(5)).build();
        Chat chat2 = Chat.builder().timestamp(timestamp).build();
        room.setChats(Arrays.asList(chat1, chat2));

        // When
        String lastMessageTimestamp = room.getLastMessageTimestamp();

        // Then
        assertEquals(timestamp.toString(), lastMessageTimestamp);
    }

}
