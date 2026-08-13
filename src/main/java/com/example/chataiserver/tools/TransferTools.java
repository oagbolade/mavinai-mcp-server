package com.example.chataiserver.tools;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferTools {
    private final TransferService service;

    @Tool(description = "Retrieve inward transfer records for an account. Requires startDate and endDate; max range is 180 days; limit defaults to 50 and caps at 200.")
    public List<TransferDto> getInwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) { return service.getInwardTransfers(accountNumber, startDate, endDate, limit); }

    @Tool(description = "Retrieve outward transfer records for an account. Requires startDate and endDate; max range is 180 days; limit defaults to 50 and caps at 200.")
    public List<TransferDto> getOutwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, Integer limit) { return service.getOutwardTransfers(accountNumber, startDate, endDate, limit); }

    @Tool(description = "Retrieve saved transfer beneficiaries for a customer.")
    public List<BeneficiaryDto> getCustomerBeneficiaries(String customerId) { return service.getCustomerBeneficiaries(customerId); }

    @Tool(description = "Retrieve mandate records where the account is the debit or beneficiary account.")
    public List<MandateDto> getAccountMandates(String accountNumber) { return service.getAccountMandates(accountNumber); }
}
