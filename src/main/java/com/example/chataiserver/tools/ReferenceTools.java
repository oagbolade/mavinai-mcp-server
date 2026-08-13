package com.example.chataiserver.tools;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceTools {
    private final ReferenceService referenceService;

    @Tool(description = "Resolve branch metadata by branch code.")
    public BranchDto getBranchDetails(String branchCode) { return referenceService.getBranchDetails(branchCode); }

    @Tool(description = "Trace a transaction by reference, session ID, transaction ID, reference ID, or payment reference across transaction and transfer tables.")
    public TransactionTraceDto getTransactionReference(String reference) { return referenceService.getTransactionReference(reference); }
}
