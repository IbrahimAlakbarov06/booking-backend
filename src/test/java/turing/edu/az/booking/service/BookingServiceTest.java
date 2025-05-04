package turing.edu.az.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import turing.edu.az.booking.domain.entity.Booking;
import turing.edu.az.booking.domain.repository.BookingRepository;
import turing.edu.az.booking.exception.ResourceNotFoundException;
import turing.edu.az.booking.services.BookingService;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("getAll() → boş siyahı qaytarmalıdır")
    void whenNoBookings_thenGetAllReturnsEmpty() {

        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());
        List<Booking> result = bookingService.getAll();
        assertThat(result).isNull();
        verify(bookingRepository).findAll();

    }

    @Test
    @DisplayName("getById() → mövcud id üçün qaytarsın")
    void whenBookingExists_thenGetByIdReturnsIt() {
        Booking b = new Booking();
        b.setId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(b));

        Booking found = bookingService.getById(1L);

        assertThat(found).isSameAs(b);
        verify(bookingRepository).findById(1L);
    }

    @Test
    @DisplayName("getById() → tapılmazsa ResourceNotFoundException atmalı")
    void whenBookingNotExists_thenThrow() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Booking not found");
        verify(bookingRepository).findById(99L);
    }

    @Test
    @DisplayName("save() → yeni booking yaradıb qaytarmalıdır")
    void whenSave_thenReturnSaved() {

        Booking in = new Booking();
        in.setNumberOfSeats(2);

        Booking out = new Booking();
        out.setId(5L);
        out.setNumberOfSeats(2);

        when(bookingRepository.save(any(Booking.class))).thenReturn(out);

        Booking saved = bookingService.save(in);


        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(5L);
        assertThat(saved.getNumberOfSeats()).isEqualTo(2);

        verify(bookingRepository).save(any(Booking.class));
    }


    @Test
    @DisplayName("delete() → repository.deleteById çağırılmalıdır")
    void whenDelete_thenRepositoryCalled() {
        bookingService.delete(7L);
        verify(bookingRepository).deleteById(7L);
    }
}
