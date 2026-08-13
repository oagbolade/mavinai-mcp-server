package com.example.chataiserver.repository;

import com.example.chataiserver.dto.BranchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.chataiserver.util.ResultSetReaders.*;

@Repository
@RequiredArgsConstructor
public class ReferenceRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BranchDto findBranchDetails(String branchCode) {
        String sql = "select BranchCode as branchCode, BranchName as branchName, Address as address, City as city, State as state, Status as status from tbl_branch where BranchCode = :branchCode";
        List<BranchDto> branches = jdbcTemplate.query(sql, new MapSqlParameterSource("branchCode", branchCode), (rs, n) -> new BranchDto(string(rs,"branchCode"), string(rs,"branchName"), string(rs,"address"), string(rs,"city"), string(rs,"state"), string(rs,"status")));
        return branches.stream().findFirst().orElseThrow(() -> new RuntimeException("Branch not found"));
    }
}
