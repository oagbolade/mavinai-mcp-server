package com.example.chataiserver.repository;

import com.example.chataiserver.dto.CustomerKycProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class KycRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CustomerKycProfileDto findCustomerKycProfile(String customerId) {
        String sql = "select top 1 c.CustomerId as customerId, c.Fullname as fullName, c.CustomerType as customerType, c.Status as status, c.Tier as tier, c.branchcode as branchCode, c.Phone1 as phone, c.Email as email, c.bvn as bvn, c.NIN as nin, id.IDCardName as idCardType, c.IDno as idCardNumber, k.verificationStatus as kycValidationStatus, k.RecordTimestamp as lastValidationDate from tbl_customer c left join tbl_IDCard id on cast(id.IDCardID as varchar(100)) = c.IDType left join tbl_KYCValidationLog k on k.IdentityNum in (c.bvn, c.NIN, c.IDno) where c.CustomerId = :customerId order by k.RecordTimestamp desc";
        List<CustomerKycProfileDto> results = jdbcTemplate.query(sql, new MapSqlParameterSource("customerId", customerId), (rs, n) -> new CustomerKycProfileDto(string(rs,"customerId"), string(rs,"fullName"), string(rs,"customerType"), string(rs,"status"), integer(rs,"tier"), string(rs,"branchCode"), string(rs,"phone"), string(rs,"email"), string(rs,"bvn"), string(rs,"nin"), string(rs,"idCardType"), string(rs,"idCardNumber"), string(rs,"kycValidationStatus"), localDate(rs,"lastValidationDate")));
        return results.stream().findFirst().orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
