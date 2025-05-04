package turing.edu.az.booking.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.FlightMapper;
import turing.edu.az.booking.model.request.FlightRequest;
import turing.edu.az.booking.model.response.FlightDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public List<FlightDto> getAllFlights() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
                .map(flightMapper::toEntity)
                .collect(Collectors.toList());
    }

    public FlightDto getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return flightMapper.toEntity(flight);
    }

    public FlightDto updateFlight(Long id, FlightRequest flightRequest) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Flight not found with id: " + id));

        flight.setOrigin(flightRequest.getOrigin());
        flight.setDestination(flightRequest.getDestination());
        flight.setAvailableSeats(flightRequest.getAvailableSeats());
        flight.setTimestamp(flightRequest.getTimestamp());

        flight = flightRepository.save(flight);
        return flightMapper.toEntity(flight);

    }

    public FlightDto searchFlight(String origin, String destination, Integer seats) {


    }
}
