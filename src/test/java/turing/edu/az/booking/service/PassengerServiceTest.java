package turing.edu.az.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import turing.edu.az.booking.domain.entity.Passenger;
import turing.edu.az.booking.domain.repository.PassengerRepository;
import turing.edu.az.booking.mapper.PassengerMapper;
import turing.edu.az.booking.model.request.PassengerRequest;
import turing.edu.az.booking.model.response.PassengerDto;
import turing.edu.az.booking.services.PassengerService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock PassengerRepository passengerRepository;
    @Mock PassengerMapper passengerMapper;

    @InjectMocks PassengerService passengerService;

    @Test
    void findById_shouldReturnDto() {
        Passenger passenger = new Passenger(1L, "Alice", "alice@mail.com");
        PassengerDto dto = new PassengerDto(1L, "Alice", "alice@mail.com");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerMapper.toDto(passenger)).thenReturn(dto);

        PassengerDto result = passengerService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void save_shouldReturnSavedDto() {
        PassengerRequest request = new PassengerRequest("Bob", "bob@mail.com");
        Passenger passenger = new Passenger(null, "Bob", "bob@mail.com");
        Passenger saved = new Passenger(1L, "Bob", "bob@mail.com");
        PassengerDto dto = new PassengerDto(1L, "Bob", "bob@mail.com");

        when(passengerRepository.save(any())).thenReturn(saved);
        when(passengerMapper.toDto(saved)).thenReturn(dto);

        PassengerDto result = passengerService.save(request);

        assertThat(result.getEmail()).isEqualTo("bob@mail.com");
    }
}
