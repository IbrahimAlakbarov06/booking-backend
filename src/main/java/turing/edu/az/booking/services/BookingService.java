package turing.edu.az.booking.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.domain.repository.BookingRepository;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.BookingMapper;
import turing.edu.az.booking.model.request.BookingRequest;
import turing.edu.az.booking.model.response.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final FlightService flightService;
    private final PassengerService passengerService;
    private final BookingMapper bookingMapper;

    public List<BookingDto> findAll(){
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }

    public BookingDto findById(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found with id " + id));
        return mapToBookingDto(booking);
    }

    @Transactional
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getNumberOfSeats());
        flightRepository.save(flight);

        bookingRepository.deleteById(id);
    }

    @Transactional
    public BookingDto save(BookingRequest bookingRequest){
        Flight flight = flightRepository.findById(bookingRequest.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + bookingRequest.getFlightId()));

        if (flight.getAvailableSeats() < bookingRequest.getNumberOfSeats()) {
            throw new IllegalArgumentException("Not enough seats available on this flight");
        }

        Passenger passenger = passengerService.getOrCreatePassenger(bookingRequest.getPassengerName(), null);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setNumberOfSeats(bookingRequest.getNumberOfSeats());

        flightService.updateAvailableSeats(flight.getId(), bookingRequest.getNumberOfSeats());

        booking = bookingRepository.save(booking);

        return mapToBookingDto(booking);
    }

    public List<BookingDto> getBookingsByFlightId(Long flightId) {
        List<Booking> bookings = bookingRepository.findByFlightId(flightId);
        return bookings.stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }

    private BookingDto mapToBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setFlightId(booking.getFlight().getId());
        dto.setPassengerName(booking.getPassenger() != null ?
                booking.getPassenger().getFullName() : null);
        dto.setNumberOfSeats(booking.getNumberOfSeats());
        return dto;
    }
}