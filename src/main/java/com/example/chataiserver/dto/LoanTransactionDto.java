package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanTransactionDto(
        String accountNumber,
        LocalDate transactionDate,
        BigDecimal transactionAmount,
        String transactionType,
        String narration,
        String fromModule,
        String postSequence,
        String actionCode,
        String transactionCode
) {
}
