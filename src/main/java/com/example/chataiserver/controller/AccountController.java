package com.example.chataiserver.controller;

import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/customer/{customerId}")
    public List<AccountOverviewDto> getCustomerAccounts(
            @PathVariable String customerId) {

        return accountService.getCustomerAccounts(customerId);
    }

    @GetMapping("/{accountNumber}/overview")
    public AccountOverviewDto getAccountOverview(
            @PathVariable String accountNumber) {

        return accountService.getAccountOverview(accountNumber);
    }

}