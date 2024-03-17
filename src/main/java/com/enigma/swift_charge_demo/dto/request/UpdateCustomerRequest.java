package com.enigma.swift_charge_demo.dto.request;

import com.enigma.swift_charge_demo.entity.UserAccount;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {
    @NotBlank(message = "id is required")
    private String id;
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "email is required")
    @Email(message = "email format incorrect")
    private String email;
    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^(\\+62|0)(8[0-9]{2}[-.\\s]?[0-9]{3,}-?[0-9]{3,}|\\(0[0-9]{2}\\)[-\\s]?[0-9]{3,}-?[0-9]{3,}|\\+62[-\\s]?[0-9]{1,2}[-.\\s]?[0-9]{3,}-?[0-9]{3,})$", message = "phone number is not in indonesia format")
    private String phone;
    private String password;
}
