package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.repository.AccountRepository;
import com.example.chataiserver.repository.CustomerRepository;
import com.example.chataiserver.repository.TransactionRepository;
import com.example.chataiserver.service.TransactionService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AiToolGuards guards;

    @Override
    public List<TransactionDto> getAccountTransactions(String accountNumber, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit) {
        Account account = requireAccount(accountNumber);
        guards.requireDateRange(startDate, endDate);
        return transactionRepository.findAccountTransactions(account.getAccountNumber(), startDate, endDate, guards.normalizeTransactionType(transactionType), guards.normalizeLimit(limit));
    }

    @Override
    public List<TransactionDto> getCustomerTransactions(String customerId, LocalDate startDate, LocalDate endDate, String transactionType, Integer limit) {
        String normalizedCustomerId = requireCustomer(customerId);
        guards.requireDateRange(startDate, endDate);
        List<String> accounts = accountRepository.findByCustomerId(normalizedCustomerId).stream().map(Account::getAccountNumber).toList();
        if (accounts.isEmpty()) {
            return List.of();
        }
        return transactionRepository.findTransactions(accounts, startDate, endDate, guards.normalizeTransactionType(transactionType), guards.normalizeLimit(limit));
    }

    @Override
    public AccountStatementDto getAccountStatement(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) {
        Account account = requireAccount(accountNumber);
        guards.requireDateRange(startDate, endDate);
        List<TransactionDto> transactions = transactionRepository.findAccountTransactions(account.getAccountNumber(), startDate, endDate, "ALL", guards.normalizeLimit(limit));
        BigDecimal totalDebits = sum(transactions, true, false);
        BigDecimal totalCredits = sum(transactions, false, true);
        BigDecimal totalCharges = transactions.stream().map(TransactionDto::charge).map(this::zeroIfNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal closingBalance = zeroIfNull(account.getBookBalance());
        BigDecimal openingBalance = closingBalance.subtract(totalCredits).add(totalDebits).add(totalCharges);
        return new AccountStatementDto(account.getAccountNumber(), account.getAccountTitle(), startDate, endDate, openingBalance, closingBalance, totalDebits, totalCredits, totalCharges, transactions);
    }

    private Account requireAccount(String accountNumber) {
        String normalized = guards.requireText(accountNumber, "accountNumber");
        return accountRepository.findById(normalized).or(() -> accountRepository.findByNuban(normalized)).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private String requireCustomer(String customerId) {
        String normalized = guards.requireText(customerId, "customerId");
        customerRepository.findByCustomerId(normalized).orElseThrow(() -> new RuntimeException("Customer not found"));
        return normalized;
    }

    private BigDecimal sum(List<TransactionDto> transactions, boolean debit, boolean credit) {
        return transactions.stream().filter(t -> matches(t.transactionType(), debit, credit)).map(TransactionDto::transactionAmount).map(this::zeroIfNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean matches(String transactionType, boolean debit, boolean credit) {
        String type = transactionType == null ? "" : transactionType.trim().toUpperCase();
        return (debit && (type.equals("DR") || type.equals("DEBIT"))) || (credit && (type.equals("CR") || type.equals("CREDIT")));
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
