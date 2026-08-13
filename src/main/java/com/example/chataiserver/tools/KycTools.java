package com.example.chataiserver.tools;

import com.example.chataiserver.dto.CustomerKycProfileDto;
import com.example.chataiserver.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KycTools {
    private final KycService kycService;

    @Tool(description = "Retrieve customer identity and KYC validation status. BVN, NIN, phone, email, and ID numbers are masked; binary images and signatures are not returned.")
    public CustomerKycProfileDto getCustomerKycProfile(String customerId) { return kycService.getCustomerKycProfile(customerId); }
}
