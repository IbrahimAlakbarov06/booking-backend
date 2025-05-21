package turing.edu.az.booking.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.mapper.FlightMapper;
import turing.edu.az.booking.model.request.FlightRequest;
import turing.edu.az.booking.model.response.FlightDto;
import turing.edu.az.booking.services.FlightService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock FlightRepository flightRepository;
    @Mock FlightMapper flightMapper;

    @InjectMocks
    FlightService flightService;

    @Test
    void update_shouldUpdateFlight() {
        Flight existing = new Flight(1L, "Baku", "Istanbul", 100, LocalDateTime.now().plusDays(2));
        FlightRequest updateRequest = new FlightRequest("Baku", "London", 50, LocalDateTime.now().plusDays(5));
        Flight updated = new Flight(1L, "Baku", "London", 50, updateRequest.getTimestamp());
        FlightDto dto = new FlightDto(1L, "Baku", "London", 50, updateRequest.getTimestamp());

        when(flightRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(flightRepository.save(existing)).thenReturn(updated);
        when(flightMapper.toDto(updated)).thenReturn(dto);

        FlightDto result = flightService.update(1L, updateRequest);

        assertThat(result.getDestination()).isEqualTo("London");
    }

    @Test
    void getFlight_notFound_throwsException() {
        when(flightRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findById(123L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
