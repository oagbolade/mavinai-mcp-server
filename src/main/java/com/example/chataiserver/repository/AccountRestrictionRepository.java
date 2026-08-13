package com.example.chataiserver.repository;

import com.example.chataiserver.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class AccountRestrictionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<AccountLienDto> findLiens(String accountNumber) {
        String sql = "select Accountnumber as accountNumber, TranDate as transactionDate, ExpiryDate as expiryDate, LienAmount as lienAmount, AccountTiedTo as accountTiedTo, ReasonCode as reasonCode, LienReason as lienReason, Status as status from tbl_Lien where Accountnumber = :accountNumber order by TranDate desc";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("accountNumber", accountNumber), (rs, n) -> new AccountLienDto(string(rs,"accountNumber"), localDate(rs,"transactionDate"), localDate(rs,"expiryDate"), decimal(rs,"lienAmount"), string(rs,"accountTiedTo"), string(rs,"reasonCode"), string(rs,"lienReason"), string(rs,"status")));
    }

    public List<AccountBlockDto> findBlocks(String accountNumber) {
        String sql = "select account_block_name as blockName, account_block_number as accountNumber, account_block_status as status, blocked_at as blockedAt, hold_number as holdNumber, nip_ref as nipReference, qt_ref as qtReference, unblocked_at as unblockedAt from account_block where account_block_number = :accountNumber order by blocked_at desc";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("accountNumber", accountNumber), (rs, n) -> new AccountBlockDto(string(rs,"blockName"), string(rs,"accountNumber"), string(rs,"status"), localDateTime(rs,"blockedAt"), string(rs,"holdNumber"), string(rs,"nipReference"), string(rs,"qtReference"), localDateTime(rs,"unblockedAt")));
    }

    public List<AccountSignatoryDto> findSignatories(String accountNumber) {
        String sql = "select Accountnumber as accountNumber, SignatoryName as signatoryName, PhoneNo as phone, email as email, cast(null as varchar(100)) as mandateClass, Status as status from tbl_AcctSignatory where Accountnumber = :accountNumber order by SignatoryName";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("accountNumber", accountNumber), (rs, n) -> new AccountSignatoryDto(string(rs,"accountNumber"), string(rs,"signatoryName"), string(rs,"phone"), string(rs,"email"), string(rs,"mandateClass"), string(rs,"status")));
    }
}
