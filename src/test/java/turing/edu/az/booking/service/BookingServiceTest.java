package turing.edu.az.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.domain.repository.BookingRepository;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.BadRequestException;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.BookingMapper;
import turing.edu.az.booking.model.request.BookingRequest;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.services.BookingService;
import turing.edu.az.booking.services.FlightService;
import turing.edu.az.booking.services.PassengerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock BookingRepository bookingRepository;
    @Mock FlightRepository flightRepository;
    @Mock PassengerService passengerService;
    @Mock FlightService flightService;
    @Mock BookingMapper bookingMapper;

    @InjectMocks
    BookingService bookingService;

    @Test
    @DisplayName("save() → throw if flight not found")
    void save_whenFlightNotFound_throwsException() {
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        BookingRequest request = new BookingRequest(999L, 1L, 2);

        assertThatThrownBy(() -> bookingService.save(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Flight not found");
    }

    @Test
    @DisplayName("save() → throw if not enough seats")
    void save_whenInsufficientSeats_throwsBadRequest() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(1);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        BookingRequest request = new BookingRequest(1L, 1L, 2);

        assertThatThrownBy(() -> bookingService.save(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Not enough seats available");
    }

    @Test
    @DisplayName("delete() → flight seats increase")
    void deleteBooking_shouldIncreaseFlightSeats() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5);

        Booking booking = new Booking(1L, flight, new Passenger(), 2);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.delete(1L);

        assertThat(flight.getAvailableSeats()).isEqualTo(7);
        verify(flightRepository).save(flight);
        verify(bookingRepository).deleteById(1L);
    }
}
