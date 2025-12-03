package com.bankingSystem.service.impl;

import com.bankingSystem.exception.AccountNotFoundException;
import com.bankingSystem.exception.InvalidAccountNumberException;
import com.bankingSystem.model.Account;
import com.bankingSystem.repository.AccountRepository;
import com.bankingSystem.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountRepository accRepo;
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        accRepo = mock(AccountRepository.class);
        service = new AccountServiceImpl(accRepo);
    }

    // CREATE ACCOUNT
    @Test
    void testCreateAccount() {
        String name = "Ashu";

        Account saved = new Account("ASH1234", name);
        when(accRepo.save(any(Account.class))).thenReturn(saved);

        Account result = service.createAccount(name);

        assertNotNull(result);
        assertEquals("ASH1234", result.getAccountNumber());
        assertEquals(name, result.getHolderName());

        verify(accRepo, times(1)).save(any(Account.class));
    }

    // INVALID ACCOUNT NUMBER FORMAT
    @Test
    void testGetAccount_invalidFormat() {
        assertThrows(InvalidAccountNumberException.class,
                () -> service.getAccount("wrong123"));
    }

    // GET ACCOUNT SUCCESS
    @Test
    void testGetAccount_success() {
        Account acc = new Account("ASH1234", "Ashu");

        when(accRepo.findByAccountNumber("ASH1234"))
                .thenReturn(Optional.of(acc));

        Account result = service.getAccount("ASH1234");

        assertEquals("ASH1234", result.getAccountNumber());
        assertEquals("Ashu", result.getHolderName());
    }

    // GET ACCOUNT NOT FOUND
    @Test
    void testGetAccount_notFound() {
        when(accRepo.findByAccountNumber("ASH1234"))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> service.getAccount("ASH1234"));
    }

    // UPDATE STATUS
    @Test
    void testUpdateStatus() {
        Account acc = new Account("ASH1234", "Ashu");
        acc.setStatus("ACTIVE");

        when(accRepo.findByAccountNumber("ASH1234"))
                .thenReturn(Optional.of(acc));
        when(accRepo.save(acc)).thenReturn(acc);

        Account result = service.updateStatus("ASH1234", "INACTIVE");

        assertEquals("INACTIVE", result.getStatus());
    }
}
