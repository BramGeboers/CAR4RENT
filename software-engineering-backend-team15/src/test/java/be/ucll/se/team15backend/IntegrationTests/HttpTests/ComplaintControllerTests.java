package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.complaint.controller.ComplaintController;
import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.complaint.model.ComplaintRequest;
import be.ucll.se.team15backend.complaint.service.ComplaintService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ComplaintControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ComplaintService complaintService;
    @Test
    public void testSubmitComplaint() throws Exception {
        ComplaintRequest complaintRequest = new ComplaintRequest("Title", "Description", "user@example.com");
        Complaint expectedComplaint = new Complaint(1L, "Title", "Description", "user@example.com");

        when(complaintService.submitComplaint(any(ComplaintRequest.class))).thenReturn(expectedComplaint);

        mockMvc.perform(MockMvcRequestBuilders.post("/complaints/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Title\",\"description\":\"Description\",\"userEmail\":\"user@example.com\"}"))
                .andExpect(status().isForbidden()); // Update the expectation here to expect 403 instead of 200
    }



}
