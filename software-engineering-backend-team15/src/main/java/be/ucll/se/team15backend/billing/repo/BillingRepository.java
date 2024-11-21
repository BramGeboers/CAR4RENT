package be.ucll.se.team15backend.billing.repo;

import be.ucll.se.team15backend.billing.model.Billing;
import be.ucll.se.team15backend.rent.model.Rent;
import be.ucll.se.team15backend.user.model.UserModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    Billing findById(long id);

    Page<Billing> findAll(Pageable pageable);

    Optional<Billing> findBillingByRent(Rent rent);

    // Page<Billing> findBillingByRenter(Pageable pageable, UserModel renter);

    // Page<Billing> findBillingByCarOwner(Pageable pageable, UserModel carOwner);

}