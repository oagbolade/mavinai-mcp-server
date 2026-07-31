package com.example.chataiserver.repository;

import com.example.chataiserver.dto.CustomerSummaryDto;
import com.example.chataiserver.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerId(String customerId);

    Optional<Customer> findByBvn(String bvn);

    List<Customer> findByFullNameContainingIgnoreCase(String name);

    List<Customer> findBySurnameContainingIgnoreCase(String surname);

    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);

}