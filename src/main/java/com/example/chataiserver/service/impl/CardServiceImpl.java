package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.CustomerCardDto;
import com.example.chataiserver.repository.CardRepository;
import com.example.chataiserver.repository.CustomerRepository;
import com.example.chataiserver.service.CardService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final AiToolGuards guards;

    public List<CustomerCardDto> getCustomerCards(String customerId) {
        String normalized = guards.requireText(customerId, "customerId");
        customerRepository.findByCustomerId(normalized).orElseThrow(() -> new RuntimeException("Customer not found"));
        return cardRepository.findCustomerCards(normalized).stream()
                .map(c -> new CustomerCardDto(c.customerId(), c.accountNumber(), guards.maskIdentifier(c.maskedPan()), c.cardType(), c.cardScheme(), c.status(), c.issueDate(), c.expiryDate(), c.sourceTable()))
                .toList();
    }
}
