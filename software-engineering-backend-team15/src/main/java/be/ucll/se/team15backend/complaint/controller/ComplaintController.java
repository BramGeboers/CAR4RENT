package be.ucll.se.team15backend.complaint.controller;

import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.complaint.model.ComplaintRequest;
import be.ucll.se.team15backend.complaint.repo.ComplaintRepository;
import be.ucll.se.team15backend.complaint.service.ComplaintService;
import be.ucll.se.team15backend.rental.model.Rental;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(value = "*")
@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ComplaintRepository complaintRepository;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/submit")
    public Complaint submitComplaint(@RequestHeader(name="Authorization") @Parameter(hidden = true) String token, @RequestBody ComplaintRequest complaintRequest) {
        return complaintService.submitComplaint(complaintRequest);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("")
    public Page<Complaint> getAllComplaints(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<Complaint> complaints = complaintService.getAllComplaints(pageable);
        return complaintService.getAllComplaints(pageable);
    }

}
