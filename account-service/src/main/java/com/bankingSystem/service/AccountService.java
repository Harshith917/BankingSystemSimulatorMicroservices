package com.bankingSystem.service;

import com.bankingSystem.model.Account;

public interface AccountService {

    // CREATE ACCOUNT
    Account createAccount(String holderName);

    // GET ACCOUNT DETAILS
    Account getAccount(String accNo);

    // UPDATE HOLDER NAME
    Account updateHolderName(String accNo, String newHolderName);

    // DELETE ACCOUNT
    void deleteAccount(String accNo);

    // OPTIONAL: UPDATE ACCOUNT BALANCE (used only if needed by other services)
    Account updateBalance(String accNo, Double newBalance);

    // OPTIONAL: UPDATE ACCOUNT STATUS (ACTIVE / INACTIVE)
    Account updateStatus(String accNo, String status);
}
