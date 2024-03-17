package com.enigma.swift_charge_demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTransactionRequest {
    private Integer size;
    private Integer page;
    private String sortBy;
    private String direction;
    private String dateStart;
    private String dateEnd;
}
