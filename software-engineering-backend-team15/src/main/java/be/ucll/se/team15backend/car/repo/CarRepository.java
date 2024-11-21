package be.ucll.se.team15backend.car.repo;

import be.ucll.se.team15backend.car.model.Car;

import be.ucll.se.team15backend.user.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

     public Car findById(long id);

     public List<Car> findAllBy();

     Page<Car> findAllByOwner(Pageable pageable, UserModel owner);

     Optional<Car> findByIdAndOwner(long id, UserModel owner);
//     Page<Car> findAll(Pageable pageable, UserModel owner);

@Query("SELECT c FROM Car c WHERE (:brand is null or c.brand = :brand) and (:model is null or c.model = :model) and (:types is null or c.types = :types) and (:licensePlate is null or c.licensePlate = :licensePlate) and (:numberOfSeats is null or c.numberOfSeats = :numberOfSeats) and (:numberOfChildSeats is null or c.numberOfChildSeats = :numberOfChildSeats) and (:foldingRearSeat is null or c.foldingRearSeat = :foldingRearSeat) and (:towBar is null or c.towBar = :towBar) and (:mileage is null or c.mileage = :mileage) and (:fuel is null or c.fuel = :fuel) and (:fuelCapacity is null or c.fuelCapacity = :fuelCapacity) and (:fuelEstimatedConsumption is null or c.fuelEstimatedConsumption = :fuelEstimatedConsumption) and (:pricePerKm is null or c.pricePerKm = :pricePerKm) and (:pricePerLiterFuel is null or c.pricePerLiterFuel = :pricePerLiterFuel)")
Page<Car> searchCars(@Param("brand") String brand, @Param("model") String model, @Param("types") String types, @Param("licensePlate") String licensePlate, @Param("numberOfSeats") Integer numberOfSeats, @Param("numberOfChildSeats") Integer numberOfChildSeats, @Param("foldingRearSeat") Boolean foldingRearSeat, @Param("towBar") Boolean towBar, @Param("mileage") Double mileage, @Param("fuel") Double fuel, @Param("fuelCapacity") Double fuelCapacity, @Param("fuelEstimatedConsumption") Double fuelEstimatedConsumption, @Param("pricePerKm") Double pricePerKm, @Param("pricePerLiterFuel") Double pricePerLiterFuel, Pageable pageable);
}
