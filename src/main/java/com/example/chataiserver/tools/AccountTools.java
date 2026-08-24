package com.example.chataiserver.tools;

import com.example.chataiserver.dto.AccountCustomerNameDto;
import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.dto.CustomerIdentityDto;
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

    @Tool(description = "Retrieve a customer's full name from an account number or NUBAN. Use this when the only requested result is the name associated with an account.")
    public AccountCustomerNameDto getCustomerNameByAccountNumber(String accountNumber) {
        return accountService.getCustomerNameByAccountNumber(accountNumber);
    }

    @Tool(description = "Retrieve a customer's ID from an account number or NUBAN. Returns the customer ID and full name for confirmation.")
    public CustomerIdentityDto getCustomerIdByAccountNumber(String accountNumber) {
        return accountService.getCustomerIdByAccountNumber(accountNumber);
    }
}
