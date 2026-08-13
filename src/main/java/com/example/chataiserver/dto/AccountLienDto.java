package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountLienDto(
        String accountNumber,
        LocalDate transactionDate,
        LocalDate expiryDate,
        BigDecimal lienAmount,
        String accountTiedTo,
        String reasonCode,
        String lienReason,
        String status
) {
}
