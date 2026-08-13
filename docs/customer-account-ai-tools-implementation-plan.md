# Customer and Account AI Tools Implementation Plan

## Objective

Expand the MCP server with read-only AI tools that let the assistant answer customer, account, transaction, loan, card, KYC, lien, and beneficiary questions using the configured SQL Server database.

The current implementation exposes only:

- `searchCustomers(keyword)`
- `getCustomerById(customerId)`
- `getCustomerAccounts(customerId)`
- `getAccountOverview(accountNumber)`

## Database Context

Datasource is configured in `src/main/resources/application.properties`.

Important discovered tables:

- Customer/account: `tbl_customer`, `tbl_casaaccount`, `tbl_customerbalance`, `tbl_customerbalances`
- Transactions: `tbl_transactions`, `tbl_Transactionshist`, `tbl_dailyTransactions`, `inward_transaction`, `outward_transaction`
- Loans: `tbl_loanaccount`, `tbl_loanscheduledetail`, `tbl_LoanHistory`, `tbl_loanSummary`, `tbl_loanclassification`
- Account restrictions: `tbl_Lien`, `account_block`
- Mandates/signatories: `mandate`, `tbl_AcctSignatory`
- Cards: `Tbl_Cardrequests`, `tbl_CardFileRecords`
- Beneficiaries: `tbl_TransferBeneficiary`
- KYC/reference: `tbl_KYCValidationLog`, `tbl_IDCard`, `tbl_branch`, `tbl_TransactType`, `tbl_tranStatus`

## Implementation Principles

- Keep all new tools read-only.
- Require bounded date ranges and pagination for transaction-style queries.
- Never expose raw images, signatures, encrypted payloads, or unnecessary PII.
- Mask sensitive identifiers such as BVN, NIN, card PAN, phone numbers, and email where full values are not required.
- Prefer DTO projections over returning JPA entities directly.
- Use explicit native SQL queries where table/column names do not map cleanly to Java naming conventions.
- Add service-layer validation for account/customer existence before querying related records.
- Keep tool descriptions specific so the model selects the right tool.

## Phase 1: Core Transaction Tools

### Tool: `getAccountTransactions`

Purpose: Retrieve debit, credit, or all transactions on one account.

Suggested signature:

```java
List<TransactionDto> getAccountTransactions(
    String accountNumber,
    LocalDate startDate,
    LocalDate endDate,
    String transactionType,
    Integer limit
)
```

Source tables:

- `tbl_transactions`
- `tbl_Transactionshist`
- `tbl_dailyTransactions`

Important columns:

- `AccountNumber`
- `Trancode`
- `TranDate`
- `Valuedate`
- `Narration`
- `TranAmount`
- `BaseAmount`
- `charge`
- `Reversal`
- `status`
- `refno`
- `TranType`
- `Channel`

Implementation notes:

- Interpret debit/credit using `TranType` where reliable.
- Join `tbl_TransactType` by `TranCode` to enrich transaction names.
- Default `limit` to 50 and cap at 200.
- Require `startDate` and `endDate`; reject ranges above a configured maximum, for example 180 days.

### Tool: `getCustomerTransactions`

Purpose: Retrieve transactions across all CASA accounts owned by a customer.

Suggested signature:

```java
List<TransactionDto> getCustomerTransactions(
    String customerId,
    LocalDate startDate,
    LocalDate endDate,
    String transactionType,
    Integer limit
)
```

Source tables:

- `tbl_casaaccount`
- Transaction tables listed above

Implementation notes:

- Resolve customer accounts from `tbl_casaaccount.customerid`.
- Query transactions with `AccountNumber IN (...)`.
- Include `accountNumber` and `accountTitle` in the response.

### Tool: `getAccountStatement`

Purpose: Produce a statement-like view with opening balance, transactions, and closing balance.

Suggested signature:

```java
AccountStatementDto getAccountStatement(
    String accountNumber,
    LocalDate startDate,
    LocalDate endDate,
    Integer limit
)
```

Source tables:

- `tbl_customerbalance`
- `tbl_customerbalances`
- Transaction tables

Implementation notes:

- Use balance tables where available for running balance.
- Fall back to current account balance plus transaction aggregation only if historical balance data is incomplete.
- Return summary totals for debits, credits, charges, opening balance, and closing balance.

## Phase 2: Loan Tools

### Tool: `getCustomerLoans`

Purpose: Retrieve all loans for a customer.

Suggested signature:

```java
List<LoanOverviewDto> getCustomerLoans(String customerId)
```

Source table:

- `tbl_loanaccount`

Important columns:

