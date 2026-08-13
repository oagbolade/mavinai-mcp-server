package com.example.chataiserver.dto;

import java.math.BigDecimal;

public record CustomerLoanSummaryDto(
        String customerId,
        BigDecimal totalLoanAmount,
        BigDecimal totalBalance,
        BigDecimal principalOutstanding,
        BigDecimal interestOutstanding,
        Long activeLoanCount,
        Long maturedLoanCount,
        Long missedPaymentCount
) {
}
