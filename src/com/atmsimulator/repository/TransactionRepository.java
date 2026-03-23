package com.atmsimulator.repository;

import com.atmsimulator.model.Transaction;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private final String FILE_PATH = "transactions.txt";

    public void save(Transaction t) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(t.getTransactionId() + "," + t.getAccountId() + "," +
                    t.getType() + "," + t.getAmount() + "," +
                    t.getCurrency() + "," + t.getTimestamp().toString());
        } catch (IOException e) {
            System.err.println("Eroare la salvarea tranzacției: " + e.getMessage());
        }
    }

    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    transactions.add(new Transaction(data[0], data[1], data[2],
                            new BigDecimal(data[3]), data[4], LocalDateTime.parse(data[5])));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la gasirea tranzacției: " + e.getMessage());
        }
        return transactions;
    }
}