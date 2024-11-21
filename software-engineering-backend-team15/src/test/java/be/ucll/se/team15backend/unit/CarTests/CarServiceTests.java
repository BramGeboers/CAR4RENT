package be.ucll.se.team15backend.unit.CarTests;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarServiceTests {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService service;

    @Mock
    private UserService userService;

//    private Car aCarToyotaCamry = new Car(1L, "Toyota", "Camry", "Sedan", "ABC123", 5, 2, true, false, null, 50000.0, 50.0, 60.0, 6.5, 0.2, 1.5);
    private Car aCarToyotaCamry = Car.builder()
        .id(1L)
        .build();
//    private Car aFordFiesta = new Car(2L, "Ford", "Fiesta", "Hatchback", "XYZ789", 4, 1, false, true, null, 30000.0, 40.0, 45.0, 5.5, 0.18, 1.2);

    private Car aFordFiesta = Car.builder()
        .id(2L)
        .build();

    private List<Car> giveListWithCars() {
        List<Car> cars = new ArrayList<>();
        cars.add(aCarToyotaCamry);
        cars.add(aFordFiesta);
        return cars;
    }

    @Test
    public void givenCars_whenGetAllCars_theListOfCarsCarsIsReturned() {
        // given
        List<Car> expectedCars = giveListWithCars();
        when(carRepository.findAllBy()).thenReturn(expectedCars);

        // when
        List<Car> result = service.getAllCars();

        // then
        assertEquals(expectedCars, result);
    }
    @Test
    public void givenNewCar_whenAddCar_thenCarIsAdded() {
        // given
//        Car newCar = new Car(3L, "Honda", "Civic", "Sedan", "DEF456", 5, 2, true, false, null, 45000.0, 50.0, 60.0, 6.5, 0.2, 1.5);
        Car newCar = Car.builder()
            .id(3L)
            .build();
        when(carRepository.save(newCar)).thenReturn(newCar);
        when(userService.getUserByEmail("rein@ucll.be")).thenReturn(new UserModel());

        // when
        Car addedCar = service.addCar(newCar, "rein@ucll.be");

        // then
        assertEquals(newCar, addedCar);
    }


//    @Test
//    public void givenCarId_whenGetCarById_thenCarIsReturned() {
//        // given
//        long carId = 1L;
//        Car aCarToyotaCamry = new Car();
//        when(carRepository.findById(carId)).thenReturn(aCarToyotaCamry);
//
//        // when
//        Car foundCar = service.getCarById(carId);
//
//        // then
//        assertEquals(aCarToyotaCamry, foundCar);
//    }



    @Test
    public void givenCarId_whenGetCarByIdNotFound_thenExceptionIsThrown() {
        // given
        long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.getCarById(carId));
    }



    @Test
    public void givenCarId_whenDeleteCarByIdNotFound_thenExceptionIsThrown() {
        // given
        long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "aaa"));
    }


    //unhappy cases
    @Test
    public void givenCarId_whenDeleteCarByIdNotFound_thenExceptionIsThrown2() {
        // given
        long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "aaa"));
    }

    @Test
    public void givenNewCar_whenAddCarWithInvalidUser_thenExceptionIsThrown() {
        // given
        Car newCar = Car.builder()
            .id(3L)
            .build();
        when(userService.getUserByEmail("fakeUser@gmail.com")).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.addCar(newCar, "fakeUser@gmail.com"));
    }

    @Test
    public void givenCarId_whenGetCarByIdSecureNotFound_thenExceptionIsThrown() {
        // given
        long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.getCarByIdSecure(carId, "aaa"));

    }

    @Test
    public void givenCarId_whenGetCarByIdSecureNotFound_thenExceptionIsThrown2() {
        // given
        long carId = 999L;
        when(carRepository.findById
            (carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.getCarByIdSecure(carId, "aaa"));

    }


    @Test
    public void givenCarId_whenGetCarByIdNotFound_thenExceptionIsThrown2() {
        // given
        long carId = 999L;
        when(carRepository.findById
            (carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.getCarById(carId));
    }

    @Test
    public void givenCarId_whenDeleteCarByIdNotFound_thenExceptionIsThrown3() {
        // given
        long carId = 999L;
        when(carRepository.findById
            (carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "aaa"));
    }

   
    @Test
    public void givenInvalidOwner_whenDeleteCarById_thenExceptionIsThrown() {
        // given
        long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(aCarToyotaCamry);
        when(userService.getUserByEmail("blabla@gmail.com")).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "blabla@gmail.com"));
    }

    @Test
    public void givenInvalidCarId_whenDeleteCarById_thenExceptionIsThrown2() {
        // given
        long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "aaa"));
    }

    @Test
    public void givenInvalidOwner_whenDeleteCarById_thenExceptionIsThrown3() {
        // given
        long carId = 1L;
        when(carRepository.findById(carId)).thenReturn(aCarToyotaCamry);
        when(userService.getUserByEmail("blabla@gg.com")).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> service.deleteCar(carId, "blabla@gg.com"));
    }

    }
