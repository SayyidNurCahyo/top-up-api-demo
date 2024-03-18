package com.enigma.swift_charge_demo.controller;

import com.enigma.swift_charge_demo.constant.APIUrl;
import com.enigma.swift_charge_demo.dto.request.SearchCustomerRequest;
import com.enigma.swift_charge_demo.dto.response.CommonResponse;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.dto.response.PagingResponse;
import com.enigma.swift_charge_demo.security.AuthenticatedUser;
import com.enigma.swift_charge_demo.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = APIUrl.CUSTOMER)
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "Bearer Authentication")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticatedUser authenticatedUser;

    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasId(#id)")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<CustomerResponse>> getById(@PathVariable String id){
        CustomerResponse response = customerService.getById(id);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("OK")
                .data(response).build();
        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAll(
            @RequestParam(value = "size", defaultValue = "1") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "sortBy", defaultValue = "customer_name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "name", required = false) String name
    ){
        SearchCustomerRequest request = SearchCustomerRequest.builder().size(size)
                .page(page).sortBy(sortBy).direction(direction).name(name).build();
        Page<CustomerResponse> response = customerService.getAll(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(response.getTotalPages())
                .totalElement(response.getTotalElements())
                .page(response.getPageable().getPageNumber()+1)
                .size(response.getPageable().getPageSize())
                .hasNext(response.hasNext())
                .hasPrevious(response.hasPrevious()).build();
        CommonResponse<List<CustomerResponse>> commonResponse = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value()).message("OK").data(response.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<CustomerResponse>> update(@PathVariable String id){
        CustomerResponse response = customerService.updateEnable(id);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("OK").data(response).build();
        return ResponseEntity.ok(commonResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> disable(@PathVariable String id){
        customerService.deleteById(id);
        CommonResponse<String> commonResponse = CommonResponse.<String>builder().statusCode(HttpStatus.OK.value())
                .message("customer successfully deleted").build();
        return ResponseEntity.ok(commonResponse);
    }
}
