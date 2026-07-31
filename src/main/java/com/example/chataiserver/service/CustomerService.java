package com.example.chataiserver.service;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.model.Customer;

import java.util.List;

public interface CustomerService {

    List<CustomerSummaryDto> searchCustomers(String keyword);

    CustomerSummaryDto getCustomerById(String customerId);

}