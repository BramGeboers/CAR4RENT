package be.ucll.se.team15backend.notification.model;

import java.util.List;

import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rent.model.Rent;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Notifications")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Hidden
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @NotBlank(message = "Message is required.")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER) // Many notifications can be related to one car
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    private List<String> emails;

}
