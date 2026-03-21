package com.atmsimulator.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String accountId;
    private String type;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime timestamp;

    public Transaction(String transactionId, String accountId, String type, BigDecimal amount, String currency, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
}