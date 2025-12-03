package com.bankingSystem.controller;

import com.bankingSystem.dto.CreateAccountRequest;
import com.bankingSystem.model.Account;
import com.bankingSystem.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService service) {
        this.service = service;
    }

    // CREATE ACCOUNT
    @PostMapping
    public ResponseEntity<Account> create(@Valid @RequestBody CreateAccountRequest req) {
        Account created = service.createAccount(req.getHolderName());
        log.info("Created account: accountNumber='{}', holder='{}'",
                created.getAccountNumber(), req.getHolderName());
        return ResponseEntity.status(201).body(created);
    }

    // GET ACCOUNT DETAILS
    @GetMapping("/{accNo}")
    public ResponseEntity<Account> get(@PathVariable String accNo) {
        Account account = service.getAccount(accNo);
        log.info("Fetched account: accountNumber='{}'", accNo);
        return ResponseEntity.ok(account);
    }

    // UPDATE ACCOUNT HOLDER NAME
    @PutMapping("/{accNo}")
    public ResponseEntity<Account> updateHolderName(
            @PathVariable String accNo,
            @Valid @RequestBody CreateAccountRequest req) {

        Account updated = service.updateHolderName(accNo, req.getHolderName());
        log.info("Updated holder name: accountNumber='{}', newHolder='{}'",
                accNo, req.getHolderName());
        return ResponseEntity.ok(updated);
    }

    // DELETE ACCOUNT
    @DeleteMapping("/{accNo}")
    public ResponseEntity<Void> delete(@PathVariable String accNo) {
        service.deleteAccount(accNo);
        log.info("Deleted account: accountNumber='{}'", accNo);
        return ResponseEntity.noContent().build();
    }

    // OPTIONAL: UPDATE ACCOUNT BALANCE (if needed)
    @PutMapping("/{accNo}/balance")
    public ResponseEntity<Account> updateBalance(
            @PathVariable String accNo,
            @RequestBody Map<String, Double> body) {

        Double balance = body.get("balance");
        Account updated = service.updateBalance(accNo, balance);
        return ResponseEntity.ok(updated);
    }


    // OPTIONAL: UPDATE ACCOUNT STATUS (ACTIVE/INACTIVE)
    @PutMapping("/{accNo}/status")
    public ResponseEntity<Account> updateStatus(
            @PathVariable String accNo,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");

        Account updated = service.updateStatus(accNo, status);
        log.info("Updated status for '{}': {}", accNo, status);

        return ResponseEntity.ok(updated);
    }


}
