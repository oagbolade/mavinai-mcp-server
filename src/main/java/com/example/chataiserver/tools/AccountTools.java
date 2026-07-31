package com.example.chataiserver.tools;

import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTools {

    private final AccountService accountService;

    @Tool(description = "Retrieve all accounts belonging to a customer")
    public List<AccountOverviewDto> getCustomerAccounts(String customerId) {
        return accountService.getCustomerAccounts(customerId);
    }

    @Tool(description = "Retrieve an account overview")
    public AccountOverviewDto getAccountOverview(String accountNumber) {
        return accountService.getAccountOverview(accountNumber);
    }
}