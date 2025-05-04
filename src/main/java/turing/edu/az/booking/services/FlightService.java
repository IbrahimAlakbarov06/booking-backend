package turing.edu.az.booking.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.FlightMapper;
import turing.edu.az.booking.model.request.FlightRequest;
import turing.edu.az.booking.model.response.FlightDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;


    public List<FlightDto> findAll() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
                .map(flightMapper::toDto)
                .collect(Collectors.toList());
    }

    public FlightDto findById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return flightMapper.toDto(flight);
    }

    public void delete(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }

    public FlightDto save(FlightRequest flightRequest) {
        Flight flight = new Flight();
        flight.setOrigin(flightRequest.getOrigin());
        flight.setDestination(flightRequest.getDestination());
        flight.setAvailableSeats(flightRequest.getAvailableSeats());
        flight.setTimestamp(flightRequest.getTimestamp());

        flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    public FlightDto update(Long id, FlightRequest flightRequest) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        flight.setOrigin(flightRequest.getOrigin());
        flight.setDestination(flightRequest.getDestination());
        flight.setAvailableSeats(flightRequest.getAvailableSeats());
        flight.setTimestamp(flightRequest.getTimestamp());

        flightRepository.save(flight);
        return flightMapper.toDto(flight);
    }

    public List<FlightDto> searchFlights(String origin, String destination, Integer minSeats) {
        List<Flight> flights;

        if (origin != null && destination != null && minSeats != null) {
            flights = flightRepository.findByOriginAndDestinationAndAvailableSeatsGreaterThanEqual(
                    origin, destination, minSeats);
        } else if (origin != null && destination != null) {
            flights = flightRepository.findByOriginAndDestination(origin, destination);
        } else if (origin != null) {
            flights = flightRepository.findByOrigin(origin);
        } else if (destination != null) {
            flights = flightRepository.findByDestination(destination);
        } else if (minSeats != null) {
            flights = flightRepository.findByAvailableSeatsGreaterThanEqual(minSeats);
        } else {
            flights = flightRepository.findAll();
        }

        return flights.stream()
                .map(flightMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateAvailableSeats(Long flightId, int seatsToBook) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + flightId));

        if (flight.getAvailableSeats() < seatsToBook) {
            throw new IllegalArgumentException("Not enough seats available");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seatsToBook);
        flightRepository.save(flight);
    }

    public List<FlightDto> getUpcomingFlights() {
        List<Flight> flights = flightRepository.findByTimestampAfter(LocalDateTime.now());
        return flights.stream()
                .map(flightMapper::toDto)
                .collect(Collectors.toList());
    }
}
