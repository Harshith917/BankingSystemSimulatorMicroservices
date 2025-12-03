package com.bankingSystem.service.impl;

import com.bankingSystem.exception.*;
import com.bankingSystem.model.Account;
import com.bankingSystem.repository.AccountRepository;
import com.bankingSystem.service.AccountService;
import com.bankingSystem.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accRepo;

    public AccountServiceImpl(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }

    @Override
    public Account createAccount(String name) {

        log.info("Service: Creating account for holder='{}'", name);

        String accNo = IdGenerator.generateAccountNumber(name);
        log.info("Service: Generated account number='{}' for holder='{}'", accNo, name);

        Account acc = new Account(accNo, name);
        Account saved = accRepo.save(acc);

        log.info("Service: Account saved in DB with accountNumber='{}'", saved.getAccountNumber());

        return saved;
    }

    private void validateAccountNumberFormat(String accNo) {
        // Account number must be: 3 letters + 4 digits  e.g. ASH1234
        if (accNo == null || !accNo.matches("^[A-Z]{3}[0-9]{4}$")) {
            throw new InvalidAccountNumberException("Invalid account number format");
        }
        log.info("Service: Account number '{}' passed validation.", accNo);
    }

    @Override
    public Account getAccount(String accNo) {

        log.info("Service: Fetching account='{}'", accNo);

        validateAccountNumberFormat(accNo);

        Account acc = accRepo.findByAccountNumber(accNo)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found"));

        log.info("Service: Account='{}' found.", accNo);

        return acc;
    }

    @Override
    public Account updateHolderName(String accNo, String newHolderName) {
        validateAccountNumberFormat(accNo);

        Account acc = getAccount(accNo);
        acc.setHolderName(newHolderName);

        log.info("Service: Holder name updated for account='{}'", accNo);

        return accRepo.save(acc);
    }

    @Override
    public void deleteAccount(String accNo) {

        validateAccountNumberFormat(accNo);

        Account acc = getAccount(accNo);

        accRepo.delete(acc);
        log.info("Service: Account '{}' deleted successfully", accNo);
    }

    @Override
    public Account updateBalance(String accNo, Double newBalance) {

        validateAccountNumberFormat(accNo);

        Account acc = getAccount(accNo);

        acc.setBalance(newBalance);

        return accRepo.save(acc);
    }


    @Override
    public Account updateStatus(String accNo, String status) {

        validateAccountNumberFormat(accNo);

        Account acc = getAccount(accNo);
        acc.setStatus(status.toUpperCase());

        return accRepo.save(acc);
    }

}
