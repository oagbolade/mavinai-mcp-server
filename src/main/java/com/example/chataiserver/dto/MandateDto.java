package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MandateDto(
        String debitAccountNumber,
        String beneficiaryAccountNumber,
        String beneficiaryName,
        BigDecimal amount,
        String frequency,
        String status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String reference
) {
}
