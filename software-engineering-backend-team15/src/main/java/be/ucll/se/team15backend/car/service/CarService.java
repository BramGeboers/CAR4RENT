package be.ucll.se.team15backend.car.service;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.car.repo.CarRepository;

import be.ucll.se.team15backend.notification.repo.NotificationRepository;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<Car> getAllCarsPageable(Pageable pageable, String email){
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (owner.getRole().equals(Role.ADMIN)) {
            return carRepository.findAll(pageable);
        }
        return carRepository.findAllByOwner(pageable, owner);
    }

    public Car addCar(Car car, String email){
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        car.setOwner(owner);
        return carRepository.save(car);
    }
    public List<Car> getAllCars(){
        return carRepository.findAllBy();
    }

    public Car deleteCar(long id, String email) {
        Car car = getCarById(id);
        if (car == null) {
            throw new RuntimeException("Car not found with id: " + id);
        }
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        if (!car.getOwner().equals(owner)) {
            throw new RuntimeException("You are not the owner of this car.");
        }

        if (!car.getRentals().isEmpty()) {
            throw new RuntimeException("Car is currently rented out.");
        }

        if (!car.getNotifications().isEmpty()) {
            throw new RuntimeException("Car has notifications.");
        }



        carRepository.delete(car);
        return car;
    }

    public Car getCarByIdSecure(Long id, String email){
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        if (owner.getRole().equals(Role.ADMIN)) {
            return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        }

        return carRepository.findByIdAndOwner(id, owner).orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    public Car getCarById(Long id){
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

    }
    //advanced search based on the parameters like brand, model, year, price, fueltype, transmission, seats, doors, color. all parameters are optional and can be used in any combination, they are by default null

    public Page<Car> searchCars(String brand, String model, String types, String licensePlate, Integer numberOfSeats, Integer numberOfChildSeats, Boolean foldingRearSeat, Boolean towBar, Double mileage, Double fuel, Double fuelCapacity, Double fuelEstimatedConsumption, Double pricePerKm, Double pricePerLiterFuel, Pageable pageable, String email){
        UserModel owner = userService.getUserByEmail(email);
        if (owner == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        //check wich parameters are null and call the right method

            return carRepository.searchCars(brand, model, types, licensePlate, numberOfSeats, numberOfChildSeats, foldingRearSeat, towBar, mileage, fuel, fuelCapacity, fuelEstimatedConsumption, pricePerKm, pricePerLiterFuel, pageable);
       
    }

}