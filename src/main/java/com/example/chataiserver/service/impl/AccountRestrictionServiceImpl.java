package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.repository.AccountRepository;
import com.example.chataiserver.repository.AccountRestrictionRepository;
import com.example.chataiserver.service.AccountRestrictionService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRestrictionServiceImpl implements AccountRestrictionService {
    private final AccountRestrictionRepository repository;
    private final AccountRepository accountRepository;
    private final AiToolGuards guards;

    public List<AccountLienDto> getAccountLiens(String accountNumber) { return repository.findLiens(requireAccount(accountNumber)); }
    public List<AccountBlockDto> getAccountBlocks(String accountNumber) { return repository.findBlocks(requireAccount(accountNumber)); }
    public List<AccountSignatoryDto> getAccountSignatories(String accountNumber) {
        return repository.findSignatories(requireAccount(accountNumber)).stream()
                .map(s -> new AccountSignatoryDto(s.accountNumber(), s.signatoryName(), guards.maskPhone(s.phone()), guards.maskEmail(s.email()), s.mandateClass(), s.status()))
                .toList();
    }

    private String requireAccount(String accountNumber) {
        String normalized = guards.requireText(accountNumber, "accountNumber");
        Account account = accountRepository.findById(normalized).or(() -> accountRepository.findByNuban(normalized)).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getAccountNumber();
    }
}
