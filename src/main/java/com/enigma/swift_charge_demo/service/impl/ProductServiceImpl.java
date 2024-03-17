package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.NewProductRequest;
import com.enigma.swift_charge_demo.dto.request.SearchProductRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateProductRequest;
import com.enigma.swift_charge_demo.dto.response.ProductResponse;
import com.enigma.swift_charge_demo.entity.Product;
import com.enigma.swift_charge_demo.repository.ProductRepository;
import com.enigma.swift_charge_demo.service.ProductService;
import com.enigma.swift_charge_demo.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(NewProductRequest request) {
        validationUtil.validate(request);
        Product product = Product.builder().name(request.getName())
                .description(request.getDescription()).price(request.getPrice())
                .isAvailable(true).build();
        productRepository.saveAndFlush(product);
        return convertToProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findByIdAvailable(id).orElseThrow(()->new RuntimeException("product not found"));
        return convertToProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        validationUtil.validate(request);
        if (request.getSize()<1) request.setSize(1);
        if (request.getPage()<1) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if (request.getName()!=null || request.getPriceMax()!=null && request.getPriceMin()!=null){
            if (request.getName()!=null) request.setName("%"+request.getName()+"%");
            Page<Product> products = productRepository.findProductByNameOrPriceBetween(request.getName(), request.getPriceMin(), request.getPriceMax(), pageable).orElseThrow(()-> new RuntimeException("product not found"));
            return convertToPageProductResponse(products);
        } else if (request.getPriceMax() != null) {
            Page<Product> products = productRepository.findByNameOrPrice(request.getPriceMax(), pageable).orElseThrow(()-> new RuntimeException("product not found"));
            return convertToPageProductResponse(products);
        } else if (request.getPriceMin() != null) {
            Page<Product> products = productRepository.findByNameOrPrice(request.getPriceMin(), pageable).orElseThrow(()-> new RuntimeException("product not found"));
            return convertToPageProductResponse(products);
        } else {
            return convertToPageProductResponse(productRepository.findAll(pageable));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(UpdateProductRequest request) {
        Product product = productRepository.findById(request.getId()).orElseThrow(()->new RuntimeException("product not found"));
        product.setDescription(request.getDescription());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setIsAvailable(true);
        productRepository.saveAndFlush(product);
        return convertToProductResponse(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        productRepository.findByIdAvailable(id).orElseThrow(()->new RuntimeException("product not found"));
        productRepository.deleteByIdAvailable(id);
    }

    public ProductResponse convertToProductResponse(Product product){
        return ProductResponse.builder().id(product.getId())
                .name(product.getName()).price(product.getPrice())
                .description(product.getDescription()).isAvailable(product.getIsAvailable()).build();
    }

    private Page<ProductResponse> convertToPageProductResponse(Page<Product> products) {
        return products.map(this::convertToProductResponse);
    }
}
