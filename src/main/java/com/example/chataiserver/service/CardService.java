package com.example.chataiserver.service;

import com.example.chataiserver.dto.CustomerCardDto;

import java.util.List;

public interface CardService {
    List<CustomerCardDto> getCustomerCards(String customerId);
}
