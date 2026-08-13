package com.example.chataiserver.dto;

import java.util.List;

public record TransactionTraceDto(
        String reference,
        List<TransactionDto> accountTransactions,
        List<TransferDto> transfers
) {
}
