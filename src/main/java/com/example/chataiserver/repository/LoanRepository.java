package com.example.chataiserver.repository;

import com.example.chataiserver.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class LoanRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<LoanOverviewDto> findCustomerLoans(String customerId) {
        return jdbcTemplate.query(loanSql("CustomerID = :customerId"), new MapSqlParameterSource("customerId", customerId), loanMapper());
    }

    public LoanOverviewDto findLoanOverview(String accountNumber) {
        List<LoanOverviewDto> loans = jdbcTemplate.query(loanSql("AccountNumber = :accountNumber"), new MapSqlParameterSource("accountNumber", accountNumber), loanMapper());
        return loans.stream().findFirst().orElseThrow(() -> new RuntimeException("Loan account not found"));
    }

    public List<LoanScheduleDto> findLoanSchedule(String accountNumber) {
        String sql = "select AccountNumber as accountNumber, TotalRepayAmt as totalRepaymentAmount, PrincipalAmt as principalAmount, " +
                "InterestAmt as interestAmount, OutstandingBal as outstandingBalance, InstalDue as installmentDue, InstalPay as installmentPaid, " +
                "Date_due as dueDate, PaymentStatus as paymentStatus, principal_due as principalDue, unpaidprinc as unpaidPrincipal, " +
                "paidprinc as paidPrincipal, unpaidinterestamt as unpaidInterestAmount, paidinterestamt as paidInterestAmount, repay_date as repaymentDate " +
                "from tbl_loanscheduledetail where AccountNumber = :accountNumber order by Date_due";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("accountNumber", accountNumber), scheduleMapper());
    }

    public List<LoanTransactionDto> findLoanHistory(String accountNumber, LocalDate startDate, LocalDate endDate) {
        String sql = "select accountnumber as accountNumber, Trandate as transactionDate, tranAmount as transactionAmount, Trantype as transactionType, " +
                "Narration as narration, FromModule as fromModule, postseq as postSequence, actioncode as actionCode, trancode as transactionCode " +
                "from tbl_LoanHistory where accountnumber = :accountNumber and cast(Trandate as date) between :startDate and :endDate " +
                "order by Trandate desc, postseq desc";
        MapSqlParameterSource params = new MapSqlParameterSource("accountNumber", accountNumber).addValue("startDate", startDate).addValue("endDate", endDate);
        return jdbcTemplate.query(sql, params, historyMapper());
    }

    public CustomerLoanSummaryDto summarizeCustomerLoans(String customerId) {
        String sql = "select :customerId as customerId, coalesce(sum(LoanAmount),0) as totalLoanAmount, coalesce(sum(currentBalance),0) as totalBalance, " +
                "coalesce(sum(principaldue),0) as principalOutstanding, coalesce(sum(interestdue),0) as interestOutstanding, " +
                "sum(case when upper(isnull(Status,'')) in ('ACTIVE','A','OPEN') then 1 else 0 end) as activeLoanCount, " +
                "sum(case when MatDate < cast(getdate() as date) then 1 else 0 end) as maturedLoanCount, " +
                "sum(case when nextpaymentdate < cast(getdate() as date) and coalesce(principaldue,0) + coalesce(interestdue,0) > 0 then 1 else 0 end) as missedPaymentCount " +
                "from tbl_loanaccount where CustomerID = :customerId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("customerId", customerId), (rs, rowNum) -> new CustomerLoanSummaryDto(
                string(rs, "customerId"), decimal(rs, "totalLoanAmount"), decimal(rs, "totalBalance"), decimal(rs, "principalOutstanding"),
                decimal(rs, "interestOutstanding"), longValue(rs, "activeLoanCount"), longValue(rs, "maturedLoanCount"), longValue(rs, "missedPaymentCount")));
    }

    private String loanSql(String predicate) {
        return "select CustomerID as customerId, AccountNumber as accountNumber, FullName as fullName, ProductCode as productCode, Currency as currency, " +
                "StartDate as startDate, MatDate as maturityDate, LoanTerm as loanTerm, IntRate as interestRate, LoanAmount as loanAmount, " +
                "LoanPurpose as loanPurpose, SettlementAcct1 as settlementAccount1, SettlementAcct2 as settlementAccount2, Status as status, " +
                "currentBalance as currentBalance, TotalPrincipal as totalPrincipal, TotalInterest as totalInterest, principalpaid as principalPaid, " +
                "interestpaid as interestPaid, principaldue as principalDue, interestdue as interestDue, nextpaymentdate as nextPaymentDate, lastpaymentdate as lastPaymentDate " +
                "from tbl_loanaccount where " + predicate + " order by StartDate desc";
    }

    private RowMapper<LoanOverviewDto> loanMapper() {
        return (rs, rowNum) -> new LoanOverviewDto(string(rs, "customerId"), string(rs, "accountNumber"), string(rs, "fullName"), string(rs, "productCode"),
                string(rs, "currency"), localDate(rs, "startDate"), localDate(rs, "maturityDate"), integer(rs, "loanTerm"), decimal(rs, "interestRate"),
                decimal(rs, "loanAmount"), string(rs, "loanPurpose"), string(rs, "settlementAccount1"), string(rs, "settlementAccount2"), string(rs, "status"),
                decimal(rs, "currentBalance"), decimal(rs, "totalPrincipal"), decimal(rs, "totalInterest"), decimal(rs, "principalPaid"), decimal(rs, "interestPaid"),
                decimal(rs, "principalDue"), decimal(rs, "interestDue"), localDate(rs, "nextPaymentDate"), localDate(rs, "lastPaymentDate"));
    }

    private RowMapper<LoanScheduleDto> scheduleMapper() {
        return (rs, rowNum) -> new LoanScheduleDto(string(rs, "accountNumber"), decimal(rs, "totalRepaymentAmount"), decimal(rs, "principalAmount"),
                decimal(rs, "interestAmount"), decimal(rs, "outstandingBalance"), decimal(rs, "installmentDue"), decimal(rs, "installmentPaid"),
                localDate(rs, "dueDate"), string(rs, "paymentStatus"), decimal(rs, "principalDue"), decimal(rs, "unpaidPrincipal"),
                decimal(rs, "paidPrincipal"), decimal(rs, "unpaidInterestAmount"), decimal(rs, "paidInterestAmount"), localDate(rs, "repaymentDate"));
    }

    private RowMapper<LoanTransactionDto> historyMapper() {
        return (rs, rowNum) -> new LoanTransactionDto(string(rs, "accountNumber"), localDate(rs, "transactionDate"), decimal(rs, "transactionAmount"),
                string(rs, "transactionType"), string(rs, "narration"), string(rs, "fromModule"), string(rs, "postSequence"), string(rs, "actionCode"), string(rs, "transactionCode"));
    }
}
