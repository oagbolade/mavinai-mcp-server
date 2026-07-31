package com.example.chataiserver.service;

import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.model.Account;

import java.util.List;

public interface AccountService {

    List<AccountOverviewDto> getCustomerAccounts(String customerId);

    AccountOverviewDto getAccountOverview(String accountNumber);

}