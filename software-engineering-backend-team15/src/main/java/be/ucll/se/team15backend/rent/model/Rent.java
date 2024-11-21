package be.ucll.se.team15backend.rent.model;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Rents")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rent {

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull(message = "Car is required")
    private Car car;

//    @NotBlank(message = "Email is required")
//    private String email;

    @ManyToOne
    private UserModel renter;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Identification number of national register is required")
    private String nationalIdentificationNumber;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    private Boolean active;

    @NotNull(message = "drivingLicenseNumber is required")
    private String drivingLicenseNumber;

    @OneToOne(fetch = FetchType.EAGER)

    private Rental rental;

    private LocalDateTime startDate;
    private double startMileage;
    private double startFuel;

    private Boolean payed;


    private LocalDateTime endDate;
    private double endMileage;
    private double endFuel;

    private double price;



}