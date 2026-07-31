package com.example.chataiserver.controller;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/search")
    public List<CustomerSummaryDto> searchCustomers(
            @RequestParam String keyword) {

        return customerService.searchCustomers(keyword);
    }

    @GetMapping("/{customerId}")
    public CustomerSummaryDto getCustomerById(
            @PathVariable String customerId) {

        return customerService.getCustomerById(customerId);
    }

}