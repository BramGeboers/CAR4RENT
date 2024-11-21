package be.ucll.se.team15backend.rental.service;

import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.map.model.Address;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.service.MapService;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rental.model.Rental;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.user.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.ucll.se.team15backend.car.repo.CarRepository;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    UserService userService;

    @Autowired
    private MapService mapService;

    public Page<Rental> getAllRentals(Pageable pageable) {

        return rentalRepository.findRentalsByRentIsNull(pageable);
    }

    public Rental addRental(Rental newRental, long carId) throws ServiceException, IOException, InterruptedException {
        if (newRental == null) {
            throw new ServiceException("rental", "Rental cannot be empty.");
        }

//        if (newRental.getEmail() != null) {
//            if (userService.getUserByEmail(newRental.getEmail()) == null) {
//                throw new ServiceException("user", "This email is not connected to a user.");
//            }
//        }
        Car car = carService.getCarById(carId);
        if (car == null) {
            throw new ServiceException("car", "Car id cannot be empty.");
        }

//        List<Rental> existingRentals = car.getRentals();
//        for (Rental existingRental : existingRentals) {
//            if (datesOverlap(existingRental.getStartDate(), existingRental.getEndDate(), newRental.getStartDate(),
//                    newRental.getEndDate())) {
//                throw new ServiceException("rental", "New rental dates overlap with existing rentals.");
//            }
//        }

        if (newRental.getLatitude() == 0.0 && newRental.getLongitude() == 0.0) { // skip during fill to save time and
                                                                                 // requests
            Address rentalAddress = Address.builder()
                    .city(newRental.getCity())
                    .street(newRental.getStreet())
                    .number(newRental.getNumber())
                    .postalCode(newRental.getPostal())
                    .build();

            System.out.println("Address: " + rentalAddress.toString());
            Coordinates coordinates = null;
            try {
                coordinates = mapService.fetchCoordinates(rentalAddress);
            } catch (Exception e) {
                System.out.println("error");

            }

            if (coordinates == null) {
                System.out.println(rentalAddress);
                throw new ServiceException("rental", "Invalid address.");
            }
            newRental.setLatitude(coordinates.getLatitude());
            newRental.setLongitude(coordinates.getLongitude());

        }
        car.setRental(newRental);

        newRental.setCar(car);
        Rental returnRental = rentalRepository.save(newRental);
        carRepository.save(car);
        return returnRental;
    }

    private boolean datesOverlap(LocalDateTime startDate1, LocalDateTime endDate1, ChronoLocalDateTime<?> startDate2,
            ChronoLocalDateTime<?> endDate2) {
        return !endDate1.isBefore(startDate2) && !startDate1.isAfter(endDate2);
    }

    public Rental cancelRental(long rentalId, String email) throws ServiceException {

        Rental rental = getRentalById(rentalId);

        if (rental == null) {
            throw new ServiceException("error", "Rental does not exist.");
        }

        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new ServiceException("error", "User does not exist.");
        }

        if (!rental.getCar().getOwner().equals(user)) {
            throw new ServiceException("error", "You are not the owner of this rental.");
        }

        if (rental.getRent() != null) {
            throw new ServiceException("error", "Rental is currently rented out.");
        }

        Car car = rental.getCar();

        car.removeRental(rental);
        carRepository.save(car);



        rentalRepository.delete(rental);

        String message = "Rental with ID " + rental.getId() + " has been cancelled";
        List<String> emails = new ArrayList<>();
        emails.add(rental.getCar().getOwner().getEmail());
        Notification notification = new Notification(0L, message, rental.getCar(), null, emails);

        notificationService.createNotification(notification, rental.getCar().getId());

        return rental;
    }

    public Rental getRentalById(long id) {
        return rentalRepository.findById(id);
    }

    public Page<Rental> searchRental(LocalDateTime startDate,LocalDateTime endDate,String City,String Street,String Postal,Pageable pageable,String email){
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return rentalRepository.searchRentals(startDate,endDate,City,Street,Postal,pageable);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceExceptions(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
