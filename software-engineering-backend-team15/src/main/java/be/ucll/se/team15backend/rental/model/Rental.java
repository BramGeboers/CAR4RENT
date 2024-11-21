package be.ucll.se.team15backend.rental.model;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rent.model.Rent;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Rentals")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Hidden
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @ManyToOne()
    @JoinColumn(name = "carId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Car car;

    @NotBlank(message = "City is required")
    private String city;

    private String street;
    private String number;
    private String postal;

//    @NotBlank(message = "Email is required")
//    @Email
//    private String email;

//    @ManyToOne()
//    private UserModel renter;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private double longitude;
    private double latitude;

    @OneToOne(mappedBy = "rental", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Rent rent;



}
