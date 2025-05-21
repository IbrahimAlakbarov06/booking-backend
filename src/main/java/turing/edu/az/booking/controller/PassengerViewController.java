package turing.edu.az.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import turing.edu.az.booking.services.PassengerService;

@Controller
@RequiredArgsConstructor
public class PassengerViewController {

    private final PassengerService passengerService;

    @GetMapping("/passengers")
    public String passengersPage(Model model) {
        model.addAttribute("passengers", passengerService.findAll());
        return "passenger-list";
    }

    @GetMapping("/passengers/create")
    public String createPassengerPage() {
        return "passenger-create";
    }
}
