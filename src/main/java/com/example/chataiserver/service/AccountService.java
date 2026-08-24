package com.example.chataiserver.service;

import com.example.chataiserver.dto.AccountOverviewDto;
import com.example.chataiserver.dto.AccountCustomerNameDto;
import com.example.chataiserver.dto.CustomerIdentityDto;

import java.util.List;

public interface AccountService {

    List<AccountOverviewDto> getCustomerAccounts(String customerId);

    AccountOverviewDto getAccountOverview(String accountNumber);

    AccountCustomerNameDto getCustomerNameByAccountNumber(String accountNumber);

    CustomerIdentityDto getCustomerIdByAccountNumber(String accountNumber);

}
