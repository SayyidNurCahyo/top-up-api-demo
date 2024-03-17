package com.enigma.swift_charge_demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCustomerRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String name;
}
