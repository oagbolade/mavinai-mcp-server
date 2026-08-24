package com.example.chataiserver.service.impl;

import com.example.chataiserver.dto.AccountCustomerNameDto;
import com.example.chataiserver.model.Account;
import com.example.chataiserver.model.Customer;
import com.example.chataiserver.repository.AccountRepository;
import com.example.chataiserver.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountServiceImplTest {

    @Test
    void resolvesCustomerNameUsingNubanWhenItIsNotAnInternalAccountNumber() {
        Account account = Account.builder()
                .accountNumber("0012345678")
                .customerId("CUST-42")
                .nuban("1234567890")
                .build();
        Customer customer = Customer.builder().customerId("CUST-42").fullName("Ada Okafor").build();
        AccountRepository accountRepository = proxy(AccountRepository.class, (methodName, argument) -> {
            if (methodName.equals("findById")) {
                return Optional.empty();
            }
            if (methodName.equals("findByNuban")) {
                assertEquals("1234567890", argument);
                return Optional.of(account);
            }
            throw new AssertionError("Unexpected account repository call: " + methodName);
        });
        CustomerRepository customerRepository = proxy(CustomerRepository.class, (methodName, argument) -> {
            if (methodName.equals("findByCustomerId")) {
                assertEquals("CUST-42", argument);
                return Optional.of(customer);
            }
            throw new AssertionError("Unexpected customer repository call: " + methodName);
        });
        AccountServiceImpl service = new AccountServiceImpl(accountRepository, customerRepository);

        AccountCustomerNameDto result = service.getCustomerNameByAccountNumber(" 1234567890 ");
        var customerIdentity = service.getCustomerIdByAccountNumber("1234567890");

        assertEquals("0012345678", result.getAccountNumber());
        assertEquals("Ada Okafor", result.getCustomerName());
        assertEquals("CUST-42", customerIdentity.getCustomerId());
        assertEquals("Ada Okafor", customerIdentity.getFullName());
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> repositoryType, RepositoryCall handler) {
        return (T) Proxy.newProxyInstance(
                repositoryType.getClassLoader(),
                new Class<?>[]{repositoryType},
                (proxy, method, args) -> handler.call(method.getName(), args == null ? null : args[0])
        );
    }

    @FunctionalInterface
    private interface RepositoryCall {
        Object call(String methodName, Object argument);
    }
}
