package com.example.chataiserver.repository;

import com.example.chataiserver.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findByCustomerId(String customerId);

    Optional<Account> findByNuban(String nuban);

}