package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.planner.controller.PlannerController;
import be.ucll.se.team15backend.planner.model.PlannerRequest;
import be.ucll.se.team15backend.planner.model.PlannerResponse;
import be.ucll.se.team15backend.planner.service.PlannerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(PlannerController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlannerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlannerService plannerService;

    @Autowired
    private ObjectMapper objectMapper;

@Test
public void testPlan() throws Exception {
    // Creating a PlannerRequest with specific latitude and longitude values
    Coordinates expectedStart = new Coordinates(37.7749, -122.4194);
    Coordinates expectedEnd = new Coordinates(34.0522, -118.2437);
    LocalDateTime expectedDate = LocalDateTime.parse("2024-04-22T10:30:00");

    // Mocking PlannerRequest object to return specific values
    PlannerRequest expectedRequest = new PlannerRequest();
    expectedRequest.setStart(expectedStart);
    expectedRequest.setEnd(expectedEnd);
    expectedRequest.setDate(expectedDate);

    PlannerResponse plannerResponse = new PlannerResponse();

    String planJson = "{\"start\": {\"latitude\": 37.7749, \"longitude\": -122.4194}, \"end\": {\"latitude\": 34.0522, \"longitude\": -118.2437}, \"date\": \"2024-04-22T10:30:00\"}";

    // Stubbing plannerService.plan() to return plannerResponse
    Mockito.when(plannerService.plan(expectedRequest))
            .thenReturn(Collections.singletonList(plannerResponse));

    // Performing the HTTP POST request with the provided JSON
    mockMvc.perform(post("/planner")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(planJson))
            .andExpect(status().isOk());

    // Verifying that plannerService.plan() is called with the expected PlannerRequest
    Mockito.verify(plannerService).plan(expectedRequest);
}

}

