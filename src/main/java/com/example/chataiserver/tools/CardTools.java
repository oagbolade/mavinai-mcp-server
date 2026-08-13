package com.example.chataiserver.tools;

import com.example.chataiserver.dto.CustomerCardDto;
import com.example.chataiserver.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardTools {
    private final CardService cardService;

    @Tool(description = "Retrieve customer card request and issued-card metadata. Card PAN is always masked.")
    public List<CustomerCardDto> getCustomerCards(String customerId) { return cardService.getCustomerCards(customerId); }
}
