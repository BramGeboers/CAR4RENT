package be.ucll.se.team15backend.rent.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckInRequest {

    @NotNull
    private long rentId;

    @NotNull
    @Min(0)
    private double mileage;

    @NotNull
    @Min(0)
    @Max(100)
    private double fuelLevel;

}
