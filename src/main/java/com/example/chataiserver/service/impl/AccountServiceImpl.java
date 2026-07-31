package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.repository.AccountRepository;
import com.example.chataiserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

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