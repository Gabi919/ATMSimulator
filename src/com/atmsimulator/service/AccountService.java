package com.atmsimulator.service;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.Transaction;
import com.atmsimulator.repository.AccountRepository;
import com.atmsimulator.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountService {

    private static final Map<String, BigDecimal> EXCHANGE_RATES = new HashMap<>();

    static {

        EXCHANGE_RATES.put("RON", new BigDecimal("1.0"));
        EXCHANGE_RATES.put("EUR", new BigDecimal("4.97"));
        EXCHANGE_RATES.put("USD", new BigDecimal("4.60"));
        EXCHANGE_RATES.put("GBP", new BigDecimal("5.80"));
    }

    private BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) return amount;

        BigDecimal rateFrom = EXCHANGE_RATES.get(fromCurrency);
        BigDecimal rateTo = EXCHANGE_RATES.get(toCurrency);

        if (rateFrom == null || rateTo == null) {
            throw new RuntimeException("Moneda nesuportata: " + fromCurrency + " sau " + toCurrency);
        }

        BigDecimal amountInBase = amount.multiply(rateFrom);

        return amountInBase.divide(rateTo, 2, RoundingMode.HALF_UP);
    }

    private final AccountRepository accountRepo = new AccountRepository();
    private final TransactionRepository transactionRepo = new TransactionRepository();

    public void withdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Fonduri insuficiente!");
        }

        account.setBalance(account.getBalance().subtract(amount));

        accountRepo.updateInFile(account);

        Transaction t = new Transaction(UUID.randomUUID().toString(), account.getAccountId(),
                "RETRAGERE", amount, account.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t);
    }

    public void deposit(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepo.updateInFile(account);

        Transaction t = new Transaction(UUID.randomUUID().toString(), account.getAccountId(),
                "DEPUNERE", amount, account.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t);
    }


    public void transfer(Account fromAccount, String toAccountId, BigDecimal amount) {

        Account toAccount = null;
        List<Account> allAccounts = accountRepo.findAll();
        BigDecimal convertedAmount;
        for (Account acc : allAccounts) {
            if (acc.getAccountId().equals(toAccountId)) {
                toAccount = acc;
                break;
            }
        }
        if (toAccount == null) {
            throw new RuntimeException("Contul destinatie nu a fost gasit!");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Contul nu are suficienti bani pentru a trimite: " + amount + " lei");
        } else {
            convertedAmount = convertCurrency(amount, fromAccount.getCurrency(), toAccount.getCurrency());

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));

            toAccount.setBalance(toAccount.getBalance().add(convertedAmount));
        }



        accountRepo.updateInFile(fromAccount);
        accountRepo.updateInFile(toAccount);

        Transaction t1 = new Transaction(UUID.randomUUID().toString(), fromAccount.getAccountId(),
                "TRANSFER", amount, fromAccount.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t1);
        Transaction t2 = new Transaction(UUID.randomUUID().toString(), toAccount.getAccountId(),
                "PRIMIRE TRANSFER", convertedAmount, toAccount.getCurrency(), LocalDateTime.now());
        transactionRepo.save(t2);
    }


}