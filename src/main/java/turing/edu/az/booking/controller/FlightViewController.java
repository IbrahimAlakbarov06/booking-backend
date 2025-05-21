package turing.edu.az.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import turing.edu.az.booking.services.FlightService;

@Controller
@RequiredArgsConstructor
public class FlightViewController {

    private final FlightService flightService;

    @GetMapping("/flights")
    public String flightsPage(Model model) {
        model.addAttribute("flights", flightService.findAll());
        return "flight-list";
    }

    @GetMapping("/flights/create")
    public String createFlightPage() {
        return "flight-create";
    }
}
