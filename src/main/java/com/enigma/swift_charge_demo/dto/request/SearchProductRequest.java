package com.enigma.swift_charge_demo.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String name;
    @Min(value = 1, message = "price should be greater than 0")
    private Long priceMax;
    @Min(value = 1, message = "price should be greater than 0")
    private Long priceMin;
}
