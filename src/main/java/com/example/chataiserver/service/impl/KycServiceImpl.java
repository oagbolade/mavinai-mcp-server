package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.CustomerKycProfileDto;
import com.example.chataiserver.repository.KycRepository;
import com.example.chataiserver.service.KycService;
import com.example.chataiserver.util.AiToolGuards;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KycServiceImpl implements KycService {
    private final KycRepository kycRepository;
    private final AiToolGuards guards;

    public CustomerKycProfileDto getCustomerKycProfile(String customerId) {
        CustomerKycProfileDto profile = kycRepository.findCustomerKycProfile(guards.requireText(customerId, "customerId"));
        return new CustomerKycProfileDto(profile.customerId(), profile.fullName(), profile.customerType(), profile.status(), profile.tier(), profile.branchCode(), guards.maskPhone(profile.phone()), guards.maskEmail(profile.email()), guards.maskIdentifier(profile.bvn()), guards.maskIdentifier(profile.nin()), profile.idCardType(), guards.maskIdentifier(profile.idCardNumber()), profile.kycValidationStatus(), profile.lastValidationDate());
    }
}
