package turing.edu.az.booking.model.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequest {

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Available seats is required")
    @Min(value = 1, message = "Available seats must be at least 1")
    private Integer availableSeats;

    @NotNull(message = "Timestamp is required")
    @Future(message = "Flight time must be in the future")
    private LocalDateTime timestamp;
}