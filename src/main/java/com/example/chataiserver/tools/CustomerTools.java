package com.example.chataiserver.tools;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.dto.CustomerIdentityDto;
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

    @Tool(description = "Find customer IDs by a customer's full name, first name, or surname. Multiple matches may be returned; each result includes only the customer ID and full name.")
    public List<CustomerIdentityDto> getCustomerIdsByName(String name) {
        return customerService.getCustomerIdsByName(name);
    }
}
