# Marvinai Team Demo Script

Live DB: IPERUDB (SQL Server). All 20 scripts below return real records — verified against the current data.

## Demo Scripts (20)

### 1. Customer search — `searchCustomers`
> "Find a customer with the name SODEINDE"

→ SODEINDE OLUGBENGA DANIEL M (customer 20050)

### 2. Customer search — `searchCustomers`
> "Search for a customer whose surname is OSHO"

→ OSHO ADEDAYO AKANMU (customer 6260)

### 3. Customer by ID — `getCustomerById`
> "Give me the profile of customer 22689"

→ Pouros Mayer leo

### 4. Customer by ID — `getCustomerById`
> "Retrieve customer 22605"

→ Alebiosu Oyeleye Tester

### 5. Customer accounts — `getCustomerAccounts`
> "List all accounts belonging to customer 20050"

→ SODEINDE OLUGBENGA DANIEL (incl. account 2000192797)

### 6. Account overview — `getAccountOverview`
> "Give me the account overview of account 2000192797"

→ SODEINDE OLUGBENGA DANIEL, balance 7,976.23

### 7. Account transactions — `getAccountTransactions`
> "Show me the transactions for account 2000192797 from 2025-12-01 to 2026-01-31"

→ hundreds of transactions returned

### 8. Customer transactions — `getCustomerTransactions`
> "Show me transactions across all accounts of customer 15886 from 2025-12-01 to 2026-01-31"

→ EMMANUEL HANNAH ADIJAT transactions

### 9. Account statement — `getAccountStatement`
> "Generate a statement for account 2000192797 covering 2025-11-01 to 2026-01-31"

→ opening/closing balance, debit & credit totals + lines

### 10. Running loans — `getCustomerLoans`
> "Fetch me the running loans for customer 6260"

→ OSHO ADEDAYO AKANMU, 11 loan records

### 11. Loan overview — `getLoanOverview`
> "Give me the loan overview for loan account 001310000232"

→ TAJUDEEN OLANREWAJU, ₦8.2M loan, outstanding ~₦7.5M

### 12. Loan schedule — `getLoanSchedule`
> "Show the repayment schedule for loan account 001303000282"

→ 1,800 schedule rows

### 13. Loan repayment history — `getLoanRepaymentHistory`
> "Show the repayment history for loan account 001303001027 from 2026-01-01 to 2026-01-31"

→ 8 repayment entries with narrations

### 14. Loan summary — `getCustomerLoanSummary`
> "Summarize the total loan exposure of customer 20035"

→ OLUGBENGA ONABOWALE, ₦1.47M total loans

### 15. KYC profile — `getCustomerKycProfile`
> "Show me the KYC profile for customer 22605"

→ BVN, NIN, ID masked; tier & status shown

### 16. Branch details — `getBranchDetails`
> "Give me branch details for branch code 001"

→ HEAD OFFICE

### 17. Branch details — `getBranchDetails`
> "What's the address of branch 003?"

→ Ajah branch

### 18. Transaction trace — `getTransactionReference`
> "Trace the transaction with reference 001110010010004"

→ 22,000+ matching transaction rows

### 19. Multi-tool (customer → accounts → statement)
> "Give me an overview of customer 20050's accounts, then a statement for account 2000192797 for the last 3 months"

→ `getCustomerAccounts` + `getAccountStatement` chain

### 20. Multi-tool (loans walkthrough)
> "For customer 6260, list all running loans, then show the repayment schedule for loan 001316000345"

→ `getCustomerLoans` + `getLoanSchedule` chain

## Debit / Credit Transaction Scripts

The tool filter now matches `D`/`C` as stored in the DB (plus `DR`/`CR`/`DEBIT`/`CREDIT`), so debit/credit queries return results.

### D1. Account debits — `getAccountTransactions`
> "Show me only the debits for account 2000192797 from 2025-12-01 to 2026-01-31"

→ 50 debit transactions (e.g. PRINCIPAL REPAYMENT ON LOAN ACCT entries)

### D2. Account credits — `getAccountTransactions`
> "Show me only the credits for account 2000192797 from 2025-12-01 to 2026-01-31"

→ 29 credit transactions (e.g. CSH DEP BY ADEBOYE / SALAU)

### D3. Customer debits — `getCustomerTransactions`
> "List all debit transactions across customer 15886's accounts from 2025-12-01 to 2026-01-31"

→ EMMANUEL HANNAH ADIJAT, 92 debit transactions (account 2000152830)

### D4. Customer credits — `getCustomerTransactions`
> "List all credit transactions across customer 6833's accounts from 2025-12-01 to 2026-01-31"

→ MODUPEOLA CHRISTIANA, 68 credit transactions (account 2000066935)

### D5. Mixed debit/credit — `getAccountTransactions`
> "Show me the debits and credits for account 2000075052 between 2025-12-01 and 2026-01-31"

→ OGUNTUYO FATIMOH ADETUTU, 85 debits + 56 credits (ask for each type separately)

## Caveats (verified against the current DB)

- **Do not demo these 7 tools** — their tables are empty (0 rows) and would return empty results:
  - Liens (`getAccountLiens`)
  - Blocks (`getAccountBlocks`)
  - Signatories (`getAccountSignatories`)
  - Beneficiaries (`getCustomerBeneficiaries`)
  - Mandates (`getAccountMandates`)
  - Cards (`getCustomerCards`)
  - Inward/outward transfers (`getInwardTransfers`, `getOutwardTransfers`)
- **Date ranges are capped at 180 days** (`AiToolGuards.requireDateRange`), so keep ranges ≤ 6 months.
