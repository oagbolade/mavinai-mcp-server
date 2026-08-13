package com.example.chataiserver.tools;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTools {
    private final LoanService loanService;

    @Tool(description = "Retrieve all loan accounts for a customer, including balances, settlement accounts, status, and payment dates.")
    public List<LoanOverviewDto> getCustomerLoans(String customerId) { return loanService.getCustomerLoans(customerId); }

    @Tool(description = "Retrieve detailed loan overview for a loan account number, including outstanding principal, interest, and settlement accounts.")
    public LoanOverviewDto getLoanOverview(String loanAccountNumber) { return loanService.getLoanOverview(loanAccountNumber); }

    @Tool(description = "Retrieve repayment schedule rows for a loan account number.")
    public List<LoanScheduleDto> getLoanSchedule(String loanAccountNumber) { return loanService.getLoanSchedule(loanAccountNumber); }

    @Tool(description = "Retrieve loan repayment and movement history for a loan account over a bounded date range. Max range is 180 days.")
    public List<LoanTransactionDto> getLoanRepaymentHistory(String loanAccountNumber, LocalDate startDate, LocalDate endDate) { return loanService.getLoanRepaymentHistory(loanAccountNumber, startDate, endDate); }

    @Tool(description = "Summarize a customer's loan exposure, outstanding balances, active loans, matured loans, and missed payment indicators.")
    public CustomerLoanSummaryDto getCustomerLoanSummary(String customerId) { return loanService.getCustomerLoanSummary(customerId); }
}
