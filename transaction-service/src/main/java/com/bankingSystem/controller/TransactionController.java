package com.bankingSystem.controller;

import com.bankingSystem.dto.AmountRequest;
import com.bankingSystem.dto.TransferRequest;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.service.TransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);


    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // DEPOSIT
    @PostMapping("/{accNo}/deposit")
    public Transaction deposit(
            @PathVariable String accNo,
            @RequestBody AmountRequest req) {
        log.info("[TXN] Incoming deposit request for {}", accNo);

        return service.deposit(accNo, req);
    }

    // WITHDRAW
    @PostMapping("/{accNo}/withdraw")
    public Transaction withdraw(
            @PathVariable String accNo,
            @RequestBody AmountRequest req) {
        return service.withdraw(accNo, req);
    }

    // TRANSFER
    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestBody TransferRequest req) {
        return service.transfer(req);
    }

    // GET TRANSACTIONS
    @GetMapping("/{accNo}")
    public List<Transaction> getTransactions(@PathVariable String accNo) {
        return service.getTransactions(accNo);
    }
}
