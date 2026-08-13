package com.example.chataiserver.service;

import com.example.chataiserver.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {
    List<LoanOverviewDto> getCustomerLoans(String customerId);
    LoanOverviewDto getLoanOverview(String loanAccountNumber);
    List<LoanScheduleDto> getLoanSchedule(String loanAccountNumber);
    List<LoanTransactionDto> getLoanRepaymentHistory(String loanAccountNumber, LocalDate startDate, LocalDate endDate);
    CustomerLoanSummaryDto getCustomerLoanSummary(String customerId);
}
