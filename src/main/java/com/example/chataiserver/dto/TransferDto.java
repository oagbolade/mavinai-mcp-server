package com.example.chataiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(
        String direction,
        String debitAccountNumber,
        String beneficiaryAccountNumber,
        String beneficiaryName,
        String beneficiaryBank,
        BigDecimal amount,
        String narration,
        String status,
        String sessionId,
        String transactionId,
        String reference,
        LocalDateTime transactionDate
) {
}
