package com.example.chataiserver.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOverviewDto {

    private String accountNumber;

    private String accountTitle;

    private String currency;

    private String status;

    private BigDecimal bookBalance;

    private BigDecimal holdBalance;

    private BigDecimal loanBalance;

    private BigDecimal dailyLimit;

    private BigDecimal singleLimit;

    private Integer tier;

}