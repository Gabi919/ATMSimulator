package com.atmsimulator.service;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.repository.AccountRepository;
import com.atmsimulator.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccountService {
    private final AccountRepository accountRepo = new AccountRepository();
    private final TransactionRepository transactionRepo = new TransactionRepository();

    public void withdraw(Account account, BigDecimal amount) {
        // 1. Regula de business
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Fonduri insuficiente!");
        }

        // 2. Actualizăm modelul
        account.setBalance(account.getBalance().subtract(amount));

        // 3. Salvăm noul sold (trebuie să actualizăm toată lista în fișier)
        updateAccountInFile(account);

        // 4. Creăm și salvăm istoricul
        Transaction t = new Transaction(UUID.randomUUID().toString(), account.getAccountId(),
                "RETRAGERE", amount, account.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t);
    }

    public void deposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        updateAccountInFile(account);

        Transaction t = new Transaction(UUID.randomUUID().toString(), account.getAccountId(),
                "DEPUNERE", amount, account.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t);
    }

    // Metodă internă ajutătoare pentru a rescrie fișierul cu contul actualizat
    private void updateAccountInFile(Account updatedAccount) {
        List<Account> allAccounts = accountRepo.findAll();
        for (int i = 0; i < allAccounts.size(); i++) {
            if (allAccounts.get(i).getAccountId().equals(updatedAccount.getAccountId())) {
                allAccounts.set(i, updatedAccount); // Înlocuim contul vechi cu cel nou
                break;
            }
        }
        accountRepo.saveAll(allAccounts); // Rescriem fișierul
    }
}