package com.enigma.swift_charge_demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    @NotBlank(message = "product id is required")
    private String id;
    @NotBlank(message = "product name is required")
    private String name;
    private String description;
    @NotNull(message = "product price is required")
    @Min(value = 0, message = "product price should be greater than 0")
    private Long price;
}
