package be.ucll.se.team15backend.planner.model;

import be.ucll.se.team15backend.rental.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerResponse {

    Rental rental;
    double distance;

    CostOverview estimatedPrice;
}
