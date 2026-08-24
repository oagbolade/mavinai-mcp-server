package com.example.chataiserver.repository;

import com.example.chataiserver.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<TransactionDto> findAccountTransactions(String accountNumber, LocalDate startDate, LocalDate endDate,
                                                        String transactionType, int limit) {
        return findTransactions(List.of(accountNumber), startDate, endDate, transactionType, limit);
    }

    public List<TransactionDto> findTransactions(Collection<String> accountNumbers, LocalDate startDate, LocalDate endDate,
                                                 String transactionType, int limit) {
        MapSqlParameterSource params = baseParams(startDate, endDate, transactionType, limit)
                .addValue("accountNumbers", accountNumbers);
        return jdbcTemplate.query(transactionUnion("t.AccountNumber in (:accountNumbers)"), params, mapper());
    }

    public List<TransactionDto> findByReference(String reference, int limit) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("reference", reference)
                .addValue("limit", limit);
        String sql = "select top (:limit) * from (" + selectFrom("tbl_transactions", "t.refno = :reference", true, true, true) +
                " union all " + selectFrom("tbl_dailyTransactions", "t.refno = :reference", true, true, true) +
                ") tx order by tx.transactionDate desc, tx.postSequence desc, tx.referenceNumber desc";
        return jdbcTemplate.query(sql, params, mapper());
    }

    private MapSqlParameterSource baseParams(LocalDate startDate, LocalDate endDate, String transactionType, int limit) {
        return new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("transactionType", transactionType)
                .addValue("limit", limit);
    }

    private String transactionUnion(String accountPredicate) {
        return "select top (:limit) * from (" + selectFrom("tbl_transactions", accountPredicate, true, true, true) +
                " union all " + selectFrom("tbl_Transactionshist", accountPredicate, false, false, false) +
                " union all " + selectFrom("tbl_dailyTransactions", accountPredicate, true, true, true) +
                ") tx where (:transactionType = 'ALL' or " +
                "(:transactionType = 'DEBIT' and upper(isnull(tx.transactionType, '')) in ('DEBIT','DR','D')) or " +
                "(:transactionType = 'CREDIT' and upper(isnull(tx.transactionType, '')) in ('CREDIT','CR','C'))) " +
                "order by tx.transactionDate desc, tx.postSequence desc, tx.referenceNumber desc";
    }

    private String selectFrom(String tableName, String accountPredicate, boolean hasReferenceNumber, boolean hasChannel, boolean hasTranType) {
        return "select t.AccountNumber as accountNumber, a.accounttitle as accountTitle, t.TranDate as transactionDate, " +
                "t.Valuedate as valueDate, t.Trancode as transactionCode, tt.TranName as transactionName, " +
                (hasTranType ? "t.TranType" : "tt.TranType") + " as transactionType, t.Narration as narration, t.TranAmount as transactionAmount, " +
                "t.BaseAmount as baseAmount, t.charge as charge, t.Reversal as reversal, t.status as status, " +
                (hasReferenceNumber ? "t.refno" : "cast(null as varchar(100))") + " as referenceNumber, " +
                (hasChannel ? "t.Channel" : "cast(null as varchar(100))") + " as channel, t.postseq as postSequence, '" + tableName + "' as sourceTable " +
                "from " + tableName + " t " +
                "left join tbl_casaaccount a on a.accountnumber = t.AccountNumber " +
                "left join tbl_TransactType tt on tt.TranCode = t.Trancode " +
                "where " + accountPredicate + " and cast(t.TranDate as date) between :startDate and :endDate";
    }

    private RowMapper<TransactionDto> mapper() {
        return (rs, rowNum) -> new TransactionDto(
                string(rs, "accountNumber"),
                string(rs, "accountTitle"),
                localDate(rs, "transactionDate"),
                localDate(rs, "valueDate"),
                string(rs, "transactionCode"),
                string(rs, "transactionName"),
                string(rs, "transactionType"),
                string(rs, "narration"),
                decimal(rs, "transactionAmount"),
                decimal(rs, "baseAmount"),
                decimal(rs, "charge"),
                string(rs, "reversal"),
                string(rs, "status"),
                string(rs, "referenceNumber"),
                string(rs, "channel"),
                string(rs, "sourceTable")
        );
    }
}
