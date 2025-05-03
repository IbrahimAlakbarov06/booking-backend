package turing.edu.az.booking.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequest {

    @NotBlank(message = "Full Name is required")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

}