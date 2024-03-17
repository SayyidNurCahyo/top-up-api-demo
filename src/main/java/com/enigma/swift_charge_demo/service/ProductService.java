package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.request.NewProductRequest;
import com.enigma.swift_charge_demo.dto.request.SearchProductRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateProductRequest;
import com.enigma.swift_charge_demo.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(NewProductRequest request);
    ProductResponse getById(String id);
    Page<ProductResponse> getAll(SearchProductRequest request);
    ProductResponse update(UpdateProductRequest request);
    void deleteById(String id);
}
