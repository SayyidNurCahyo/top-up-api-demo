package com.enigma.swift_charge_demo.controller;

import com.enigma.swift_charge_demo.constant.APIUrl;
import com.enigma.swift_charge_demo.dto.request.NewTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.SearchTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateStatusRequest;
import com.enigma.swift_charge_demo.dto.response.CommonResponse;
import com.enigma.swift_charge_demo.dto.response.PagingResponse;
import com.enigma.swift_charge_demo.dto.response.TransactionResponse;
import com.enigma.swift_charge_demo.security.AuthenticatedUser;
import com.enigma.swift_charge_demo.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {
    private final TransactionService transactionService;
    private final AuthenticatedUser authenticatedUser;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<TransactionResponse>> addTransaction(@RequestBody NewTransactionRequest request){
        TransactionResponse transaction = transactionService.addTransaction(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully save data")
                .data(transaction).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "product_name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "dateStart", required = false) String dateStart,
            @RequestParam(name = "dateEnd", required = false) String dateEnd
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder().page(page).size(size)
                .direction(direction).sortBy(sortBy).dateStart(dateStart).dateEnd(dateEnd).build();
        Page<TransactionResponse> transactions = transactionService.getAllTransaction(request);
        PagingResponse paging = PagingResponse.builder()
                .page(transactions.getPageable().getPageNumber() + 1)
                .size(transactions.getPageable().getPageSize())
                .totalPages(transactions.getTotalPages())
                .totalElement(transactions.getTotalElements())
                .hasNext(transactions.hasNext())
                .hasPrevious(transactions.hasPrevious()).build();
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(transactions.getContent())
                .paging(paging).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or @authenticatedUser.hasId(#customerId)")
    @GetMapping(path = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransactionByCustomerId(@PathVariable String customerId) {
        List<TransactionResponse> transactions = transactionService.getAllByCustomerId(customerId);
        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(transactions)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatus(@RequestBody Map<String, Object> request){
        UpdateStatusRequest statusRequest = UpdateStatusRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString()).build();
        transactionService.updateStatus(statusRequest);
        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK").build());
    }
}
