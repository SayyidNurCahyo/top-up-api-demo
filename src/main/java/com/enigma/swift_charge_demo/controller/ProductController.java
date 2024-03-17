package com.enigma.swift_charge_demo.controller;

import com.enigma.swift_charge_demo.constant.APIUrl;
import com.enigma.swift_charge_demo.dto.request.NewProductRequest;
import com.enigma.swift_charge_demo.dto.request.SearchProductRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateProductRequest;
import com.enigma.swift_charge_demo.dto.response.CommonResponse;
import com.enigma.swift_charge_demo.dto.response.PagingResponse;
import com.enigma.swift_charge_demo.dto.response.ProductResponse;
import com.enigma.swift_charge_demo.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.PRODUCT)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> create(@RequestBody NewProductRequest request){
        ProductResponse response = productService.create(request);
        CommonResponse<ProductResponse> commonResponse = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message("product successfully added")
                .data(response).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> getById(@PathVariable String id){
        ProductResponse response = productService.getById(id);
        CommonResponse<ProductResponse> commonResponse = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("OK")
                .data(response).build();
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "product_name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "priceMax", required = false) Long priceMax,
            @RequestParam(name = "priceMin", required = false) Long priceMin
    ){
        SearchProductRequest request = SearchProductRequest.builder().page(page)
                .sortBy(sortBy).size(size).direction(direction).name(name)
                .priceMax(priceMax).priceMin(priceMin).build();
        Page<ProductResponse> response = productService.getAll(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(response.getTotalPages())
                .totalElement(response.getTotalElements())
                .page(response.getPageable().getPageNumber()+1)
                .size(response.getPageable().getPageSize())
                .hasNext(response.hasNext())
                .hasPrevious(response.hasPrevious()).build();
        CommonResponse<List<ProductResponse>> commonResponse = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message("OK").data(response.getContent()).paging(pagingResponse).build();
        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> update(@RequestBody UpdateProductRequest request){
        ProductResponse response = productService.update(request);
        CommonResponse<ProductResponse> commonResponse = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("product successfully updated")
                .data(response).build();
        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id){
        productService.deleteById(id);
        CommonResponse<String> commonResponse = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value()).message("product successfully deleted").build();
        return ResponseEntity.ok(commonResponse);
    }
}
