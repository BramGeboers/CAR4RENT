package be.ucll.se.team15backend.map.model;

import be.ucll.se.team15backend.rental.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClosestsResponse {

    private Rental rental;
    private double distance;
}
