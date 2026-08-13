package com.example.chataiserver.service;

import com.example.chataiserver.dto.CustomerKycProfileDto;

public interface KycService {
    CustomerKycProfileDto getCustomerKycProfile(String customerId);
}
