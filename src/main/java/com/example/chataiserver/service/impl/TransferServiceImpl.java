package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.repository.*;
import com.example.chataiserver.service.TransferService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AiToolGuards guards;

    public List<TransferDto> getInwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) {
        guards.requireDateRange(startDate, endDate);
        return transferRepository.findInwardTransfers(requireAccount(accountNumber), startDate, endDate, guards.normalizeLimit(limit));
    }

    public List<TransferDto> getOutwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) {
        guards.requireDateRange(startDate, endDate);
        return transferRepository.findOutwardTransfers(requireAccount(accountNumber), startDate, endDate, guards.normalizeLimit(limit));
    }

    public List<BeneficiaryDto> getCustomerBeneficiaries(String customerId) {
        String normalized = guards.requireText(customerId, "customerId");
        customerRepository.findByCustomerId(normalized).orElseThrow(() -> new RuntimeException("Customer not found"));
        return transferRepository.findCustomerBeneficiaries(normalized);
    }

    public List<MandateDto> getAccountMandates(String accountNumber) { return transferRepository.findAccountMandates(requireAccount(accountNumber)); }

    private String requireAccount(String accountNumber) {
        String normalized = guards.requireText(accountNumber, "accountNumber");
        Account account = accountRepository.findById(normalized).or(() -> accountRepository.findByNuban(normalized)).orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getAccountNumber();
    }
}
