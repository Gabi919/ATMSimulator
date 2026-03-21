package com.atmsimulator.repository;

import com.atmsimulator.model.Account;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {
    private final String FILE_PATH = "accounts.txt";

    // 1. Citește toate conturile din fișier
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Structura în txt: accountId,userId,pin,balance,currency
                if (data.length == 5) {
                    accounts.add(new Account(data[0], data[1], data[2], new BigDecimal(data[3]), data[4]));
                }
            }
        } catch (IOException e) {
            // Dacă fișierul nu există la prima rulare, returnăm o listă goală
        }
        return accounts;
    }

    // 2. Salvează toate conturile înapoi în fișier (suprascriere)
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
}