package com.example.chataiserver.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class AiToolGuards {

    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 200;
    private static final long MAX_DATE_RANGE_DAYS = 180;

    public String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    public int normalizeLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        return Math.min(limit, MAX_LIMIT);
    }

    public void requireDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) > MAX_DATE_RANGE_DAYS) {
            throw new IllegalArgumentException("date range cannot exceed " + MAX_DATE_RANGE_DAYS + " days");
        }
    }

    public String normalizeTransactionType(String transactionType) {
        if (transactionType == null || transactionType.isBlank() || "all".equalsIgnoreCase(transactionType)) {
            return "ALL";
        }
        if ("debit".equalsIgnoreCase(transactionType) || "dr".equalsIgnoreCase(transactionType)) {
            return "DEBIT";
        }
        if ("credit".equalsIgnoreCase(transactionType) || "cr".equalsIgnoreCase(transactionType)) {
            return "CREDIT";
        }
        throw new IllegalArgumentException("transactionType must be debit, credit, or all");
    }

    public String maskIdentifier(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        String trimmed = value.trim();
        if (trimmed.length() <= 4) {
            return "****";
        }
        return "****" + trimmed.substring(trimmed.length() - 4);
    }

    public String maskPhone(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        String digits = value.replaceAll("\\D", "");
        if (digits.length() <= 4) {
            return "****";
        }
        return "****" + digits.substring(digits.length() - 4);
    }

    public String maskEmail(String value) {
        if (value == null || value.isBlank() || !value.contains("@")) {
            return value;
        }
        String[] parts = value.split("@", 2);
        String prefix = parts[0].isEmpty() ? "*" : parts[0].substring(0, 1) + "***";
        return prefix + "@" + parts[1];
    }
}
