package be.ucll.se.team15backend.unit.RentTests;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rent.model.CheckInRequest;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.rent.repo.RentRepository;
import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.rent.service.RentService;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

import org.h2.engine.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentServiceTests {

    @Mock
    private RentRepository rentRepository;

    @InjectMocks
    private RentService rentService;

    @Mock
    private NotificationService notificationService;

@Mock
private UserService userService;



    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        UserModel userModel = new UserModel();
        userModel.setEmail("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(userModel);

    }

    private Rent createMockRent() {
        Rent rent = mock(Rent.class);
        Rental rental = new Rental();
        rental.setStartDate(LocalDateTime.of(LocalDate.of(2024, 4, 1), LocalDateTime.MIN.toLocalTime()));
        rental.setEndDate(LocalDateTime.of(LocalDate.of(2024, 4, 7), LocalDateTime.MIN.toLocalTime()));
        Car car = new Car();

        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setLicensePlate("ABC123");

        UserModel owner = new UserModel();
        owner.setEmail("test@example.com");
        car.setOwner(owner);
//        rent.setCar(car);
//        rent.setRenter(owner);
        when(rent.getRental()).thenReturn(rental);
        when(rent.getCar()).thenReturn(car);
        when(rent.getRenter()).thenReturn(owner);
//        when(rent.getRenter().getEmail()).thenReturn("test@example.com");
//        when(rent.getEmail()).thenReturn("test@example.com");
        return rent;
    }


    @Test
    public void givenNewRent_whenAddRent_thenRentIsAdded() {
        // Given
        Rent rent = new Rent();
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        Rent addedRent = rentService.addRent(rent);

        // Then
        assertEquals(rent, addedRent);
    }








//    @Test
//    public void givenRentsByEmail_whenSearchRentsByEmail_thenCorrectRentsReturned() throws ServiceException {
//        // Given
//        String email = "test@example.com";
//        Rent rent1 = new Rent();
//        rent1.setEmail(email);
//        Rent rent2 = new Rent();
//        rent2.setEmail(email);
//        when(rentRepository.findAllBy()).thenReturn(List.of(rent1, rent2));
//
//        // When
//        List<Rent> rents = rentService.searchRentsByEmail(email);
//
//        // Then
//        assertEquals(2, rents.size());
//    }

    @Test
    public void givenRent_whenCancelRentRequest_thenRentIsCancelled() {
        // Given
        Rent rent = new Rent();
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        when(rentRepository.findRentById(1L)).thenReturn(rent);

        // When
        Rent canceledRent = rentService.cancelRentRequest(1L);

        // Then
        assertNull(canceledRent.getCar());
    }

    // TO DO: CHECK IN & OUT TEST
    @Test
    public void givenRent_whenCheckIn_thenRentIsCheckedIn() throws ServiceException {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("blabla@gmail.com");
        rent.setRenter(renter);
        when(rentRepository.findRentById(1L)).thenReturn(rent);
        when(rentRepository.save(rent)).thenReturn(rent);
   
        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);
   
        Rent checkedInRent = rentService.checkIn(checkInRequest);

        // Then
        assertEquals(1000, checkedInRent.getStartMileage());
        assertEquals(50, checkedInRent.getStartFuel());
    }
    
    


    @Test
    public void givenRent_whenCheckOut_thenRentIsCheckedOut() throws ServiceException {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("email@test.com");
        rent.setRenter(renter);
        when(rentRepository.findRentById(1L)).thenReturn(rent);
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);

        checkInRequest.setRentId(1L);
        rentService.checkIn(checkInRequest);
        Rent checkedOutRent = rentService.checkOut(checkInRequest);

        // Then
        assertEquals(1000, checkedOutRent.getEndMileage());
        assertEquals(50, checkedOutRent.getEndFuel());
    }

    //unhappy cases
    @Test
    public void givenRent_whenCheckInInvalid_thenExceptionIsThrown() {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("test@email.com");

        rent.setRenter(renter);
        when(rentRepository.findRentById(1L)).thenReturn(null);
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);

        // Then
        assertThrows(ServiceException.class, () -> rentService.checkIn(checkInRequest));
    }

    @Test
    public void givenRent_whenCheckOutInvalid_thenExceptionIsThrown() {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("tst@gmail.com");
        rent.setRenter(renter);
        when(rentRepository.findRentById(1L)).thenReturn(null);
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);

        // Then
        assertThrows(ServiceException.class, () -> rentService.checkOut(checkInRequest));
    }

    @Test
    public void givenRent_whenCheckStartDateNotNull_thenExceptionIsThrown() {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("ttt@ttt.com");
        rent.setRenter(renter);
        rent.setStartDate(LocalDateTime.now().plusDays(1));
        when(rentRepository.findRentById(1L)).thenReturn(rent);
        when(rentRepository.save(rent)).thenReturn(rent);
        when(rentRepository.findRentById(1L)).thenReturn(rent);
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);
        


        // Then
        assertThrows(ServiceException.class, () -> rentService.checkIn(checkInRequest));
    }

    @Test
    public void givenRent_whenCheckEndDateNotNull_thenExceptionIsThrown() {
        // Given
        Rent rent = new Rent();
        rent.setActive(true);
        rent.setId(1L);
        Car car = new Car();
        car.setId(1L);
        rent.setCar(car);
        UserModel renter = new UserModel();
        renter.setEmail("dsfsdf@dsfsd.c");
        rent.setRenter(renter);
        rent.setEndDate(LocalDateTime.now().plusDays(1));
        when(rentRepository.findRentById(1L)).thenReturn(rent);
        when(rentRepository.save(rent)).thenReturn(rent);

        // When
        CheckInRequest checkInRequest = new CheckInRequest();
        checkInRequest.setRentId(1L);
        checkInRequest.setMileage(1000);
        checkInRequest.setFuelLevel(50);

        // Then
        assertThrows(ServiceException.class, () -> rentService.checkOut(checkInRequest));
    }

}