- `CustomerID`
- `AccountNumber`
- `FullName`
- `ProductCode`
- `Currency`
- `StartDate`
- `MatDate`
- `LoanTerm`
- `IntRate`
- `LoanAmount`
- `LoanPurpose`
- `SettlementAcct1`
- `SettlementAcct2`
- `Status`
- `currentBalance`
- `TotalPrincipal`
- `TotalInterest`
- `principalpaid`
- `interestpaid`
- `interestdue`
- `principaldue`
- `nextpaymentdate`
- `lastpaymentdate`

### Tool: `getLoanOverview`

Purpose: Retrieve detailed loan information by loan account number.

Suggested signature:

```java
LoanOverviewDto getLoanOverview(String loanAccountNumber)
```

Source table:

- `tbl_loanaccount`

Implementation notes:

- Include settlement CASA account numbers.
- Include outstanding principal/interest and payment dates.

### Tool: `getLoanSchedule`

Purpose: Retrieve repayment schedule for a loan.

Suggested signature:

```java
List<LoanScheduleDto> getLoanSchedule(String loanAccountNumber)
```

Source table:

- `tbl_loanscheduledetail`

Important columns:

- `AccountNumber`
- `TotalRepayAmt`
- `PrincipalAmt`
- `InterestAmt`
- `OutstandingBal`
- `InstalDue`
- `InstalPay`
- `Date_due`
- `PaymentStatus`
- `principal_due`
- `unpaidprinc`
- `paidprinc`
- `unpaidinterestamt`
- `paidinterestamt`
- `repay_date`

### Tool: `getLoanRepaymentHistory`

Purpose: Retrieve loan repayment and loan movement history.

Suggested signature:

```java
List<LoanTransactionDto> getLoanRepaymentHistory(
    String loanAccountNumber,
    LocalDate startDate,
    LocalDate endDate
)
```

Source table:

- `tbl_LoanHistory`

Important columns:

- `accountnumber`
- `Trandate`
- `tranAmount`
- `Trantype`
- `Narration`
- `FromModule`
- `postseq`
- `actioncode`
- `trancode`

### Tool: `getCustomerLoanSummary`

Purpose: Summarize customer loan exposure and repayment posture.

Suggested signature:

```java
CustomerLoanSummaryDto getCustomerLoanSummary(String customerId)
```

Source tables:

- `tbl_loanaccount`
- `tbl_loanSummary`

Implementation notes:

- Return total loan amount, total balance, principal outstanding, interest outstanding, matured loans, active loans, and missed-payment indicators.

## Phase 3: Account Restrictions and Access Tools

### Tool: `getAccountLiens`

Purpose: Retrieve active and historical liens on an account.

Suggested signature:

```java
List<AccountLienDto> getAccountLiens(String accountNumber)
```

Source table:

- `tbl_Lien`

Important columns:

- `Accountnumber`
- `TranDate`
- `ExpiryDate`
- `LienAmount`
- `AccountTiedTo`
- `ReasonCode`
- `LienReason`
- `Status`

### Tool: `getAccountBlocks`

Purpose: Retrieve account block information.

Suggested signature:

```java
List<AccountBlockDto> getAccountBlocks(String accountNumber)
```

Source table:

- `account_block`

Important columns:

- `account_block_name`
- `account_block_number`
- `account_block_status`
- `blocked_at`
- `hold_number`
- `nip_ref`
- `qt_ref`
- `unblocked_at`

### Tool: `getAccountSignatories`

Purpose: Retrieve account signatories.

Suggested signature:

```java
List<AccountSignatoryDto> getAccountSignatories(String accountNumber)
```

Source table:

- `tbl_AcctSignatory`

Implementation notes:

- Mask phone and email by default.
- Do not expose signature images.

## Phase 4: Transfers, Beneficiaries, Mandates, and Cards

### Tool: `getInwardTransfers`

Purpose: Retrieve inward transfer records for an account.

Suggested signature:

```java
List<TransferDto> getInwardTransfers(
    String accountNumber,
    LocalDate startDate,
    LocalDate endDate,
    Integer limit
)
```

Source table:

- `inward_transaction`

Filter:

- `beneficiary_account_number = accountNumber`

### Tool: `getOutwardTransfers`

Purpose: Retrieve outward transfer records for an account.

Suggested signature:

```java
List<TransferDto> getOutwardTransfers(
    String accountNumber,
    LocalDate startDate,
    LocalDate endDate,
    Integer limit
)
```

Source table:

- `outward_transaction`

Filter:

- `debit_account_number = accountNumber`

### Tool: `getCustomerBeneficiaries`

Purpose: Retrieve saved transfer beneficiaries for a customer.

Suggested signature:

```java
List<BeneficiaryDto> getCustomerBeneficiaries(String customerId)
```

Source table:

- `tbl_TransferBeneficiary`

### Tool: `getAccountMandates`

Purpose: Retrieve mandate records involving an account.

