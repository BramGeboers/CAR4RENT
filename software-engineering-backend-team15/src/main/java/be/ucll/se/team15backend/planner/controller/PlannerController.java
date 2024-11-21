package be.ucll.se.team15backend.planner.controller;

import be.ucll.se.team15backend.planner.model.PlannerRequest;
import be.ucll.se.team15backend.planner.model.PlannerResponse;
import be.ucll.se.team15backend.planner.service.PlannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/planner")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    @PostMapping("")
    public List<PlannerResponse> plan(@Valid @RequestBody PlannerRequest request) {
        return plannerService.plan(request);
    }
}
