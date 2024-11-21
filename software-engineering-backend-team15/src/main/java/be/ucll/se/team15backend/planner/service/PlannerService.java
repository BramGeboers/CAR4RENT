package be.ucll.se.team15backend.planner.service;

import be.ucll.se.team15backend.map.model.ClosestsResponse;
import be.ucll.se.team15backend.map.model.Coordinates;
import be.ucll.se.team15backend.map.service.MapService;
import be.ucll.se.team15backend.car.model.Car;
import be.ucll.se.team15backend.rental.model.Rental;
import be.ucll.se.team15backend.planner.model.CostOverview;
import be.ucll.se.team15backend.planner.model.PlannerRequest;
import be.ucll.se.team15backend.planner.model.PlannerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlannerService {

    @Autowired
    private MapService mapService;

    private boolean isDateInBetween(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public List<PlannerResponse> plan(PlannerRequest request) {

        List<ClosestsResponse> nearestRentals = mapService.getClosestRentals(request.getStart(), -1);

        //TODO: ADD FILTERS

        //TODO: filter on fuel level higher then 10% (maybe give warning to owner)

        List<PlannerResponse> response = new ArrayList<>();

        nearestRentals
                .stream().filter(closestsResponse -> isDateInBetween(request.getDate(), closestsResponse.getRental().getStartDate(), closestsResponse.getRental().getEndDate()))
                .forEach(closestsResponse -> {
            Coordinates coordsCar = new Coordinates(closestsResponse.getRental().getLatitude(), closestsResponse.getRental().getLongitude());
            double distanceCarToDestination = mapService.getDistance(coordsCar, request.getEnd());
            double distanceUserToCar = closestsResponse.getDistance();
            PlannerResponse plannerResponse = PlannerResponse.builder()
                    .rental(closestsResponse.getRental())
                    .distance(round(distanceUserToCar, 3))
                    .estimatedPrice(calculateCost(closestsResponse.getRental(), distanceCarToDestination))
                    .build();
            response.add(plannerResponse);
        });

        return response;

    }

    private CostOverview calculateCost(Rental rental, double distance) {
        Car car = rental.getCar();

        double fixedCost = 5.99;

        double distanceCost = distance * car.getPricePerKm();

        double fuelCost = distance/100 * car.getFuelEstimatedConsumption() * car.getPricePerLiterFuel();

        double totalCost = fixedCost + distanceCost + fuelCost;

        return CostOverview.builder()
                .fixedCost(round(fixedCost, 2))
                .distanceCost(round(distanceCost, 2))
                .fuelCost(round(fuelCost, 2))
                .totalCost(round(totalCost, 2))
                .build();

    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
