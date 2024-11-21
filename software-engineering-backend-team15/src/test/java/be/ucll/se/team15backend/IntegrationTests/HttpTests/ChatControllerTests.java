package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.chats.controller.ChatController;
import be.ucll.se.team15backend.chats.model.ChatResponse;
import be.ucll.se.team15backend.chats.model.RoomOverview;
import be.ucll.se.team15backend.chats.model.SendRequest;
import be.ucll.se.team15backend.chats.service.ChatService;
import be.ucll.se.team15backend.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTests {

    private static final String secretKey = "4A404E635266556A586E327234753778214125442A472D4B6150645367566B59";


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ChatService chatService;

    @Test
    public void testGetRoomOverview() throws Exception {
        // Mock behavior of JwtService to return email
        String token = "mockToken";
        String email = "test@example.com";
                @SuppressWarnings("deprecation")
                String jwt = Jwts.builder()
                .setSubject("test@example.com")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        when(jwtService.extractEmail(token)).thenReturn(email);

        // Mock behavior of ChatService
        when(chatService.getOverview(email)).thenReturn(List.of(new RoomOverview()));

        // Perform request with valid JWT token
        mockMvc.perform(get("/chat/overview")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSendMessage() throws Exception {
        // Mock behavior
        String token = "mockToken";
        SendRequest sendRequest = new SendRequest("roomId", "message", "localId");
        String email = "test@example.com";
        when(jwtService.extractEmail(token)).thenReturn(email);

        mockMvc.perform(post("/chat/send")
                        .header("Authorization", "Bearer " + token)
                        .content(String.valueOf(sendRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    @Test
    public void testGetFirstMessages() throws Exception {
        // Mock behavior
        String token = "mockToken";
        String email = "test@example.com";
        String roomId = "roomId";
        Integer amount = 10;
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(chatService.getFirstMessage(email, roomId, amount)).thenReturn(List.of(new ChatResponse()));

        mockMvc.perform(get("/chat/first")
                        .header("Authorization", "Bearer " + token)
                        .param("roomId", roomId)
                        .param("amount", String.valueOf(amount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testGetNextMessages() throws Exception {
        // Mock behavior
        String token = "mockToken";
        String email = "test@example.com";
        String roomId = "roomId";
        Integer amount = 10;
        String startId = "startId";
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(chatService.getNextMessages(email, roomId, amount, startId)).thenReturn(List.of(new ChatResponse()));

        mockMvc.perform(get("/chat/next")
                        .header("Authorization", "Bearer " + token)
                        .param("roomId", roomId)
                        .param("amount", String.valueOf(amount))
                        .param("startId", startId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
    @Test
    public void testIsIdAvailable() throws Exception {
        // Mock behavior
        String token = "mockToken";
        String chatId = "chatId";
        when(chatService.isIdAvailable(chatId)).thenReturn(true);

        mockMvc.perform(get("/chat/available")
                        .header("Authorization", "Bearer " + token)
                        .param("chatId", chatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(jsonPath("$").value(true));
    }
    @Test
    public void testGetRoomEmails() throws Exception {
        // Mock behavior
        String token = "mockToken";
        String roomId = "roomId";
        String email = "test@example.com";
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(chatService.getRoomEmails(roomId, email)).thenReturn(List.of("email1", "email2"));

        mockMvc.perform(get("/chat/room/{roomId}/emails", roomId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(jsonPath("$[0]").value("email1"))
                // .andExpect(jsonPath("$[1]").value("email2"));
    }
    @Test
    public void testReadMessages() throws Exception {
        // Mock behavior
        String token = "mockToken";
        String roomId = "roomId";
        String email = "test@example.com";
        when(jwtService.extractEmail(token)).thenReturn(email);

        mockMvc.perform(put("/chat/read")
                        .header("Authorization", "Bearer " + token)
                        .param("roomId", roomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
