package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanOverviewDto(
        String customerId,
        String accountNumber,
        String fullName,
        String productCode,
        String currency,
        LocalDate startDate,
        LocalDate maturityDate,
        Integer loanTerm,
        BigDecimal interestRate,
        BigDecimal loanAmount,
        String loanPurpose,
        String settlementAccount1,
        String settlementAccount2,
        String status,
        BigDecimal currentBalance,
        BigDecimal totalPrincipal,
        BigDecimal totalInterest,
        BigDecimal principalPaid,
        BigDecimal interestPaid,
        BigDecimal principalDue,
        BigDecimal interestDue,
        LocalDate nextPaymentDate,
        LocalDate lastPaymentDate
) {
}
