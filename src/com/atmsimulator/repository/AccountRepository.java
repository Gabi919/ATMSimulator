package com.atmsimulator.repository;

import com.atmsimulator.model.Account;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private final String FILE_PATH = "accounts.txt";

    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    accounts.add(new Account(data[0], data[1], data[2], new BigDecimal(data[3]), data[4]));
                }
            }
        } catch (IOException e){}
        return accounts;
    }

    public void saveAll(List<Account> accounts) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Account acc : accounts) {
                pw.println(acc.getAccountId() + "," + acc.getUserId() + "," +
                        acc.getPin() + "," + acc.getBalance() + "," + acc.getCurrency());
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea conturilor: " + e.getMessage());
        }
    }

    public void updateInFile(Account updatedAccount) {
        List<Account> allAccounts = findAll();
        for (int i = 0; i < allAccounts.size(); i++) {
            if (allAccounts.get(i).getAccountId().equals(updatedAccount.getAccountId())) {
                allAccounts.set(i, updatedAccount);
                break;
            }
        }
        saveAll(allAccounts);
    }

    public void deleteById(String accountId) {
        List<Account> accounts = findAll();

        boolean removed = accounts.removeIf(acc -> acc.getAccountId().equals(accountId));

        if (removed) {
            saveAll(accounts);
        } else {
            throw new RuntimeException("Contul nu a fost găsit în baza de date!");
        }
    }

    public void save(Account newAccount) {

        List<Account> allAccounts = findAll();

        allAccounts.add(newAccount);

        saveAll(allAccounts);
    }

}