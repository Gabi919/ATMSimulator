package com.atmsimulator.service;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.model.User;
import com.atmsimulator.repository.AccountRepository;
import com.atmsimulator.repository.TransactionRepository;
import com.atmsimulator.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AuthService {
    private final AccountRepository accountRepo = new AccountRepository();
    private final UserRepository userRepo = new UserRepository();
    private final TransactionRepository transactionRepo = new TransactionRepository();

    public Account login(String accountId, String pin) {
        List<Account> accounts = accountRepo.findAll();

        for (Account acc : accounts) {
            if (acc.getAccountId().equals(accountId)) {
                if (acc.getPin().equals(pin)) {
                    return acc;
                } else {
                    throw new RuntimeException("PIN incorect!");
                }
            }
        }
        throw new RuntimeException("Contul nu a fost gasit!");
    }

    public User getOwnerDetails(String userId) {
        return userRepo.findById(userId);
    }

    public void changePin(Account account, String oldPin, String newPin) {
        if (!account.getPin().equals(oldPin)) {
            throw new RuntimeException("PIN-ul actual este incorect!");
        }

        if (oldPin.equals(newPin)) {
            throw new RuntimeException("Noul PIN nu poate fi identic cu cel vechi!");
        }

        if (!newPin.matches("\\d{4}")) {
            throw new RuntimeException("Noul PIN trebuie să conțină exact 4 cifre!");
        }

        account.setPin(newPin);

        accountRepo.updateInFile(account);

        Transaction pinChange = new Transaction(UUID.randomUUID().toString(), account.getAccountId(), "SCHIMBARE PIN",
                                                BigDecimal.ZERO, account.getCurrency(), LocalDateTime.now());

        transactionRepo.save(pinChange);
    }
}