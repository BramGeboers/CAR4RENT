package be.ucll.se.team15backend.authentication.repo;

import be.ucll.se.team15backend.authentication.model.VerifyToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerifyRepository extends JpaRepository<VerifyToken, String> {
    Optional<VerifyToken> findByToken(String token);
}
