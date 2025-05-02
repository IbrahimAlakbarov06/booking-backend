package turing.edu.az.booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.edu.az.booking.domain.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
