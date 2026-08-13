package com.example.chataiserver.service;

import com.example.chataiserver.dto.*;

import java.util.List;

public interface AccountRestrictionService {
    List<AccountLienDto> getAccountLiens(String accountNumber);
    List<AccountBlockDto> getAccountBlocks(String accountNumber);
    List<AccountSignatoryDto> getAccountSignatories(String accountNumber);
}
