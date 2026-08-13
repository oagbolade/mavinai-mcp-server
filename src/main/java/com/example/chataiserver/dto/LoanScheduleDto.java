package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanScheduleDto(
        String accountNumber,
        BigDecimal totalRepaymentAmount,
        BigDecimal principalAmount,
        BigDecimal interestAmount,
        BigDecimal outstandingBalance,
        BigDecimal installmentDue,
        BigDecimal installmentPaid,
        LocalDate dueDate,
        String paymentStatus,
        BigDecimal principalDue,
        BigDecimal unpaidPrincipal,
        BigDecimal paidPrincipal,
        BigDecimal unpaidInterestAmount,
        BigDecimal paidInterestAmount,
        LocalDate repaymentDate
) {
}
