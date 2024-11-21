package be.ucll.se.team15backend.billing.model;

import java.time.LocalDate;
import java.util.List;

import be.ucll.se.team15backend.notification.model.Notification;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Table(name = "Billing")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Billing {

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @NotNull(message = "Cost is required")
    private Double cost;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @ManyToOne
    @NotNull(message = "User is required")
    private UserModel user;

    @OneToOne
    @NotNull(message = "Rent is required")
    private Rent rent;

}
