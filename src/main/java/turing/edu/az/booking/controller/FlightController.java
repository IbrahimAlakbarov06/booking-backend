package turing.edu.az.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.edu.az.booking.model.request.FlightRequest;
import turing.edu.az.booking.model.response.FlightDto;
import turing.edu.az.booking.services.FlightService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightDto> createFlight(@Valid @RequestBody FlightRequest request) {
        FlightDto createdFlight = flightService.save(request);
        return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<FlightDto>> getAllFlights() {
        List<FlightDto> flights = flightService.findAll();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable Long id) {
        FlightDto flight = flightService.findById(id);
        return ResponseEntity.ok(flight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDto> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequest request) {
        FlightDto updatedFlight = flightService.update(id, request);
        return ResponseEntity.ok(updatedFlight);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> searchFlights(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Integer minSeats) {
        List<FlightDto> flights = flightService.searchFlights(origin, destination, minSeats);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FlightDto>> getUpcomingFlights() {
        List<FlightDto> flights = flightService.getUpcomingFlights();
        return ResponseEntity.ok(flights);
    }
}