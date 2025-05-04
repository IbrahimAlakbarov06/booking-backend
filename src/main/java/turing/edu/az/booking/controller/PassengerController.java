package turing.edu.az.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.edu.az.booking.model.request.PassengerRequest;
import turing.edu.az.booking.model.response.PassengerDto;
import turing.edu.az.booking.services.PassengerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public ResponseEntity<List<PassengerDto>> getAllPassengers() {
        List<PassengerDto> passengers = passengerService.findAll();
        return ResponseEntity.ok(passengers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDto> getPassengerById(@PathVariable Long id) {
        PassengerDto passenger = passengerService.findById(id);
        return ResponseEntity.ok(passenger);
    }

    @PostMapping
    public ResponseEntity<PassengerDto> createPassenger(@Valid @RequestBody PassengerRequest request) {
        PassengerDto createdPassenger = passengerService.save(request);
        return new ResponseEntity<>(createdPassenger, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerDto> updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody PassengerRequest request) {
        PassengerDto updatedPassenger = passengerService.update(id, request);
        return ResponseEntity.ok(updatedPassenger);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}