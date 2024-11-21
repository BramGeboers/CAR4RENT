package be.ucll.se.team15backend.unit.RentalTests;

import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.user.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.rental.service.RentalService;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.map.service.MapService;
import static org.mockito.ArgumentMatchers.any;


public class RentalServiceTests {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MapService mapService;

    @InjectMocks
    private RentalService rentalService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Rental rental = new Rental();
        rental.setId(1L);
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(1));

        Car car = new Car();
        car.setId(1L);
        car.setRentals(new ArrayList<>());
    }

/*    @Test
    public void givenValidRentalAndCarId_whenAddRental_thenRentalIsAddedSuccessfully() throws ServiceException, IOException, InterruptedException {
        // Given
        long carId = 1L;
        Car car = new Car();
        Rental rental = new Rental();
        when(carService.getCarById(carId)).thenReturn(car);
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        // When
        Rental result = rentalService.addRental(rental, carId);

        // Then
        assertNotNull(result);
        assertEquals(rental.getId(), result.getId());
        assertEquals(car.getId(), result.getCar().getId());
    }*/



    @Test
    public void givenOverlappingRentalDates_whenAddingRental_thenServiceExceptionThrown() throws ServiceException, IOException, InterruptedException {
        // Given
        Rental rental = new Rental();
        rental.setStartDate(LocalDateTime.now());
        rental.setEndDate(LocalDateTime.now().plusDays(1));
        Car car = new Car();
        car.setId(1L);
        List<Rental> existingRentals = new ArrayList<>();
        existingRentals.add(rental);
        car.setRentals(existingRentals);

        when(carService.getCarById(anyLong())).thenReturn(car);

        // When/Then
        assertThrows(ServiceException.class, () -> rentalService.addRental(rental, 1L));
    }



//    @Test
//    public void givenExistingRentalId_whenCancelingRental_thenRentalIsCanceled() throws ServiceException {
//        // Given
//        long rentalId = 1L;
//        Rental rental = new Rental();
//        rental.setId(rentalId);
//        Car car = new Car();
//        rental.setCar(car);
//        UserModel user = new UserModel();
//        user.setEmail("aaa");
//        car.setOwner(user);
//
//        when(rentalRepository.findById(rentalId)).thenReturn(rental);
//
//
//        // When
//        Rental canceledRental = rentalService.cancelRental(rentalId, "email");
//
//        // Then
//        assertNotNull(canceledRental);
//        assertEquals(rentalId, canceledRental.getId());
//        verify(rentalRepository, times(1)).delete(rental);
//        verify(notificationService, times(1)).createNotification(any(Notification.class), eq(car.getId()));
//    }
//
//    @Test
//    public void givenNonExistingRentalId_whenCancelingRental_thenServiceExceptionThrown() {
//        // Given
//        long rentalId = 1L;
//
//        when(rentalRepository.findById(rentalId)).thenReturn(null);
//
//        // When / Then
//        assertThrows(ServiceException.class, () -> rentalService.cancelRental(rentalId, "email"));
//        verify(rentalRepository, never()).delete(any(Rental.class));
//        verify(notificationService, never()).createNotification(any(Notification.class), anyLong());
//    }

    @Test
    public void givenExistingRentalId_whenGettingRentalById_thenRentalIsReturned() {
        // Given
        long rentalId = 1L;
        Rental rental = new Rental();
        rental.setId(rentalId);

        when(rentalRepository.findById(rentalId)).thenReturn(rental);

        // When
        Rental retrievedRental = rentalService.getRentalById(rentalId);

        // Then
        assertNotNull(retrievedRental);
        assertEquals(rentalId, retrievedRental.getId());
    }

    @Test
    public void givenNonExistingRentalId_whenGettingRentalById_thenNullIsReturned() {
        // Given
        long rentalId = 1L;

        when(rentalRepository.findById(rentalId)).thenReturn(null);

        // When
        Rental retrievedRental = rentalService.getRentalById(rentalId);

        // Then
        assertNull(retrievedRental);
    }
}
