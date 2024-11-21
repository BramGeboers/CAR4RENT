package be.ucll.se.team15backend.planner.model;

import be.ucll.se.team15backend.map.model.Coordinates;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerRequest {

    @NotNull
    Coordinates start;
    @NotNull
    Coordinates end;
    @NotNull
    LocalDateTime date;


}
