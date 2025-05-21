package turing.edu.az.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import turing.edu.az.booking.model.response.BookingDto;
import turing.edu.az.booking.services.BookingService;

import java.util.List;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingViewController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        List<BookingDto> bookings = bookingService.findAll();
        model.addAttribute("bookings", bookings);
        return "booking-list";
    }

    // ✅ BUNU ƏLAVƏ ET!
    @GetMapping("/create")
    public String showCreateForm() {
        return "booking-create";
    }
}
