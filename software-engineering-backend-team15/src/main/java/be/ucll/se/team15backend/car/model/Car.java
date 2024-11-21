package be.ucll.se.team15backend.car.model;

import java.util.List;

import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Entity
@Table(name = "Car")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    @NotBlank(message="Brand can not be empty !")
    private String brand;
    private String model;

    @NotBlank(message="Type can not be empty !")
    private String types;

    @NotBlank(message="License Plate can not be empty !")
    @Column(unique = true)
    private String licensePlate;

    @Min(value = 1, message = "Number of seats must be at least 1")
    private int numberOfSeats;
    private int numberOfChildSeats;
    private boolean foldingRearSeat;
    private boolean towBar;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Rental> rentals;


    private double mileage;
    private double fuel;  // percentage of tank
    private double fuelCapacity; // Liters of full tank

    private double fuelEstimatedConsumption; // Liter per 100 km


    private double pricePerKm;

    private double pricePerLiterFuel; // Euro per liter (TODO: via api)

    @ManyToOne()
    @JoinColumn(name = "ownerId")
    private UserModel owner;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> notifications;

    public void setRental(Rental rental){
        this.rentals.add(rental);
    }

    public void removeRental(Rental rental){
        this.rentals.remove(rental);
    }
}
