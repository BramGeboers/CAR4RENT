package be.ucll.se.team15backend.unit.BotTests;

import be.ucll.se.team15backend.bot.model.BotException;
import be.ucll.se.team15backend.bot.model.BotResponse;
import be.ucll.se.team15backend.bot.service.BotService;
import be.ucll.se.team15backend.chats.model.Chat;
import be.ucll.se.team15backend.chats.model.ChatException;
import be.ucll.se.team15backend.chats.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class BotServiceTests {

//    @Mock
//    private ChatService chatService;
//
//    @InjectMocks
//    private BotService botService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testMain() throws IOException, BotException, ChatException {
//        // Given
//        String message = "Test message";
//        String email = "boxter.buddy@ucll.be";
//        String roomId = "room123";
//        String response = "Bot response";
//
//        when(chatService.sendMessage(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(new Chat());
//
//        // When
//        BotResponse botResponse = botService.main(message, email, roomId);
//
//        // Then
//        assertEquals(response, botResponse.getData());
//        verify(chatService, times(1)).sendMessage(eq(message), eq(email), eq(roomId), anyString());
//    }
//
//    @Test
//    void testAskBot() throws IOException, BotException, ChatException {
//        // Given
//        String message = "Test message";
//        String email = "test@example.com";
//        String roomId = "room123";
//        String response = "Bot response";
//
//        // Mocking chatService.emailInRoom to return true
//        when(chatService.emailInRoom(eq(email), eq(roomId))).thenReturn(true);
//
//        // Mocking chatService.sendMessage to return a new Chat object
//        when(chatService.sendMessage(anyString(), anyString(), anyString(), anyString())).thenReturn(new Chat());
//
//        // When
//        String botResponse = botService.askBot(message, email, roomId);
//
//        // Then
//        assertEquals(response, botResponse);
//        verify(chatService, times(1)).emailInRoom(eq(email), eq(roomId));
//        verify(chatService, times(1)).sendMessage(anyString(), anyString(), anyString(), anyString());
//    }


}
