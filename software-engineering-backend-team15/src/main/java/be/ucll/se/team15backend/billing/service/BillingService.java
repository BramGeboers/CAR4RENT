package be.ucll.se.team15backend.billing.service;

import java.util.List;

import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.rent.repo.RentRepository;
import be.ucll.se.team15backend.rent.service.RentService;
import be.ucll.se.team15backend.rental.repo.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ucll.se.team15backend.billing.model.Billing;
import be.ucll.se.team15backend.billing.repo.BillingRepository;
import be.ucll.se.team15backend.user.model.Role;
import be.ucll.se.team15backend.user.model.UserModel;
import be.ucll.se.team15backend.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Service
public class BillingService {

    @Autowired
    BillingRepository billingRepository;

    @Autowired
    UserService userService;

    @Autowired
    RentRepository rentRepository;

    
    public Billing getBillingById(Long rentId, String email) {
        UserModel user = userService.getUserByEmail(email);

        Rent rent = rentRepository.findById(rentId).orElseThrow(() -> new RuntimeException("Rent not found with id: " + rentId));


        Billing billing = billingRepository.findBillingByRent(rent).orElseThrow(() -> new RuntimeException("Billing not found with id: " + rent.getId()));

        if (user.getRole() == Role.ADMIN || user.getRole() == Role.ACCOUNTANT ) {
            return billing;
        }

        if (user.getEmail() == billing.getRent().getRenter().getEmail() ) {
            return billing;
        }

        throw new RuntimeException("You are not the receipent of the billing.");
    }
    

    public Page<Billing> getBilling(Pageable pageable, String email) {

        UserModel user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // if (user.getRole().equals(Role.ADMIN)) {
            return billingRepository.findAll(pageable);
        // }
        // if (user.getRole().equals(Role.OWNER)) {
        //     return billingRepository.findBillingByCarOwner(pageable, user);
        // }
        // if (user.getRole().equals(Role.RENTER)) {
        //     return billingRepository.findBillingByRenter(pageable, user);
        // }
        // throw new RuntimeException("User not found with email: " + email);
    }

    public Billing createBilling(Billing newBilling) {
        billingRepository.save(newBilling);

        return newBilling;
    }
}
