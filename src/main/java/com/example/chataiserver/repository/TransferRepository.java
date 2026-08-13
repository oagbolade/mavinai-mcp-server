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
public class TransferRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<TransferDto> findInwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, int limit) {
        String sql = "select top (:limit) 'INWARD' as direction, debit_account_number as debitAccountNumber, beneficiary_account_number as beneficiaryAccountNumber, beneficiary_name as beneficiaryName, beneficiary_bank as beneficiaryBank, amount as amount, narration as narration, transaction_status as status, session_id as sessionId, transaction_id as transactionId, coalesce(reference_id, payment_reference) as reference, transaction_date as transactionDate from inward_transaction where beneficiary_account_number = :accountNumber and cast(transaction_date as date) between :startDate and :endDate order by transaction_date desc";
        return queryTransfers(sql, accountNumber, startDate, endDate, limit);
    }

    public List<TransferDto> findOutwardTransfers(String accountNumber, LocalDate startDate, LocalDate endDate, int limit) {
        String sql = "select top (:limit) 'OUTWARD' as direction, debit_account_number as debitAccountNumber, beneficiary_account_number as beneficiaryAccountNumber, beneficiary_name as beneficiaryName, beneficiary_bank as beneficiaryBank, amount as amount, narration as narration, transaction_status as status, session_id as sessionId, transaction_id as transactionId, coalesce(reference_id, payment_reference) as reference, transaction_date as transactionDate from outward_transaction where debit_account_number = :accountNumber and cast(transaction_date as date) between :startDate and :endDate order by transaction_date desc";
        return queryTransfers(sql, accountNumber, startDate, endDate, limit);
    }

    public List<TransferDto> findTransfersByReference(String reference, int limit) {
        MapSqlParameterSource params = new MapSqlParameterSource("reference", reference).addValue("limit", limit);
        String inward = referenceSelect("inward_transaction", "INWARD") + " where session_id = :reference or transaction_id = :reference or reference_id = :reference or payment_reference = :reference";
        String outward = referenceSelect("outward_transaction", "OUTWARD") + " where session_id = :reference or transaction_id = :reference or reference_id = :reference or payment_reference = :reference";
        return jdbcTemplate.query("select top (:limit) * from (" + inward + " union all " + outward + ") t order by transactionDate desc", params, transferMapper());
    }

    public List<BeneficiaryDto> findCustomerBeneficiaries(String customerId) {
        String sql = "select CustomerId as customerId, BeneficiaryName as beneficiaryName, BeneficiaryAccountNumber as beneficiaryAccountNumber, BeneficiaryBankName as beneficiaryBank, BeneficiaryBankCode as beneficiaryBankCode, cast(null as varchar(100)) as beneficiaryType, Status as status from tbl_TransferBeneficiary where CustomerId = :customerId order by BeneficiaryName";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("customerId", customerId), (rs, n) -> new BeneficiaryDto(string(rs,"customerId"), string(rs,"beneficiaryName"), string(rs,"beneficiaryAccountNumber"), string(rs,"beneficiaryBank"), string(rs,"beneficiaryBankCode"), string(rs,"beneficiaryType"), string(rs,"status")));
    }

    public List<MandateDto> findAccountMandates(String accountNumber) {
        String sql = "select debit_account_number as debitAccountNumber, beneficiary_account_number as beneficiaryAccountNumber, beneficiary_account_name as beneficiaryName, amount as amount, cast(null as varchar(100)) as frequency, transaction_status as status, created_at as startDate, cast(null as datetime2) as endDate, mandate_reference_number as reference from mandate where debit_account_number = :accountNumber or beneficiary_account_number = :accountNumber order by created_at desc";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("accountNumber", accountNumber), (rs, n) -> new MandateDto(string(rs,"debitAccountNumber"), string(rs,"beneficiaryAccountNumber"), string(rs,"beneficiaryName"), decimal(rs,"amount"), string(rs,"frequency"), string(rs,"status"), localDateTime(rs,"startDate"), localDateTime(rs,"endDate"), string(rs,"reference")));
    }

    private List<TransferDto> queryTransfers(String sql, String accountNumber, LocalDate startDate, LocalDate endDate, int limit) {
        MapSqlParameterSource params = new MapSqlParameterSource("accountNumber", accountNumber).addValue("startDate", startDate).addValue("endDate", endDate).addValue("limit", limit);
        return jdbcTemplate.query(sql, params, transferMapper());
    }

    private String referenceSelect(String table, String direction) {
        return "select '" + direction + "' as direction, debit_account_number as debitAccountNumber, beneficiary_account_number as beneficiaryAccountNumber, beneficiary_name as beneficiaryName, beneficiary_bank as beneficiaryBank, amount as amount, narration as narration, transaction_status as status, session_id as sessionId, transaction_id as transactionId, coalesce(reference_id, payment_reference) as reference, transaction_date as transactionDate from " + table;
    }

    private RowMapper<TransferDto> transferMapper() {
        return (rs, n) -> new TransferDto(string(rs,"direction"), string(rs,"debitAccountNumber"), string(rs,"beneficiaryAccountNumber"), string(rs,"beneficiaryName"), string(rs,"beneficiaryBank"), decimal(rs,"amount"), string(rs,"narration"), string(rs,"status"), string(rs,"sessionId"), string(rs,"transactionId"), string(rs,"reference"), localDateTime(rs,"transactionDate"));
    }
}
