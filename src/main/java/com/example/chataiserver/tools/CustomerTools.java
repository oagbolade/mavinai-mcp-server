package com.example.chataiserver.tools;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTools {

    private final CustomerService customerService;

    @Tool(description = "Search customers by name")
    public List<CustomerSummaryDto> searchCustomers(String keyword) {
        return customerService.searchCustomers(keyword);
    }

    @Tool(description = "Retrieve a customer by ID")
    public CustomerSummaryDto getCustomerById(String customerId) {
        return customerService.getCustomerById(customerId);
    }
}