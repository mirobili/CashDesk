package net.biliarski.cashdesk.dto;

import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction.TransactionType;
import java.util.Map;
import java.util.Objects;

public class CashOperationRequest {
    private String cashierName;
    private int amount;
    private Currency currency;
    private TransactionType type;
    private Map<Integer, Integer> denominations;

    // Getters and Setters
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
        CashOperationRequest that = (CashOperationRequest) o;
        return amount == that.amount &&
                Objects.equals(cashierName, that.cashierName) &&
                currency == that.currency &&
                type == that.type &&
                Objects.equals(denominations, that.denominations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashierName, amount, currency, type, denominations);
    }

    @Override
    public String toString() {
        return "CashOperationRequest{" +
                "cashierName='" + cashierName + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", type=" + type +
                ", denominations=" + denominations +
                '}';
    }
}
