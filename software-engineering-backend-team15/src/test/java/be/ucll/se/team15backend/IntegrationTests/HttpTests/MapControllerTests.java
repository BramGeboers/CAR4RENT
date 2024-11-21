package be.ucll.se.team15backend.IntegrationTests.HttpTests;

import be.ucll.se.team15backend.map.controller.MapController;
        import be.ucll.se.team15backend.map.model.Address;
        import be.ucll.se.team15backend.map.model.ClosestsResponse;
        import be.ucll.se.team15backend.map.model.Coordinates;
        import be.ucll.se.team15backend.map.service.MapService;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.junit.jupiter.MockitoExtension;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
        import org.springframework.test.context.junit.jupiter.SpringExtension;
        import org.springframework.test.web.servlet.MockMvc;
        import org.springframework.test.web.servlet.setup.MockMvcBuilders;

        import java.util.Collections;

        import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.when;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(MapController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MapControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MapService mapService;

    @InjectMocks
    private MapController mapController;

    @Test
    public void testGetCoordinates() throws Exception {
        // Prepare test data
        String address = "123 Main St";
        Coordinates coordinates = new Coordinates(55.9207481, -3.3842856);

        // Mock the service response
        when(mapService.fetchCoordinates(anyString())).thenReturn(coordinates);

        // Perform the GET request
        mockMvc.perform(get("/map/coordinates?address=123%20Main%20St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.latitude").value(coordinates.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(coordinates.getLongitude()));
    }

}
