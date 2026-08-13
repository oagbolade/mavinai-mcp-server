package com.example.chataiserver.dto;

import java.time.LocalDateTime;

public record AccountBlockDto(
        String blockName,
        String accountNumber,
        String status,
        LocalDateTime blockedAt,
        String holdNumber,
        String nipReference,
        String qtReference,
        LocalDateTime unblockedAt
) {
}
