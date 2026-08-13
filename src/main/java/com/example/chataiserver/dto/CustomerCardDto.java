package com.example.chataiserver.dto;

import java.time.LocalDate;

public record CustomerCardDto(
        String customerId,
        String accountNumber,
        String maskedPan,
        String cardType,
        String cardScheme,
        String status,
        LocalDate issueDate,
        LocalDate expiryDate,
        String sourceTable
) {
}
