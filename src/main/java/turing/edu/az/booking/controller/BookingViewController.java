package turing.edu.az.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import turing.edu.az.booking.services.BookingService;

@Controller
@RequiredArgsConstructor
public class BookingViewController {

    private final BookingService bookingService;

    @GetMapping("/bookings")
    public String bookingsPage(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "booking-list";
    }

    @GetMapping("/bookings/create")
    public String createBookingPage() {
        return "booking-create";
    }
}
