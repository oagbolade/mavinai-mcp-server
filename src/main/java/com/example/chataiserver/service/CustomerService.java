package com.example.chataiserver.service;

import com.example.chataiserver.dto.CustomerIdentityDto;
import com.example.chataiserver.dto.CustomerSummaryDto;

import java.util.List;

public interface CustomerService {

    List<CustomerSummaryDto> searchCustomers(String keyword);

    CustomerSummaryDto getCustomerById(String customerId);

    List<CustomerIdentityDto> getCustomerIdsByName(String name);

}
