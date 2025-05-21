package turing.edu.az.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import turing.edu.az.booking.exception.BadRequestException;
import turing.edu.az.booking.exception.GlobalExceptionHandler;
import turing.edu.az.booking.model.request.BookingRequest;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.services.BookingService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        bookingService = mock(BookingService.class);
        BookingController bookingController = new BookingController(bookingService);

        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new GlobalExceptionHandler()) // ✅ Exception handler əlavə olundu
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("GET /api/v1/bookings → should return empty JSON array")
    void getAllBookings_whenNoneExist_returnsEmptyArray() throws Exception {
        given(bookingService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/bookings/{id} → should return booking by id")
    void getBookingById_returnsBooking() throws Exception {
        BookingDto dto = new BookingDto(1L, 1L, "John Doe", 2);

        given(bookingService.findById(1L)).willReturn(dto);

        mockMvc.perform(get("/api/v1/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.passengerName").value("John Doe"));
    }

    @Test
    @DisplayName("POST /api/v1/bookings → should create booking")
    void createBooking_returnsCreated() throws Exception {
        BookingRequest request = new BookingRequest(1L, 5L, 2);
        BookingDto dto = new BookingDto(1L, 1L, "John Doe", 2);

        given(bookingService.save(any())).willReturn(dto);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.passengerName").value("John Doe"));
    }

    @Test
    @DisplayName("POST /api/v1/bookings → not enough seats should return 400")
    void createBooking_withInvalidSeats_returns400() throws Exception {
        BookingRequest request = new BookingRequest(1L, 9L, 99);

        given(bookingService.save(any()))
                .willThrow(new BadRequestException("Not enough seats available on this flight"));

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Not enough seats available on this flight"));
    }

    @Test
    @DisplayName("DELETE /api/v1/bookings/{id} → should return 204")
    void deleteBooking_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isNoContent());
    }
}
