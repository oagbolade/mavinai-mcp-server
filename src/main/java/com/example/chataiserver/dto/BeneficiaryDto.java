package com.example.chataiserver.dto;

public record BeneficiaryDto(
        String customerId,
        String beneficiaryName,
        String beneficiaryAccountNumber,
        String beneficiaryBank,
        String beneficiaryBankCode,
        String beneficiaryType,
        String status
) {
}
