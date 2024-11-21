package be.ucll.se.team15backend.unit.MapTests;

import be.ucll.se.team15backend.map.model.Address;
import be.ucll.se.team15backend.map.model.ClosestsResponse;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.model.MapException;
import be.ucll.se.team15backend.map.service.MapService;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MapServiceTests {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private MapService mapService;

    private MapService mockedMapService = Mockito.mock(MapService.class);

    @Test
    void fetchCoordinates_ValidAddress_ReturnsCoordinates() throws IOException, InterruptedException, MapException {
        // // Arrange
        // String addressString = "Eiffel Tower";
        // Coordinates expectedCoordinates = new Coordinates(48.8584, 2.2945); //
        // Example coordinates
        //
        // when(mockedMapService.fetchCoordinates(addressString)).thenReturn(expectedCoordinates);
        //
        // Coordinates actualCoordinates = mapService.fetchCoordinates(addressString);
        //
        // assertNotNull(actualCoordinates);
        // assertEquals(expectedCoordinates.getLatitude(),
        // actualCoordinates.getLatitude());
        // assertEquals(expectedCoordinates.getLongitude(),
        // actualCoordinates.getLongitude());
    }

    // Too many requests

    @Test
    void fetchAddress_ValidCoordinates_ReturnsAddress() throws IOException, MapException {
        // Arrange
        Coordinates coordinates = new Coordinates(48.8584, 2.2945); // Example coordinates
        Address expectedAddress = new Address("Quai Jacques Chirac", "5", "75007", null, "France", "Eiffel Tower");

        when(mockedMapService.fetchAddress(coordinates)).thenReturn(expectedAddress);

        Address actualAddress = mapService.fetchAddress(coordinates);

        assertNotNull(actualAddress);
        assertEquals(expectedAddress.getStreet(), actualAddress.getStreet());
        assertEquals(expectedAddress.getCity(), actualAddress.getCity());
        assertEquals(expectedAddress.getCountry(), actualAddress.getCountry());
        assertEquals(expectedAddress.getPostalCode(), actualAddress.getPostalCode());
    }

    @Test
    void getClosestRentals_ValidCoordinates_ReturnsClosestRentals() {
        // Arrange
        double latitude = 51.5074;
        double longitude = 0.1278;
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(latitude, longitude);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    void getClosestRentals_InvalidCoordinates_ReturnsEmptyList() {
        // Arrange
        double latitude = 0;
        double longitude = 0;
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(latitude, longitude);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    void getClosestRentals_NullCoordinates_ReturnsEmptyList() {
        // Arrange
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(0, 0);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    void getClosestRentals_EmptyRentals_ReturnsEmptyList() {
        // Arrange
        double latitude = 51.5074;
        double longitude = 0.1278;
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(latitude, longitude);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    void getClosestRentals_NullLatitude_ReturnsEmptyList() {
        // Arrange
        double longitude = 0.1278;
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(0, longitude);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }

    @Test
    void getClosestRentals_NullLongitude_ReturnsEmptyList() {
        // Arrange
        double latitude = 51.5074;
        List<ClosestsResponse> expectedResponse = new ArrayList<>();

        when(rentalRepository.findAll()).thenReturn(new ArrayList<>());

        List<ClosestsResponse> actualResponse = mapService.getClosestRentals(latitude, 0);
        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.size(), actualResponse.size());
    }
}
