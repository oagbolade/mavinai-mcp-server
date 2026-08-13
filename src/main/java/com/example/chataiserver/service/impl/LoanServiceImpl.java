package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.repository.CustomerRepository;
import com.example.chataiserver.repository.LoanRepository;
import com.example.chataiserver.service.LoanService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final AiToolGuards guards;

    public List<LoanOverviewDto> getCustomerLoans(String customerId) {
        return loanRepository.findCustomerLoans(requireCustomer(customerId));
    }

    public LoanOverviewDto getLoanOverview(String loanAccountNumber) {
        return loanRepository.findLoanOverview(guards.requireText(loanAccountNumber, "loanAccountNumber"));
    }

    public List<LoanScheduleDto> getLoanSchedule(String loanAccountNumber) {
        return loanRepository.findLoanSchedule(guards.requireText(loanAccountNumber, "loanAccountNumber"));
    }

    public List<LoanTransactionDto> getLoanRepaymentHistory(String loanAccountNumber, LocalDate startDate, LocalDate endDate) {
        guards.requireDateRange(startDate, endDate);
        return loanRepository.findLoanHistory(guards.requireText(loanAccountNumber, "loanAccountNumber"), startDate, endDate);
    }

    public CustomerLoanSummaryDto getCustomerLoanSummary(String customerId) {
        return loanRepository.summarizeCustomerLoans(requireCustomer(customerId));
    }

    private String requireCustomer(String customerId) {
        String normalized = guards.requireText(customerId, "customerId");
        customerRepository.findByCustomerId(normalized).orElseThrow(() -> new RuntimeException("Customer not found"));
        return normalized;
    }
}
