package net.biliarski.cashdesk.dto;

import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction.TransactionType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class CashOperationResponse {
    private String transactionId;
    private String cashierName;
    private int amount;
    private Currency currency;
    private TransactionType type;
    private LocalDateTime timestamp;
    private Map<Integer, Integer> denominations;
    private boolean success;
    private String message;
 //   private Object data;

    public static CashOperationResponse error(String message) {
        CashOperationResponse response = new CashOperationResponse();
        response.success = false;
        response.message = message;
     //   response.data = null;
        return response;
    }

    // Getters and Setters

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
   // public Object getData() { return data; }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
        CashOperationResponse that = (CashOperationResponse) o;
        return amount == that.amount &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(cashierName, that.cashierName) &&
                currency == that.currency &&
                type == that.type &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(denominations, that.denominations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, cashierName, amount, currency, type, timestamp, denominations);
    }

    @Override
    public String toString() {
        return "CashOperationResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", cashierName='" + cashierName + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", denominations=" + denominations +
                '}';
    }

    public void setSuccess(boolean b) {
        this.success = b;
    }
}
