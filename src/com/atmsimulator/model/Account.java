package com.atmsimulator.model;

import java.math.BigDecimal;

public class Account {
    private String accountId;
    private String userId;
    private String pin;
    private BigDecimal balance;
    private String currency;

    public Account(String accountId, String userId, String pin, BigDecimal balance, String currency) {
        this.accountId = accountId;
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.currency = currency;
    }

    // Getteri
    public String getAccountId() { return accountId; }
    public String getUserId() { return userId; }
    public String getPin() { return pin; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }

    // Setteri
    public void setPin(String pin) { this.pin = pin; }

    public void setBalance(BigDecimal balance) {
        if (balance == null) throw new IllegalArgumentException("Soldul nu poate fi null");
        this.balance = balance;
    }
}