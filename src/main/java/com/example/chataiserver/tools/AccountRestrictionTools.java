package com.example.chataiserver.tools;

import com.example.chataiserver.dto.*;
import com.example.chataiserver.service.AccountRestrictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountRestrictionTools {
    private final AccountRestrictionService service;

    @Tool(description = "Retrieve active and historical liens for an account.")
    public List<AccountLienDto> getAccountLiens(String accountNumber) { return service.getAccountLiens(accountNumber); }

    @Tool(description = "Retrieve account block records and unblock metadata for an account.")
    public List<AccountBlockDto> getAccountBlocks(String accountNumber) { return service.getAccountBlocks(accountNumber); }

    @Tool(description = "Retrieve account signatories with phone and email masked. Signature images are never returned.")
    public List<AccountSignatoryDto> getAccountSignatories(String accountNumber) { return service.getAccountSignatories(accountNumber); }
}
