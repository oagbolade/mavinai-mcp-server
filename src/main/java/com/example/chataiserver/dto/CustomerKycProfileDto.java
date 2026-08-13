package com.example.chataiserver.dto;

import java.time.LocalDate;

public record CustomerKycProfileDto(
        String customerId,
        String fullName,
        String customerType,
        String status,
        Integer tier,
        String branchCode,
        String phone,
        String email,
        String bvn,
        String nin,
        String idCardType,
        String idCardNumber,
        String kycValidationStatus,
        LocalDate lastValidationDate
) {
}
