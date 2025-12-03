package com.bankingSystem.service.impl;

import com.bankingSystem.dto.AmountRequest;
import com.bankingSystem.dto.NotificationRequest;
import com.bankingSystem.dto.TransferRequest;
import com.bankingSystem.exception.InsufficientBalanceException;
import com.bankingSystem.exception.InvalidAmountException;
import com.bankingSystem.feign.AccountClient;
import com.bankingSystem.feign.NotificationClient;
import com.bankingSystem.feign.dto.AccountDTO;
import com.bankingSystem.model.Transaction;
import com.bankingSystem.repository.TransactionRepository;
import com.bankingSystem.service.TransactionService;
import com.bankingSystem.util.IdGenerator;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository txnRepo;
    private final AccountClient accountClient;
    private final NotificationClient notificationClient;

    public TransactionServiceImpl(TransactionRepository txnRepo,
                                  AccountClient accountClient,
                                  NotificationClient notificationClient) {
        this.txnRepo = txnRepo;
        this.accountClient = accountClient;
        this.notificationClient = notificationClient;
    }

    @CircuitBreaker(name = "notificationCB", fallbackMethod = "notifyFallback")
    public void sendNotificationSafe(NotificationRequest request) {
        notificationClient.sendNotification(request);
    }

    public void notifyFallback(NotificationRequest request, Throwable ex) {
        System.out.println("⚠ Notification FAILED → storing message locally.");
        System.out.println("Request = " + request);
        System.out.println("Reason  = " + ex.getMessage());
    }

    // ---------------------------------------------------------
    // DEPOSIT
    // ---------------------------------------------------------
    @Override
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "depositFallback")
    public Transaction deposit(String accNo, AmountRequest req) {

        if (req.getAmount() == null || req.getAmount() <= 0)
            throw new InvalidAmountException("Invalid Amount");

        AccountDTO acc = accountClient.getAccount(accNo);

        double newBalance = acc.getBalance() + req.getAmount();
        accountClient.updateBalance(accNo, Map.of("balance", newBalance));

        Transaction txn = new Transaction(
                null,
                IdGenerator.generateTransactionId(),
                "DEPOSIT",
                req.getAmount(),
                Instant.now(),
                "SUCCESS",
                accNo,
                null
        );

        // always safe – fallback handles failures
        sendNotificationSafe(new NotificationRequest(
                acc.getHolderName() + "@gmail.com",
                "Deposit successful!"
        ));

        return txnRepo.save(txn);
    }

    public Transaction depositFallback(String accNo, AmountRequest req, Throwable ex) {
        return new Transaction(null, IdGenerator.generateTransactionId(),
                "DEPOSIT", req.getAmount(), Instant.now(),
                "FAILED", accNo, null);
    }

    // ---------------------------------------------------------
    // WITHDRAW
    // ---------------------------------------------------------
    @Override
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "withdrawFallback")
    public Transaction withdraw(String accNo, AmountRequest req) {

        if (req.getAmount() == null || req.getAmount() <= 0)
            throw new InvalidAmountException("Invalid Amount");

        AccountDTO acc = accountClient.getAccount(accNo);

        if (acc.getBalance() < req.getAmount())
            throw new InsufficientBalanceException("Insufficient Balance");

        double newBalance = acc.getBalance() - req.getAmount();
        accountClient.updateBalance(accNo, Map.of("balance", newBalance));

        Transaction txn = new Transaction(
                null,
                IdGenerator.generateTransactionId(),
                "WITHDRAW",
                req.getAmount(),
                Instant.now(),
                "SUCCESS",
                accNo,
                null
        );

        sendNotificationSafe(new NotificationRequest(
                acc.getHolderName() + "@gmail.com",
                "Withdrawal successful!"
        ));

        return txnRepo.save(txn);
    }

    public Transaction withdrawFallback(String accNo, AmountRequest req, Throwable ex) {
        return new Transaction(null, IdGenerator.generateTransactionId(),
                "WITHDRAW", req.getAmount(), Instant.now(),
                "FAILED", accNo, null);
    }

    // ---------------------------------------------------------
    // TRANSFER
    // ---------------------------------------------------------
    @Override
    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "transferFallback")
    public Transaction transfer(TransferRequest req) {

        if (req.getSourceAccount().equals(req.getDestinationAccount()))
            throw new InvalidAmountException("Source and Destination cannot be the same");

        AccountDTO src = accountClient.getAccount(req.getSourceAccount());
        AccountDTO dest = accountClient.getAccount(req.getDestinationAccount());

        if (src.getBalance() < req.getAmount())
            throw new InsufficientBalanceException("Insufficient Balance");

        accountClient.updateBalance(req.getSourceAccount(),
                Map.of("balance", src.getBalance() - req.getAmount()));

        accountClient.updateBalance(req.getDestinationAccount(),
                Map.of("balance", dest.getBalance() + req.getAmount()));

        Transaction txn = new Transaction(
                null,
                IdGenerator.generateTransactionId(),
                "TRANSFER",
                req.getAmount(),
                Instant.now(),
                "SUCCESS",
                req.getSourceAccount(),
                req.getDestinationAccount()
        );

        sendNotificationSafe(new NotificationRequest(
                src.getHolderName() + "@gmail.com",
                "You sent ₹" + req.getAmount()
        ));

        sendNotificationSafe(new NotificationRequest(
                dest.getHolderName() + "@gmail.com",
                "You received ₹" + req.getAmount()
        ));

        return txnRepo.save(txn);
    }

    public Transaction transferFallback(TransferRequest req, Throwable ex) {
        return new Transaction(null, IdGenerator.generateTransactionId(),
                "TRANSFER", req.getAmount(), Instant.now(),
                "FAILED", req.getSourceAccount(), req.getDestinationAccount());
    }

    @Override
    public List<Transaction> getTransactions(String accNo) {
        return txnRepo.findBySourceAccountOrDestinationAccount(accNo, accNo);
    }
}
