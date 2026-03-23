package com.atmsimulator.service;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.model.User;
import com.atmsimulator.repository.AccountRepository;
import com.atmsimulator.repository.TransactionRepository;
import com.atmsimulator.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AdminService {
    TransactionRepository transactionRepo = new TransactionRepository();
    AccountRepository accountRepo = new AccountRepository();
    UserRepository userRepo = new UserRepository();

    public List<Transaction> getTransactionsForAccount(String accountId) {
        List<Transaction> transactions = transactionRepo.findAll();
        List<Transaction> transactionsForId = new ArrayList<>();

        for (Transaction t : transactions) {
            if (t.getAccountId().equals(accountId)) {
                transactionsForId.add(t);
            }
        }
        int total = transactionsForId.size();

        int startIndex = Math.max(0, total - 5);

        return transactionsForId.subList(startIndex, total);
    }

    public List<Transaction> getAllSystemTransactions() {
        return transactionRepo.findAll();
    }

    public void closeAccount(String accountId) {
        Account acc = accountRepo.findAll().stream()
                .filter(a -> a.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Contul nu există!"));

        if (acc.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Contul are sold pozitiv (" + acc.getBalance() + " " + acc.getCurrency() +
                    "). Utilizatorul trebuie să ridice banii de la bancă înainte de închidere!");
        }

        Transaction deleteTransaction = new Transaction(
                UUID.randomUUID().toString(),
                accountId,
                "INCHIDERE CONT",
                BigDecimal.ZERO,
                "---",
                LocalDateTime.now()
        );
        transactionRepo.save(deleteTransaction);

        accountRepo.deleteById(accountId);
    }

    public void createFullProfile(String fullName, String pin, BigDecimal initialBalance, String currency) {
        String generatedUserId = "U" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        String generatedAccountId = "RO" + (10000 + new Random().nextInt(89999));

        User newUser = new User(generatedUserId, fullName, "CLIENT");
        userRepo.save(newUser);

        Account newAccount = new Account(
                generatedAccountId,
                generatedUserId,
                pin,
                initialBalance,
                currency
        );

        Transaction createTransaction = new Transaction(
                UUID.randomUUID().toString(),
                generatedAccountId,
                "CREARE CONT",
                initialBalance,
                currency,
                LocalDateTime.now()
        );
        transactionRepo.save(createTransaction);

        accountRepo.save(newAccount);
    }
}
