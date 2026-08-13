package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountStatementDto(
        String accountNumber,
        String accountTitle,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        BigDecimal totalDebits,
        BigDecimal totalCredits,
        BigDecimal totalCharges,
        List<TransactionDto> transactions
) {
}
