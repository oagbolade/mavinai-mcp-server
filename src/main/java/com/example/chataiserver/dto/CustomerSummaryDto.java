package com.example.chataiserver.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDto {

    private String customerId;
    private String fullName;
    private String phone;
    private String email;
    private String customerType;
    private String status;
    private Integer tier;

}