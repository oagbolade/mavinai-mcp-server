package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.repository.ReferenceRepository;
import com.example.chataiserver.repository.TransactionRepository;
import com.example.chataiserver.repository.TransferRepository;
import com.example.chataiserver.service.ReferenceService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {
    private final ReferenceRepository referenceRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;
    private final AiToolGuards guards;

    public BranchDto getBranchDetails(String branchCode) {
        return referenceRepository.findBranchDetails(guards.requireText(branchCode, "branchCode"));
    }

    public TransactionTraceDto getTransactionReference(String reference) {
        String normalized = guards.requireText(reference, "reference");
        int limit = guards.normalizeLimit(50);
        return new TransactionTraceDto(normalized, transactionRepository.findByReference(normalized, limit), transferRepository.findTransfersByReference(normalized, limit));
    }
}
