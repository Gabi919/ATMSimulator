package com.atmsimulator.service;

import com.atmsimulator.model.Transaction;
import com.atmsimulator.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    TransactionRepository transactionRepo = new TransactionRepository();

    public List<Transaction> getTransactionsForAccount(String accountId) {
        List<Transaction> transactions = transactionRepo.findAll();
        List<Transaction> transactionsForId = new ArrayList<>();

        for(Transaction t : transactions) {
            if(t.getAccountId().equals(accountId)) {
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
}
