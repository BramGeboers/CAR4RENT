package be.ucll.se.team15backend.unit.ChatTests;

import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.Room;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChatModelTests {

    @Test
    void testChatHappyCase() {
        // Given
        String id = "1";
        LocalDateTime timestamp = LocalDateTime.now();
        UserModel sender = new UserModel();
        sender.setUserId(1L); // Set appropriate property for UserModel
        Room room = new Room();
        room.setRoomId("roomId"); // Set appropriate property for Room
        String data = "Hello!";
        List<UserModel> readBy = new ArrayList<>();

        // When
        Chat chat = Chat.builder()
                .id(id)
                .timestamp(timestamp)
                .sender(sender)
                .room(room)
                .data(data)
                .readBy(readBy)
                .build();

        // Then
        assertNotNull(chat);
        assertEquals(id, chat.getId());
        assertEquals(timestamp, chat.getTimestamp());
        assertEquals(sender, chat.getSender());
        assertEquals(room, chat.getRoom());
        assertEquals(data, chat.getData());
        assertEquals(readBy, chat.getReadBy());
    }

}
