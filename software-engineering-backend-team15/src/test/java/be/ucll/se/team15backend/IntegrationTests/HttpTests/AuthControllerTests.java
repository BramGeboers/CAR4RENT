package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.authentication.controller.AuthController;
import be.ucll.se.team15backend.authentication.model.LoginRequest;
import be.ucll.se.team15backend.authentication.model.LoginResponse;
import be.ucll.se.team15backend.authentication.model.RegisterRequest;
import be.ucll.se.team15backend.authentication.model.RegisterResponse;
import be.ucll.se.team15backend.authentication.service.AuthService;
import be.ucll.se.team15backend.security.JwtService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AuthService authService;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JwtService jwtService;

        private LoginRequest loginRequest;
        private RegisterRequest registerRequest;

        @MockBean
        private UserService userService;

        @BeforeEach
        public void setUp() {
                loginRequest = new LoginRequest("user1@example.com", "password1");
                // registerRequest = RegisterRequest.builder()
                // .email("user1@example.com")
                // .password("password1")
                // .role("renter")
                // .build();
        }

        @Test
        public void testLogin() throws Exception {
                LoginResponse loginResponse = new LoginResponse("user1@example.com", "token", Role.RENTER, 1L);

                when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

                mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(loginResponse.getEmail()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(loginResponse.getToken()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.role")
                                                .value(loginResponse.getRole().name()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(loginResponse.getId()));
        }

        @Test
        public void testRegister2() throws Exception {
                RegisterResponse registerResponse = RegisterResponse.builder()
                                .user(UserModel.builder()
                                                .email("user1@example.com")
                                                .role(Role.RENTER)
                                                .build())
                                .token("token")
                                .build();

                // Arrange
                String userJson = "{\"email\":\"loveleen@ucll.be\", \"password\": \"loveleen\", \"role\": \"owner\"}";

                // Act
                ResultActions result = mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJson));

                // Assert
                result.andExpect(status().isOk());
        }


}
