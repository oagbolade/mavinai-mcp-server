package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.model.Customer;
import com.example.chataiserver.repository.CustomerRepository;
import com.example.chataiserver.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerSummaryDto> searchCustomers(String keyword) {

        Set<CustomerSummaryDto> results = new LinkedHashSet<>();

        results.addAll(
                customerRepository.findByFullNameContainingIgnoreCase(keyword)
                        .stream()
                        .map(this::toSummaryDto)
                        .toList()
        );

        results.addAll(
                customerRepository.findBySurnameContainingIgnoreCase(keyword)
                        .stream()
                        .map(this::toSummaryDto)
                        .toList()
        );

        results.addAll(
                customerRepository.findByFirstNameContainingIgnoreCase(keyword)
                        .stream()
                        .map(this::toSummaryDto)
                        .toList()
        );

        return List.copyOf(results);
    }

    @Override
    public CustomerSummaryDto getCustomerById(String customerId) {

        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return toSummaryDto(customer);
    }

    private CustomerSummaryDto toSummaryDto(Customer customer) {
        return CustomerSummaryDto.builder()
                .customerId(customer.getCustomerId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone1())
                .email(customer.getEmail())
                .customerType(customer.getCustomerType())
                .status(customer.getStatus())
                .tier(customer.getTier())
                .build();
    }
}