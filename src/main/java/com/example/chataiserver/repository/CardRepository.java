package com.example.chataiserver.repository;

import com.example.chataiserver.dto.CustomerCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class CardRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<CustomerCardDto> findCustomerCards(String customerId) {
        String sql = "select CustomerID as customerId, AccountNumberA as accountNumber, cast(null as varchar(100)) as maskedPan, CardType as cardType, CardScheme as cardScheme, SentStatusFlag as status, RECR_Time as issueDate, cast(null as date) as expiryDate, 'Tbl_Cardrequests' as sourceTable from Tbl_Cardrequests where CustomerID = :customerId " +
                "union all select CustomerID as customerId, AccountNumberA as accountNumber, MaskedPAN as maskedPan, CardType as cardType, CardScheme as cardScheme, CardStatus as status, IssueDate as issueDate, ExpiryDate as expiryDate, 'tbl_CardFileRecords' as sourceTable from tbl_CardFileRecords where CustomerID = :customerId";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("customerId", customerId), mapper());
    }

    private RowMapper<CustomerCardDto> mapper() {
        return (rs, n) -> new CustomerCardDto(string(rs,"customerId"), string(rs,"accountNumber"), string(rs,"maskedPan"), string(rs,"cardType"), string(rs,"cardScheme"), string(rs,"status"), localDate(rs,"issueDate"), localDate(rs,"expiryDate"), string(rs,"sourceTable"));
    }
}
