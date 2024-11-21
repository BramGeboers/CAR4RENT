package be.ucll.se.team15backend.rent.service;

import be.ucll.se.team15backend.billing.model.Billing;
import be.ucll.se.team15backend.billing.repo.BillingRepository;
import be.ucll.se.team15backend.billing.service.BillingService;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.service.CarService;
import be.ucll.se.team15backend.customexception.ServiceException;
import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rent.model.CheckInRequest;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.rental.service.RentalService;
import be.ucll.se.team15backend.notification.repo.NotificationRepository;
import be.ucll.se.team15backend.rent.repo.RentRepository;

import be.ucll.se.team15backend.notification.service.NotificationService;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentService {
    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillingService billingService;

    public Rent addRent(Rent rent) {
        return rentRepository.save(rent);
    }

    public Rent payRent(Long id, String email) {
        Rent rent = getRentById(id);

        if (rent == null) {
            throw new RuntimeException("This rent does not exist");
        }

        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        rent.setActive(true);
        String message = "Rent" + rent.getRental().getStartDate() + " from "
                + rent.getRental().getEndDate() + " for car " + rent.getCar().getBrand() + " "
                + rent.getCar().getModel() + " with licenseplate " + rent.getCar().getLicensePlate()
                + " has been payed, Thank you!";

        List<String> emails = new ArrayList<>();
        emails.add(email);
        emails.add(rent.getRenter().getEmail());

        Double cost = (rent.getStartFuel() - rent.getEndFuel()) * 1.75 + 5.99
                + (rent.getEndMileage() - rent.getStartMileage()) * rent.getCar().getPricePerKm();

        Billing billing = new Billing(0L, cost, LocalDate.now(), rent.getRenter(), rent);

        billingService.createBilling(billing);

        billingRepository.save(billing);

        Notification notification = new Notification(0L, message, rent.getCar(), rent, emails);
        notificationService.createNotification(notification, rent.getCar().getId());

        rent.setPayed(true);

        return rentRepository.save(rent);
    }

    public Rent confirmRent(Long rentid, String email) {
        Rent rent = getRentById(rentid);
        if (rent == null) {
            throw new RuntimeException("Rent not found with id: " + rentid);
        }

        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (user.getRole() != Role.ADMIN && !rent.getCar().getOwner().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("You are not the owner of the car.");
        }

        rent.setActive(true);
        String message = "Request to rent from " + rent.getRental().getStartDate() + " until "
                + rent.getRental().getEndDate() + " for car " + rent.getCar().getBrand() + " "
                + rent.getCar().getModel() + " with licenseplate " + rent.getCar().getLicensePlate()
                + " has been confirmed";

        List<String> emails = new ArrayList<>();
        emails.add(email);
        emails.add(rent.getRenter().getEmail());

        Notification notification = new Notification(0L, message, rent.getCar(), rent, emails);
        notificationService.createNotification(notification, rent.getCar().getId());
        return rentRepository.save(rent);
    }

    public Page<Rent> showRent(Pageable pageable) {
        return rentRepository.findAll(pageable);
    }

    public Page<Rent> getRent(Pageable pageable, String email) {

        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (user.getRole().equals(Role.ADMIN)) {
            return rentRepository.findAll(pageable);
        }
        if (user.getRole().equals(Role.OWNER)) {
            return rentRepository.findRentByCarOwner(pageable, user);
        }
        if (user.getRole().equals(Role.RENTER)) {
            return rentRepository.findRentByRenterAndRentalNotNull(pageable, user);
        }
        throw new RuntimeException("User not found with email: " + email);
    }

    public Rent rentRequest(Rent rent, Long carId, Long rentalId, String email) throws ServiceException {
        UserModel renter = userService.getUserByEmail(email);
        if (renter == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        rent.setRenter(renter);

        Car car = carService.getCarById(carId);

        if (car == null) {
            throw new ServiceException("car", "There aren't any cars with the specified ID.");
        }
        Rental rental = rentalService.getRentalById(rentalId);

        if (rental == null) {
            throw new ServiceException("rental", "There aren't any rentals with the specified ID.");
        }
        rent.setCar(car);
        rent.setRental(rental);

        rentRepository.save(rent);

        List<String> emails = new ArrayList<>();
        emails.add(email);
        emails.add(rental.getCar().getOwner().getEmail());

        String message = "Request to rent " + rent.getId() + " from " + " until " + " for car " + car.getBrand() + " "
                + car.getModel() + " with licenseplate " + car.getLicensePlate();
        Notification notification = new Notification(0L, message, rent.getCar(), rent, emails);
        notificationService.createNotification(notification, rent.getCar().getId());

        return rent;
    }

    public Rent getRentById(Long id) {
        return rentRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    public List<Rent> searchRentsByEmail(String email) throws ServiceException {
        if (email == null) {
            throw new ServiceException("rent", "You need to choose one or more values to get search results");
        }
        List<Rent> allRents = rentRepository.findAllBy();
        return allRents.stream()
                .filter(rent -> rent.getRenter().getEmail().equals(email))
                .collect(Collectors.toList());
    }

    public Page<Rent> searchRent(LocalDateTime startDate, LocalDateTime endDate, String city, String licenseplate, String email,
            Pageable pageable, String emailOwner) {
        UserModel owner = userService.getUserByEmail(emailOwner);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return rentRepository.searchRents(startDate, endDate, city, licenseplate,email, pageable);
    }

    private Rent getRentById(long id) {
        return rentRepository.findRentById(id);
    }

    public Rent cancelRentRequest(long rentId) {

        Rent rent = getRentById(rentId);

        String email = "ditiseenoplossing";

        List<String> emails = new ArrayList<>();
        // emails.add(rent.getRenter().getEmail());

        List<Notification> notifications = notificationService.getAllNotifications(email);
        for (Notification n : notifications) {
            if (n.getRent() != null && n.getRent().getId() == rent.getId()) {
                // Delete the notification
                notificationRepository.delete(n);
            }
        }

        String message = "Rent request with ID " + rent.getId() + " has been denied.";

        Notification notification = new Notification(0L, message, rent.getCar(), null, emails);
        notificationService.createNotification(notification, rent.getCar().getId());

        rent.setCar(null);

        rentRepository.delete(rent);

        return rent;
    }

    public Rent cancelRent(long rentId) throws ServiceException {
        Rent rent = getRentById(rentId);

        if (rent == null) {
            throw new ServiceException("rent", "There aren't any rents with the specified ID.");
        }

        rent.setRental(null);

        String email = "ditiseenoplossing";

        String message = "Rent with ID " + rent.getId() + " has been cancelled.";

        List<String> emails = new ArrayList<>();
        emails.add(rent.getRenter().getEmail());

        Notification notification = new Notification(0L, message, rent.getCar(), null, emails);
        notificationService.createNotification(notification, rent.getCar().getId());

        // Loop through notifications
        List<Notification> notifications = notificationService.getAllNotifications(email);
        for (Notification n : notifications) {
            if (n.getRent() != null && n.getRent().getId() == rent.getId()) {
                // Delete the notification
                notificationRepository.delete(n);
            }
        }

        // Delete the rent
        rentRepository.delete(rent);

        return rent;
    }

    public Rent checkIn(CheckInRequest checkInRequest) throws ServiceException {
        Rent rent = getRentById(checkInRequest.getRentId());

        if (rent == null) {
            throw new ServiceException("rent", "There aren't any rents with the specified ID.");
        }

        if (!rent.getActive()) {
            throw new ServiceException("rent", "The rent is not active.");
        }

        if (rent.getStartDate() != null) {
            throw new ServiceException("rent", "The rent has already been checked in.");
        }

        // TODO: CHECK IF RENT IS FROM THE USER

        List<String> emails = new ArrayList<>();
        emails.add(rent.getRenter().getEmail());

        notificationService.createNotification(new Notification(0L,
                "The rent with ID " + rent.getId() + " has been checked in.", rent.getCar(), rent, emails),
                rent.getCar().getId());
        rent.setStartMileage(checkInRequest.getMileage());
        rent.setStartFuel(checkInRequest.getFuelLevel());
        rent.setStartDate(LocalDateTime.now());

        return rentRepository.save(rent);
    }

    public Rent checkOut(CheckInRequest checkInRequest) throws ServiceException {

        Rent rent = getRentById(checkInRequest.getRentId());

        if (rent == null) {
            throw new ServiceException("rent", "There aren't any rents with the specified ID.");
        }

        if (!rent.getActive()) {
            throw new ServiceException("rent", "The rent is not active.");
        }

        if (rent.getStartDate() == null) {
            throw new ServiceException("rent", "The rent has not been checked in.");
        }

        if (rent.getEndDate() != null) {
            throw new ServiceException("rent", "The rent has already been checked out.");
        }

        if (rent.getStartMileage() > checkInRequest.getMileage()) {
            throw new ServiceException("rent", "The mileage cannot be lower than the start mileage.");
        }

        if (rent.getStartFuel() < checkInRequest.getFuelLevel()) { // misschien later nog aanpassen indien tanken is
                                                                   // toegestaan
            throw new ServiceException("rent", "The fuel level cannot be higher than the start fuel level.");
        }

        // TODO: CHECK IF RENT IS FROM THE USER

        List<String> emails = new ArrayList<>();
        emails.add(rent.getRenter().getEmail());

        notificationService.createNotification(new Notification(0L,
                "The rent with ID " + rent.getId() + " has been checked out.", rent.getCar(), rent, emails),
                rent.getCar().getId());
        rent.setEndMileage(checkInRequest.getMileage());
        rent.setEndFuel(checkInRequest.getFuelLevel());
        rent.setEndDate(LocalDateTime.now());

        return rentRepository.save(rent);
    }
}