package com.example.chataiserver.service;

import com.example.chataiserver.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface TransferService {
    List<TransferDto> getInwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit);
    List<TransferDto> getOutwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit);
    List<BeneficiaryDto> getCustomerBeneficiaries(String customerId);
    List<MandateDto> getAccountMandates(String accountNumber);
}
