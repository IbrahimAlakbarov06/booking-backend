package turing.edu.az.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.domain.repository.BookingRepository;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.BookingMapper;
import turing.edu.az.booking.model.request.BookingRequest;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.services.BookingService;
import turing.edu.az.booking.services.FlightService;
import turing.edu.az.booking.services.PassengerService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("findAll() → should return empty list when no bookings")
    void whenNoBookings_thenFindAllReturnsEmpty() {
        // Given
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());
        when(bookingMapper.toDto(anyList())).thenReturn(Collections.emptyList());

        // When
        List<BookingDto> result = bookingService.findAll();

        // Then
        assertThat(result).isNotNull().isEmpty();
        verify(bookingRepository).findAll();
    }

    @Test
    @DisplayName("findById() → should return booking for existing id")
    void whenBookingExists_thenFindByIdReturnsIt() {
        // Given
        Booking booking = new Booking();
        booking.setId(1L);
        BookingDto bookingDto = new BookingDto(1L, 1L, "John Doe", 2);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        // When
        BookingDto found = bookingService.findById(1L);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        verify(bookingRepository).findById(1L);
        verify(bookingMapper).toDto(booking);
    }

    @Test
    @DisplayName("findById() → should throw ResourceNotFoundException when booking not found")
    void whenBookingNotExists_thenThrowResourceNotFoundException() {
        // Given
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> bookingService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");

        verify(bookingRepository).findById(99L);
    }

    @Test
    @DisplayName("save() → should create new booking and return it")
    void whenSave_thenReturnSavedBookingDto() {
        // Given
        BookingRequest request = new BookingRequest(1L, "John Doe", 2);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(10);

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFullName("John Doe");

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setFlight(flight);
        savedBooking.setPassenger(passenger);
        savedBooking.setNumberOfSeats(2);

        BookingDto expectedDto = new BookingDto(1L, 1L, "John Doe", 2);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerService.getOrCreatePassenger(eq("John Doe"), isNull())).thenReturn(passenger);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toDto(savedBooking)).thenReturn(expectedDto);

        // When
        BookingDto result = bookingService.save(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFlightId()).isEqualTo(1L);
        assertThat(result.getPassengerName()).isEqualTo("John Doe");
        assertThat(result.getNumberOfSeats()).isEqualTo(2);

        verify(flightRepository).findById(1L);
        verify(passengerService).getOrCreatePassenger(eq("John Doe"), isNull());
        verify(flightService).updateAvailableSeats(1L, 2);
        verify(bookingRepository).save(any(Booking.class));
        verify(bookingMapper).toDto(savedBooking);
    }

    @Test
    @DisplayName("save() → should throw exception when not enough seats")
    void whenNotEnoughSeats_thenThrowIllegalArgumentException() {
        // Given
        BookingRequest request = new BookingRequest(1L, "John Doe", 10);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5); // Only 5 seats available

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        // When/Then
        assertThatThrownBy(() -> bookingService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough seats available");

        verify(flightRepository).findById(1L);
        verifyNoMoreInteractions(passengerService, bookingRepository, bookingMapper);
    }

    @Test
    @DisplayName("delete() → should increase flight available seats and delete booking")
    void whenDelete_thenIncreaseFlightSeatsAndDeleteBooking() {
        // Given
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(8);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setFlight(flight);
        booking.setNumberOfSeats(2);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // When
        bookingService.delete(1L);

        // Then
        assertThat(flight.getAvailableSeats()).isEqualTo(10); // 8 + 2 = 10
        verify(bookingRepository).findById(1L);
        verify(flightRepository).save(flight);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    @DisplayName("getBookingsByFlightId() → should return bookings for flight")
    void whenGetBookingsByFlightId_thenReturnBookingsForFlight() {
        // Given
        Booking booking = new Booking();
        booking.setId(1L);
        List<Booking> bookings = List.of(booking);

        BookingDto bookingDto = new BookingDto(1L, 1L, "John Doe", 2);
        List<BookingDto> expectedDtos = List.of(bookingDto);

        when(bookingRepository.findByFlightId(1L)).thenReturn(bookings);
        when(bookingMapper.toDto(bookings)).thenReturn(expectedDtos);

        // When
        List<BookingDto> result = bookingService.getBookingsByFlightId(1L);

        // Then
        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(bookingRepository).findByFlightId(1L);
        verify(bookingMapper).toDto(bookings);
    }
}