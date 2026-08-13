package com.example.chataiserver.service;

import com.example.chataiserver.dto.AccountStatementDto;
import com.example.chataiserver.dto.TransactionDto;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    List<TransactionDto> getAccountTransactions(String accountNumber, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit);
    List<TransactionDto> getCustomerTransactions(String customerId, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit);
    AccountStatementDto getAccountStatement(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit);
}
