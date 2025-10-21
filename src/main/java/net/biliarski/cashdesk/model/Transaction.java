package net.biliarski.cashdesk.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class Transaction {
    private String id;
    private LocalDateTime timestamp;
    private String cashierName;
    private int amount;
    private Currency currency;
    private TransactionType type;
    private Map<Integer, Integer> denominations;
//
//    public enum TransactionType {
//        DEPOSIT,
//        WITHDRAW
//    }


    public enum TransactionType {
        DESK_OPEN("Open desk session", 0),
        LOAD("Load cash to desk", 1),
        DEPOSIT("Deposit cash to desk", 1),
        WITHDRAW("Withdraw cash from desk", -1),
        RETURN_TO_VAULT("Return cash to vault", -1),
        DESK_CLOSE("Close desk session", 0);

        private final String description;
        private final int operation;

        TransactionType(String description, int operation) {
            this.description = description;
            this.operation = operation;
        }

        public String getDescription() {
            return description;
        }

        public int getOperation() {
            return operation;
        }
    }

    // Constructors
    public Transaction() {
    }

    public Transaction(String cashierName, int amount, Currency currency, 
                      TransactionType type, Map<Integer, Integer> denominations) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.cashierName = cashierName;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.denominations = Map.copyOf(denominations);
    }

    // constructor for fetched data
    public Transaction(String id, LocalDateTime timestamp, String cashierName,
                       int amount, Currency currency, TransactionType type,
                       Map<Integer, Integer> denominations) {
        this.id = id;
        this.timestamp = timestamp;
        this.cashierName = cashierName;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.denominations = Map.copyOf(denominations);
    }
//    public Transaction(String cashierName, int amount, Currency currency,
//                       TransactionType type, Map<Integer, Integer> denominations) {
//        this.id = UUID.randomUUID().toString();
//        this.timestamp = LocalDateTime.now();
//        this.cashierName = cashierName;
//        this.amount = amount;
//        this.currency = currency;
//        this.type = type;
//        this.denominations = Map.copyOf(denominations);
//    }

//    public enum TransactionType {
//        DEPOSIT,
//        WITHDRAWAL
//    }
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Map<Integer, Integer> getDenominations() {
        return denominations;
    }

    public void setDenominations(Map<Integer, Integer> denominations) {
        this.denominations = denominations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return amount == that.amount &&
                Objects.equals(id, that.id) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(cashierName, that.cashierName) &&
                currency == that.currency &&
                type == that.type &&
                Objects.equals(denominations, that.denominations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, cashierName, amount, currency, type, denominations);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", cashierName='" + cashierName + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", type=" + type +
                ", denominations=" + denominations +
                '}';
    }


}
