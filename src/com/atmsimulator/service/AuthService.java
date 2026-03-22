package com.atmsimulator.service;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.User;
import com.atmsimulator.repository.AccountRepository;
import com.atmsimulator.repository.UserRepository;

import java.util.List;

public class AuthService {
    private final AccountRepository accountRepo = new AccountRepository();
    private final UserRepository userRepo = new UserRepository();

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
}