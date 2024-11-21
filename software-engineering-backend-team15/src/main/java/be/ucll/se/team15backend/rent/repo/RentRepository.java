package be.ucll.se.team15backend.rent.repo;


import be.ucll.se.team15backend.rent.model.Rent;

import be.ucll.se.team15backend.user.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    public Rent findRentById(long Id);

    public List<Rent> findAllBy();

    public List<Rent> getRentByCarId(long id);

    Page<Rent> findAll(Pageable pageable);

    Page<Rent> findRentByRenterAndRentalNotNull(Pageable pageable, UserModel renter);

    Page<Rent> findRentByCarOwner(Pageable pageable, UserModel carOwner);


    @Query("SELECT r FROM Rent r WHERE (:startDate is null or r.startDate >= :startDate) and (:endDate is null or r.endDate <= :endDate) and (:city is null or r.rental.city = :city) and (:licenseplate is null or r.car.licensePlate = :licenseplate) and (:email is null or r.renter.email = :email)")
Page<Rent> searchRents(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("city") String city, @Param("licenseplate") String licenseplate, @Param("email") String email, Pageable pageable);
}




