package be.ucll.se.team15backend.complaint.repo;

import be.ucll.se.team15backend.complaint.model.Complaint;
import be.ucll.se.team15backend.rental.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComplaintRepository extends JpaRepository<Complaint, String> {

    public Page<Complaint> findAll(Pageable pageable);

}
