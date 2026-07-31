package com.example.chataiserver.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_casaaccount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "accountnumber")
    private String accountNumber;

    @Column(name = "branchcode")
    private String branchCode;

    @Column(name = "productcode")
    private String productCode;

    @Column(name = "accounttitle")
    private String accountTitle;

    @Column(name = "customerid")
    private String customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "customerid",
            referencedColumnName = "CustomerId",
            insertable = false,
            updatable = false
    )
    private Customer customer;

    @Column(name = "officercode")
    private String officerCode;

    @Column(name = "CurrencyCode")
    private String currencyCode;

    @Column(name = "DateOpened")
    private LocalDate dateOpened;

    @Column(name = "Status")
    private String status;

    @Column(name = "BKBalance")
    private BigDecimal bookBalance;

    @Column(name = "LNbalance")
    private BigDecimal loanBalance;

    @Column(name = "ODLimit")
    private BigDecimal overdraftLimit;

    @Column(name = "HoldBal")
    private BigDecimal holdBalance;

    @Column(name = "lien")
    private BigDecimal lien;

    @Column(name = "LastCRDate")
    private LocalDate lastCreditDate;

    @Column(name = "LastDRDate")
    private LocalDate lastDebitDate;

    @Column(name = "Tier")
    private Integer tier;

    @Column(name = "nuban")
    private String nuban;

    @Column(name = "DailyTranLimit")
    private BigDecimal dailyTransactionLimit;

    @Column(name = "singleTranLimit")
    private BigDecimal singleTransactionLimit;

    @Column(name = "DailyCummDRlimit")
    private BigDecimal dailyCumulativeDebitLimit;
}