package com.example.chataiserver.service;

import com.example.chataiserver.dto.BranchDto;
import com.example.chataiserver.dto.TransactionTraceDto;

public interface ReferenceService {
    BranchDto getBranchDetails(String branchCode);
    TransactionTraceDto getTransactionReference(String reference);
}
