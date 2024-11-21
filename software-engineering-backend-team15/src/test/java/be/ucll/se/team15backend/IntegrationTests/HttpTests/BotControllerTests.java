package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.authentication.controller.AuthController;
import be.ucll.se.team15backend.bot.model.BotResponse;
import be.ucll.se.team15backend.bot.service.BotService;
import be.ucll.se.team15backend.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.crypto.SecretKey;

//@WebMvcTest(AuthController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BotControllerTests {

    private static final String secretKey = "4A404E635266556A586E327234753778214125442A472D4B6150645367566B59";

    SecretKey secretKey2 = Keys.hmacShaKeyFor(secretKey.getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JwtService jwtService;

    @Mock
    private BotService botService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBot() throws Exception {
        // Mock JWT service behavior
        @SuppressWarnings("deprecation")
        String jwt = Jwts.builder()
                .setSubject("test@example.com")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        when(jwtService.extractEmail(anyString())).thenReturn("test@example.com");

        // Create a BotResponse instance and set the response message
        BotResponse botResponse = new BotResponse();

        // Mock Bot service behavior to return the BotResponse instance
        when(botService.main(anyString(), anyString(), anyString())).thenReturn(botResponse);

        // Perform GET request to /bot endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/bot"))
                // .header("Authorization", "Bearer " + jwt)
                // .param("message", "Test message")
                // .param("roomId", "123")
                // .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(jsonPath("$.message").value("Response from bot"));

        // Verify that JwtService and BotService methods were called with expected
        // arguments
        // verify(jwtService).extractEmail(anyString());
        // verify(botService).main(eq("Test message"), eq("test@example.com"), eq("123"));
    }
}
