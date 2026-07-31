package com.example.chataiserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CustomerId", unique = true)
    private String customerId;

    @Column(name = "title")
    private String title;

    @Column(name = "SurName")
    private String surname;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "othername")
    private String otherName;

    @Column(name = "Fullname")
    private String fullName;

    @Column(name = "CustomerType")
    private String customerType;

    @Column(name = "DOB")
    private LocalDate dateOfBirth;

    @Column(name = "sex")
    private String gender;

    @Column(name = "Nationality")
    private String nationality;

    @Column(name = "Occupation")
    private String occupation;

    @Column(name = "Address")
    private String address;

    @Column(name = "Address2")
    private String address2;

    @Column(name = "Phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;

    @Column(name = "Email")
    private String email;

    @Column(name = "bvn")
    private String bvn;

    @Column(name = "NIN")
    private String nin;

    @Column(name = "Tin")
    private String tin;

    @Column(name = "Status")
    private String status;

    @Column(name = "branchcode")
    private String branchCode;

    @Column(name = "AcctOfficer")
    private String accountOfficer;

    @Column(name = "CreateDate")
    private LocalDate createDate;

    @Column(name = "Tier")
    private Integer tier;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}

