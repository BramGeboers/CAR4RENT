package be.ucll.se.team15backend.planner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostOverview {

    private double fixedCost;

    private double distanceCost;

    private double fuelCost;

    private double totalCost;
}
