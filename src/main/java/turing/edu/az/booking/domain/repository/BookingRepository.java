package turing.edu.az.booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.entity.Passenger;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByFlight(Flight flight);

    List<Booking> findByPassenger(Passenger passenger);

    List<Booking> findByFlightId(Long flightId);

    long countByFlightId(Long flightId);
}
