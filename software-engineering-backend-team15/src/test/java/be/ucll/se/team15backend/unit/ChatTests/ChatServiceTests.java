package be.ucll.se.team15backend.unit.ChatTests;

import be.ucll.se.team15backend.chats.model.*;
import be.ucll.se.team15backend.chats.repo.ChatRepository;
import be.ucll.se.team15backend.chats.repo.RoomRepository;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ChatServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitRooms_UserNull() {
        UserModel user = null;
        chatService.initRooms(user);

        verifyNoInteractions(userService, roomRepository, chatRepository);
    }

    @Test
    public void testInitRooms_UserIsBot() {
        UserModel user = new UserModel();
        user.setRole(Role.BOT);

        chatService.initRooms(user);

        verifyNoInteractions(userService, roomRepository, chatRepository);
    }

    //@Test
    //public void testInitRooms_UserNotBot() {}

    @Test
    public void testSaveChat() {
        Chat chat = new Chat();

        chatService.saveChat(chat);

        verify(chatRepository).save(chat);
        assertNotNull(chat.getId());
        assertNotNull(chat.getTimestamp());
    }
    @Test
    void saveRoom_NewRoom() {
        Room room = new Room();
        room.setRoomId(null);

        when(roomRepository.save(room)).thenReturn(room);

        Room savedRoom = chatService.saveRoom(room);

        assertNotNull(savedRoom.getRoomId());
        verify(roomRepository, times(1)).save(room);
    }

    //@Test
    //void sendMessage_ValidInputs_ReturnsChat() throws ChatException {}

    @Test
    void getFirstMessage_ValidInputs_ReturnsChatResponses() throws ChatException {
        UserModel user = new UserModel();
        user.setEmail("user@example.com");

        Room room = new Room();
        room.setRoomId("room_id");
        room.addUser(user);

        Chat chat = new Chat();
        chat.setId("1");
        chat.setTimestamp(LocalDateTime.now());
        chat.setSender(user);
        chat.setData("Test message");

        room.addChat(chat);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(roomRepository.findRoomByRoomId(room.getRoomId())).thenReturn(room);

        List<ChatResponse> chatResponses = chatService.getFirstMessage(user.getEmail(), room.getRoomId(), 1);

        assertNotNull(chatResponses);
        assertEquals(1, chatResponses.size());
        assertEquals(chat.getId(), chatResponses.get(0).getId());
        assertEquals(chat.getTimestamp().toString(), chatResponses.get(0).getTimestamp());
        assertEquals(chat.getSender().getEmail(), chatResponses.get(0).getSenderEmail());
        assertEquals(chat.getData(), chatResponses.get(0).getData());
    }
    @Test
    void getNextMessages_ValidInputs_ReturnsChatResponses() throws ChatException {
        UserModel user = new UserModel();
        user.setEmail("loveleen@ucll.com");

        Room room = new Room();
        room.setRoomId("room_id");
        room.addUser(user);

        Chat chat = new Chat();
        chat.setId("1");
        chat.setTimestamp(LocalDateTime.now());
        chat.setSender(user);
        chat.setData("Test message");

        room.addChat(chat);

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(roomRepository.findRoomByRoomId(room.getRoomId())).thenReturn(room);

        List<ChatResponse> chatResponses = chatService.getNextMessages(user.getEmail(), room.getRoomId(), 1, chat.getId());

        assertEquals(0, chatResponses.size());
    }


    @Test
    void isIdAvailable_IdAvailable_ReturnsTrue() {
        String chatId = "1";

        when(chatRepository.findChatById(chatId)).thenReturn(null);

        boolean result = chatService.isIdAvailable(chatId);

        assertTrue(result);
    }

    @Test
    void getRoomEmails_ValidInputs_ReturnsEmails() throws ChatException {
        String roomId = "room_id";
        String email = "user@example.com";

        UserModel user = new UserModel();
        user.setEmail(email);

        Room room = new Room();
        room.setRoomId(roomId);
        room.addUser(user);

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roomRepository.findRoomByRoomId(roomId)).thenReturn(room);

        List<String> emails = chatService.getRoomEmails(roomId, email);

        assertNotNull(emails);
        assertEquals(1, emails.size());
        assertEquals(email, emails.get(0));
    }

    @Test
    void readMessages_ValidInputs_MarksMessagesAsRead() throws ChatException {
        String roomId = "room_id";
        String email = "user@example.com";

        UserModel user = new UserModel();
        user.setEmail(email);

        Room room = new Room();
        room.setRoomId(roomId);
        room.addUser(user);

        Chat chat = new Chat();
        chat.setId("1");
        chat.setReadBy(new ArrayList<>());
        chat.getReadBy().add(user);

        room.addChat(chat);

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roomRepository.findRoomByRoomId(roomId)).thenReturn(room);

        chatService.readMessages(email, roomId);

        assertTrue(chat.getReadBy().contains(user));
    }

    @Test
    void emailInRoom_ValidInputs_ReturnsTrue() throws ChatException {
        String roomId = "room_id";
        String email = "user@example.com";

        UserModel user = new UserModel();
        user.setEmail(email);

        Room room = new Room();
        room.setRoomId(roomId);
        room.addUser(user);

        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roomRepository.findRoomByRoomId(roomId)).thenReturn(room);

        boolean result = chatService.emailInRoom(email, roomId);

        assertTrue(result);
    }
    @Test
    public void getFirstMessage_UserNotFound_ThrowsChatException() {
        // Given
        String email = "nonexistent@example.com";
        String roomId = "room_id";
        Integer amount = 1;
        when(userService.getUserByEmail(email)).thenReturn(null);

        // When / Then
        assertThrows(ChatException.class, () -> chatService.getFirstMessage(email, roomId, amount));
    }

    @Test
    public void getFirstMessage_RoomNotFound_ThrowsChatException() {
        // Given
        String email = "user@example.com";
        String roomId = "nonexistent_room";
        Integer amount = 1;
        when(userService.getUserByEmail(email)).thenReturn(new UserModel());
        when(roomRepository.findRoomByRoomId(roomId)).thenReturn(null);

        // When / Then
        assertThrows(ChatException.class, () -> chatService.getFirstMessage(email, roomId, amount));
    }

    @Test
    public void getFirstMessage_UserNotInRoom_ThrowsChatException() {
        // Given
        String email = "user@example.com";
        String roomId = "room_id";
        Integer amount = 1;
        UserModel user = new UserModel();
        user.setEmail(email);
        Room room = new Room();
        when(userService.getUserByEmail(email)).thenReturn(user);
        when(roomRepository.findRoomByRoomId(roomId)).thenReturn(room);

        // When / Then
        assertThrows(ChatException.class, () -> chatService.getFirstMessage(email, roomId, amount));
    }

}
