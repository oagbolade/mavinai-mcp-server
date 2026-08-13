package com.example.chataiserver.dto;

public record AccountSignatoryDto(
        String accountNumber,
        String signatoryName,
        String phone,
        String email,
        String mandateClass,
        String status
) {
}
