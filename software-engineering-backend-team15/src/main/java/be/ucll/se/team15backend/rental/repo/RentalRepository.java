package be.ucll.se.team15backend.rental.repo;

import be.ucll.se.team15backend.rental.model.Rental;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    
    public Rental findById(long id);
    
    public Page<Rental> findAll(Pageable pageable);

    Page<Rental> findRentalsByRentIsNull(Pageable pageable);

    public List<Rental> getRentalsByCarId(long id);
    @Query("SELECT r FROM Rental r WHERE (:startDate is null or r.startDate >= :startDate) and (:endDate is null or r.endDate <= :endDate) and (:city is null or r.city = :city) and (:street is null or r.street = :street) and (:postal is null or r.postal = :postal)")
    Page<Rental> searchRentals(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("city") String City, @Param("street") String Street, @Param("postal") String Postal, Pageable pageable);

}