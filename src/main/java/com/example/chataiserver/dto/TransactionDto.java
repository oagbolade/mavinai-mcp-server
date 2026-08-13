package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(
        String accountNumber,
        String accountTitle,
        LocalDate transactionDate,
        LocalDate valueDate,
        String transactionCode,
        String transactionName,
        String transactionType,
        String narration,
        BigDecimal transactionAmount,
        BigDecimal baseAmount,
        BigDecimal charge,
        String reversal,
        String status,
        String referenceNumber,
        String channel,
        String sourceTable
) {
}
