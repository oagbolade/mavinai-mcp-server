package com.example.chataiserver.tools;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionTools {
    private final TransactionService transactionService;

    @Tool(description = "Retrieve debit, credit, or all transactions for one account. Requires startDate and endDate; max range is 180 days; limit defaults to 50 and caps at 200.")
    public List<TransactionDto> getAccountTransactions(String accountNumber, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit) { return transactionService.getAccountTransactions(accountNumber, startDate, endDate, transactionType, limit); }

    @Tool(description = "Retrieve debit, credit, or all transactions across all CASA accounts owned by a customer. Requires startDate and endDate; max range is 180 days; limit defaults to 50 and caps at 200.")
    public List<TransactionDto> getCustomerTransactions(String customerId, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit) { return transactionService.getCustomerTransactions(customerId, startDate, endDate, transactionType, limit); }

    @Tool(description = "Produce a statement-like account view with opening balance, closing balance, debit totals, credit totals, charges, and transactions for a bounded date range.")
    public AccountStatementDto getAccountStatement(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) { return transactionService.getAccountStatement(accountNumber, startDate, endDate, limit); }
}
