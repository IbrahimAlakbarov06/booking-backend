package turing.edu.az.booking.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turing.edu.az.booking.domain.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
