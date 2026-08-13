package com.example.chataiserver.dto;

public record BranchDto(
        String branchCode,
        String branchName,
        String address,
        String city,
        String state,
        String status
) {
}
