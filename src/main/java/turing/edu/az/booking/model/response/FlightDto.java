package turing.edu.az.booking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Long id;
    private String origin;
    private String destination;
    private Integer availableSeats;
    private LocalDateTime timestamp;
}