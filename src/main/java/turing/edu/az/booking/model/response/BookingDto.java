package turing.edu.az.booking.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long bookingId;
    private Long flightId;
    private String origin;
    private String destination;
    private LocalDateTime flightDateTime;
    private String passengerName;
    private String passengerEmail;
    private Integer numberOfSeats;

    public BookingDto(Long bookingId, Long flightId, String passengerName, Integer numberOfSeats) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengerName = passengerName;
        this.numberOfSeats = numberOfSeats;
    }
}
