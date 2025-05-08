package turing.edu.az.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import turing.edu.az.booking.domain.entity.Flight;
import turing.edu.az.booking.domain.repository.FlightRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.model.request.BookingRequest;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.services.BookingService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // For LocalDateTime serialization
    }

    @Test
    @DisplayName("GET /api/v1/bookings → should return empty JSON array")
    void whenNoBookings_thenGetAllReturnsEmptyJsonArray() throws Exception {
        given(bookingService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/bookings/{id} → should return booking by id")
    void whenGetBookingById_thenReturnBooking() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, 1L, "John Doe", 2);

        given(bookingService.findById(1L)).willReturn(bookingDto);

        mockMvc.perform(get("/api/v1/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightId").value(1))
                .andExpect(jsonPath("$.passengerName").value("John Doe"))
                .andExpect(jsonPath("$.numberOfSeats").value(2));
    }

    @Test
    @DisplayName("POST /api/v1/bookings → should create and return the booking")
    void whenCreateBooking_thenReturnsCreatedBooking() throws Exception {
        BookingRequest request = new BookingRequest(1L, "John Doe", 2);
        BookingDto createdBooking = new BookingDto(1L, 1L, "John Doe", 2);

        given(bookingService.save(any(BookingRequest.class))).willReturn(createdBooking);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightId").value(1))
                .andExpect(jsonPath("$.passengerName").value("John Doe"))
                .andExpect(jsonPath("$.numberOfSeats").value(2));
    }

    @Test
    @DisplayName("GET /api/v1/bookings/flight/{flightId} → should return bookings by flight id")
    void whenGetBookingsByFlightId_thenReturnBookings() throws Exception {
        BookingDto bookingDto = new BookingDto(1L, 1L, "John Doe", 2);
        List<BookingDto> bookings = List.of(bookingDto);

        given(bookingService.getBookingsByFlightId(1L)).willReturn(bookings);

        mockMvc.perform(get("/api/v1/bookings/flight/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].flightId").value(1))
                .andExpect(jsonPath("$[0].passengerName").value("John Doe"))
                .andExpect(jsonPath("$[0].numberOfSeats").value(2));
    }

    @Test
    @DisplayName("DELETE /api/v1/bookings/{id} → should return no content")
    void whenDeleteBooking_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/v1/bookings → insufficient seats should return 400 with error message")
    void whenCreateBookingWithInvalidSeats_thenReturnBadRequest() throws Exception {
        // BookingRequest'in `numberOfSeats`'i uçuşdakı mövcud yer sayısından çoxdur
        BookingRequest invalidRequest = new BookingRequest(1L, "John Doe", 10); // 10 yer istənir, lakin azdır

        // Flight ID: 1 olan uçuşda mövcud yer sayı 5-dirsə
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(5);

        given(flightRepository.findById(1L)).willReturn(Optional.of(flight));

        // Flight'ın mövcud yerindən çox yer istəyirik
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Not enough seats available on this flight"));
    }
}