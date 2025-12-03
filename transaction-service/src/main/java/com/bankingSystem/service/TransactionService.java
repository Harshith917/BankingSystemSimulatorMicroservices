package com.bankingSystem.service;

import com.bankingSystem.dto.AmountRequest;
import com.bankingSystem.dto.TransferRequest;
import com.bankingSystem.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction deposit(String accNo, AmountRequest req);

    Transaction withdraw(String accNo, AmountRequest req);

    Transaction transfer(TransferRequest req);

    List<Transaction> getTransactions(String accNo);
}
