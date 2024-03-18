package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.NewTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.SearchTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateStatusRequest;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.dto.response.PaymentResponse;
import com.enigma.swift_charge_demo.dto.response.ProductResponse;
import com.enigma.swift_charge_demo.dto.response.TransactionResponse;
import com.enigma.swift_charge_demo.entity.*;
import com.enigma.swift_charge_demo.repository.TransactionRepository;
import com.enigma.swift_charge_demo.service.*;
import com.enigma.swift_charge_demo.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse addTransaction(NewTransactionRequest request) {
        validationUtil.validate(request);
        CustomerResponse customerResponse = customerService.getById(request.getCustomerId());
        ProductResponse productResponse = productService.getById(request.getProductId());
        Transaction transaction = Transaction.builder()
                .date(parseDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"yyyy-MM-dd"))
                .customer(Customer.builder().id(customerResponse.getCustomerId())
                        .name(customerResponse.getCustomerName())
                        .phone(customerResponse.getCustomerPhone())
                        .userAccount((UserAccount) userService.loadUserByUsername(customerResponse.getCustomerEmail())).build())
                .product(Product.builder().id(productResponse.getId()).name(productResponse.getName())
                        .price(productResponse.getPrice()).description(productResponse.getDescription())
                        .isAvailable(productResponse.getIsAvailable()).build()).build();
        Payment payment = paymentService.createPayment(transaction);
        transaction.setPayment(payment);
        PaymentResponse paymentResponse = PaymentResponse.builder().id(payment.getId())
                .redirectUrl(payment.getRedirectUrl()).status(payment.getStatus()).build();
        transactionRepository.saveAndFlush(transaction);
        return TransactionResponse.builder().id(transaction.getId())
                .date(transaction.getDate().toString()).customerName(transaction.getCustomer().getName())
                .customerEmail(transaction.getCustomer().getUserAccount().getEmail())
                .productName(transaction.getProduct().getName()).productPrice(transaction.getProduct().getPrice())
                .paymentId(transaction.getPayment().getId()).redirectUrl(transaction.getPayment().getRedirectUrl()).build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        String format = "yyyy-MM-dd";
        Pageable pageable = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if (request.getDateStart()!=null && request.getDateEnd()!=null){
            Page<Transaction> transaction = transactionRepository.findBetweenDate(parseDate(request.getDateStart(),format), parseDate(request.getDateEnd(),format), pageable).orElseThrow(()->new RuntimeException("transaction not found"));
            return convertToPageTransactionResponse(transaction);
        } else if (request.getDateEnd()!=null){
            Page<Transaction> transactions = transactionRepository.findBeforeDate(parseDate(request.getDateEnd(),format),pageable).orElseThrow(()->new RuntimeException("transaction not found"));
            return convertToPageTransactionResponse(transactions);
        } else if (request.getDateStart()!=null) {
            Page<Transaction> transactions = transactionRepository.findAfterDate(parseDate(request.getDateStart(),format),pageable).orElseThrow(()->new RuntimeException("transaction not found"));
            return convertToPageTransactionResponse(transactions);
        } else {
            Page<Transaction> transactions = transactionRepository.findAll(pageable);
            return convertToPageTransactionResponse(transactions);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllByCustomerId(String customerId) {
        List<Transaction> transactions = transactionRepository.findTrById(customerId).orElseThrow(()->new RuntimeException("transaction not found"));
        return transactions.stream().map(trans -> TransactionResponse.builder()
                .id(trans.getId()).date(trans.getDate().toString()).customerName(trans.getCustomer().getName())
                .customerEmail(trans.getCustomer().getUserAccount().getEmail())
                .productName(trans.getProduct().getName()).productPrice(trans.getProduct().getPrice())
                .paymentId(trans.getPayment().getId()).redirectUrl(trans.getPayment().getRedirectUrl()).build()).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateStatusRequest request) {
        Payment payment = paymentService.findById(request.getOrderId());
        payment.setStatus(request.getTransactionStatus());
    }

    private Date parseDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Date tempDate = new Date();
        try {
            tempDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tempDate;
    }

    private Page<TransactionResponse> convertToPageTransactionResponse(Page<Transaction> transaction) {
        return transaction.map(trans -> TransactionResponse.builder()
                .id(trans.getId()).date(trans.getDate().toString()).customerName(trans.getCustomer().getName())
                .customerEmail(trans.getCustomer().getUserAccount().getEmail())
                .productName(trans.getProduct().getName()).productPrice(trans.getProduct().getPrice())
                .paymentId(trans.getPayment().getId()).redirectUrl(trans.getPayment().getRedirectUrl()).build());
    }
}
