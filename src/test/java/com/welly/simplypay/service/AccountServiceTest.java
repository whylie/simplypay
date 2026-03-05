package com.welly.simplypay.service;

import com.welly.simplypay.model.Account;
import com.welly.simplypay.objects.AccountDTO;
import com.welly.simplypay.repository.AccountRepository;
import com.welly.simplypay.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        // Manual instantiation without Lombok
        this.accountService = new AccountService(accountRepository);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        // Arrange
        AccountDTO request = new AccountDTO(1L,"123456789012", "Savings", "MYR", BigDecimal.valueOf(1000), new ArrayList<>());
        Account account = new Account();
        account.setAccountNumber(request.accountNumber());

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        AccountDTO response = accountService.createAccount(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("123456789012", response.accountNumber());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }
}
