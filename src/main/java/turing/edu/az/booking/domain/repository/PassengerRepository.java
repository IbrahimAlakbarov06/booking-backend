package turing.edu.az.booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.edu.az.booking.domain.entity.Passenger;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByFullName(String fullName);

    boolean existsByEmail(String email);
}