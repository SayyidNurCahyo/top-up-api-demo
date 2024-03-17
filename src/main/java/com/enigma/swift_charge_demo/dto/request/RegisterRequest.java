package com.enigma.swift_charge_demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^(\\+62|0)(8[0-9]{2}[-.\\s]?[0-9]{3,}-?[0-9]{3,}|\\(0[0-9]{2}\\)[-\\s]?[0-9]{3,}-?[0-9]{3,}|\\+62[-\\s]?[0-9]{1,2}[-.\\s]?[0-9]{3,}-?[0-9]{3,})$", message = "phone number is not in indonesia format")
    private String phone;
    @NotBlank(message = "email is required")
    @Email(message = "email format incorrect")
    private String email;
    @NotBlank(message = "password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "password at least 8 character, 1 uppercase-lowercase-number, and can contain special character")
    private String password;
}