Suggested signature:

```java
List<MandateDto> getAccountMandates(String accountNumber)
```

Source table:

- `mandate`

Filter:

- `debit_account_number = accountNumber`
- `beneficiary_account_number = accountNumber`

### Tool: `getCustomerCards`

Purpose: Retrieve customer card request and issued-card metadata.

Suggested signature:

```java
List<CustomerCardDto> getCustomerCards(String customerId)
```

Source tables:

- `Tbl_Cardrequests`
- `tbl_CardFileRecords`

Implementation notes:

- Return masked PAN only.
- Include card type, scheme, status, issue date, expiry date, and linked account numbers.

## Phase 5: Customer Profile, KYC, and Reference Tools

### Tool: `getCustomerKycProfile`

Purpose: Retrieve customer identity and KYC validation status.

Suggested signature:

```java
CustomerKycProfileDto getCustomerKycProfile(String customerId)
```

Source tables:

- `tbl_customer`
- `tbl_KYCValidationLog`
- `tbl_IDCard`

Implementation notes:

- Mask BVN, NIN, phone, and email unless full disclosure is explicitly required by the business policy.
- Exclude binary image/signature fields.

### Tool: `getBranchDetails`

Purpose: Resolve branch metadata for customer/account branch codes.

Suggested signature:

```java
BranchDto getBranchDetails(String branchCode)
```

Source table:

- `tbl_branch`

### Tool: `getTransactionReference`

Purpose: Trace a transaction by reference, session ID, or transaction ID.

Suggested signature:

```java
TransactionTraceDto getTransactionReference(String reference)
```

Source tables:

- `tbl_transactions`
- `tbl_dailyTransactions`
- `tbl_Transactionshist`
- `inward_transaction`
- `outward_transaction`

Search fields:

- `refno`
- `session_id`
- `transaction_id`
- `reference_id`
- `payment_reference`

## Proposed Package Structure

Add DTOs:

- `dto/TransactionDto.java`
- `dto/AccountStatementDto.java`
- `dto/LoanOverviewDto.java`
- `dto/LoanScheduleDto.java`
- `dto/LoanTransactionDto.java`
- `dto/CustomerLoanSummaryDto.java`
- `dto/AccountLienDto.java`
- `dto/AccountBlockDto.java`
- `dto/AccountSignatoryDto.java`
- `dto/TransferDto.java`
- `dto/BeneficiaryDto.java`
- `dto/MandateDto.java`
- `dto/CustomerCardDto.java`
- `dto/CustomerKycProfileDto.java`
- `dto/BranchDto.java`
- `dto/TransactionTraceDto.java`

Add repositories:

- `repository/TransactionRepository.java`
- `repository/LoanRepository.java`
- `repository/AccountRestrictionRepository.java`
- `repository/TransferRepository.java`
- `repository/CardRepository.java`
- `repository/KycRepository.java`
- `repository/ReferenceRepository.java`

Add services:

- `service/TransactionService.java`
- `service/LoanService.java`
- `service/AccountRestrictionService.java`
- `service/TransferService.java`
- `service/CardService.java`
- `service/KycService.java`
- `service/ReferenceService.java`

Add tool classes:

- `tools/TransactionTools.java`
- `tools/LoanTools.java`
- `tools/AccountRestrictionTools.java`
- `tools/TransferTools.java`
- `tools/CardTools.java`
- `tools/KycTools.java`
- `tools/ReferenceTools.java`

## Suggested Build Order

- [x] Add DTOs and native query repository methods for transaction lookup.
- [x] Add `TransactionService` and `TransactionTools`.
- [x] Add loan DTOs, repository methods, `LoanService`, and `LoanTools`.
- [x] Add account restriction tools for liens, blocks, and signatories.
- [x] Add transfer, beneficiary, mandate, and card tools.
- [x] Add KYC and branch/reference tools.
- [x] Add validation, masking helpers, date-range limits, and pagination caps.
- [x] Run `mvn clean compile`.
- [x] Skip focused service/repository tests per implementation instruction.

## Validation Checklist

- Application starts successfully.
- MCP tool registry exposes all new tools.
- Each tool rejects blank identifiers.
- Date-range queries enforce maximum range and result limit.
- Transaction tools return deterministic ordering by transaction date and sequence/reference.
- Sensitive fields are masked.
- Native SQL aliases match DTO projection fields.
- No write/update/delete SQL is introduced.

## Security and Compliance Notes

- Credentials should be moved out of committed `application.properties` and supplied through environment variables.
- Tool responses should not include binary identity images, raw signatures, encrypted request/response payloads, full BVN, full NIN, or full card PAN.
- If these tools will be exposed outside a trusted internal environment, add authentication, authorization, audit logging, and per-tool access control before enabling them.
