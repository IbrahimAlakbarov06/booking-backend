package turing.edu.az.booking.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.domain.repository.PassengerRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.PassengerMapper;
import turing.edu.az.booking.model.request.PassengerRequest;
import turing.edu.az.booking.model.response.PassengerDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public List<PassengerDto> findAll() {
        List<Passenger> passengers = passengerRepository.findAll();

        return passengers.stream()
                .map(passengerMapper::toDto)
                .collect(Collectors.toList());
    }

    public Passenger getOrCreatePassenger(String fullName, String email) {
        if (email != null && !email.isEmpty()) {
            return passengerRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Passenger newPassenger = new Passenger();
                        newPassenger.setFullName(fullName);
                        newPassenger.setEmail(email);
                        return passengerRepository.save(newPassenger);
                    });
        } else {
            return passengerRepository.findByFullName(fullName)
                    .orElseGet(() -> {
                        Passenger newPassenger = new Passenger();
                        newPassenger.setFullName(fullName);
                        return passengerRepository.save(newPassenger);
                    });
        }
    }
    
    public Passenger findEntityById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
    }

    public PassengerDto findById(Long id){
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Passenger not found with id: " + id));
        return passengerMapper.toDto(passenger);
    }

    public PassengerDto save(PassengerRequest passengerRequest) {
        Passenger passenger = new Passenger();
        passenger.setFullName(passengerRequest.getFullName());
        passenger.setEmail(passengerRequest.getEmail());

        return passengerMapper.toDto(passengerRepository.save(passenger));
    }

    public void delete(Long id){
        if (!passengerRepository.existsById(id)) {
            throw new RuntimeException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
    }

    public PassengerDto update(Long id, PassengerRequest passengerRequest) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Passenger not found with id: " + id));

        passenger.setFullName(passengerRequest.getFullName());
        passenger.setEmail(passengerRequest.getEmail());
        return passengerMapper.toDto(passengerRepository.save(passenger));
    }
}
