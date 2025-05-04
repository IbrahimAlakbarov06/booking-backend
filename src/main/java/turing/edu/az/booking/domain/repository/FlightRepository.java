package turing.edu.az.booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.edu.az.booking.domain.entity.Flight;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOrigin(String origin);

    List<Flight> findByDestination(String destination);

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> findByTimestampAfter(LocalDateTime timestamp);

    List<Flight> findByAvailableSeatsGreaterThanEqual(Integer seats);

    List<Flight> findByOriginAndDestinationAndAvailableSeatsGreaterThanEqual(String origin, String destination, Integer seats);
}