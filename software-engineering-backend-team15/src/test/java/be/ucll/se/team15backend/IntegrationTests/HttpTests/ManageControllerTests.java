package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.manage.controller.ManageController;
import be.ucll.se.team15backend.manage.model.ManageException;
import be.ucll.se.team15backend.manage.service.ManageService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserException;
import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@WebMvcTest(ManageController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ManageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ManageService manageService;

    @Test
    public void testUpdateRole() throws Exception {
        Long userId = 1L;
        String role = "ADMIN";

        UserModel userModel = new UserModel();
        userModel.setUserId(userId);
        userModel.setRole(Role.ADMIN);
        Mockito.when(manageService.updateRole(Mockito.anyLong(), Mockito.anyString())).thenReturn(userModel);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/manage/role/update")
                        .param("userId", String.valueOf(userId))
                        .param("role", role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
                // .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                // .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(userModel)));
    }
}
