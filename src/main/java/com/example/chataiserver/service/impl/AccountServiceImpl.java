package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.AccountCustomerNameDto;
import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.dto.CustomerIdentityDto;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.repository.AccountRepository;
import com.example.chataiserver.repository.CustomerRepository;
import com.example.chataiserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<AccountOverviewDto> getCustomerAccounts(String customerId) {

        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::toOverviewDto)
                .toList();

    }

    @Override
    public AccountOverviewDto getAccountOverview(String accountNumber) {

        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        return toOverviewDto(account);

    }

    @Override
    public AccountCustomerNameDto getCustomerNameByAccountNumber(String accountNumber) {
        Account account = findAccount(accountNumber);

        String customerName = customerRepository.findByCustomerId(account.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found for account"))
                .getFullName();

        return AccountCustomerNameDto.builder()
                .accountNumber(account.getAccountNumber())
                .customerName(customerName)
                .build();
    }

    @Override
    public CustomerIdentityDto getCustomerIdByAccountNumber(String accountNumber) {
        Account account = findAccount(accountNumber);
        String fullName = customerRepository.findByCustomerId(account.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found for account"))
                .getFullName();

        return CustomerIdentityDto.builder()
                .customerId(account.getCustomerId())
                .fullName(fullName)
                .build();
    }

    private Account findAccount(String accountNumber) {
        String normalizedAccountNumber = normalizeAccountNumber(accountNumber);
        return accountRepository.findById(normalizedAccountNumber)
                .or(() -> accountRepository.findByNuban(normalizedAccountNumber))
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private String normalizeAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
        return accountNumber.trim();
    }

    private AccountOverviewDto toOverviewDto(Account account) {
        return AccountOverviewDto.builder()
                .accountNumber(account.getAccountNumber())
                .accountTitle(account.getAccountTitle())
                .currency(account.getCurrencyCode())
                .status(account.getStatus())
                .bookBalance(account.getBookBalance())
                .holdBalance(account.getHoldBalance())
                .loanBalance(account.getLoanBalance())
                .dailyLimit(account.getDailyTransactionLimit())
                .singleLimit(account.getSingleTransactionLimit())
                .tier(account.getTier())
                .build();
    }
}
