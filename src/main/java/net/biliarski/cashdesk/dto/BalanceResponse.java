package net.biliarski.cashdesk.dto;

import net.biliarski.cashdesk.model.Currency;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class BalanceResponse {
    private String cashierName;
    private Map<Currency, Integer> balances;
    private Map<Currency, Map<Integer, Integer>> denominations;
    private LocalDateTime timestamp;

    // Getters
    public String getCashierName() {
        return cashierName;
    }

    public Map<Currency, Integer> getBalances() {
        return balances;
    }

    public Map<Currency, Map<Integer, Integer>> getDenominations() {
        return denominations;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public void setBalances(Map<Currency, Integer> balances) {
        this.balances = balances;
    }

    public void setDenominations(Map<Currency, Map<Integer, Integer>> denominations) {
        this.denominations = denominations;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceResponse that = (BalanceResponse) o;
        return Objects.equals(cashierName, that.cashierName) &&
                Objects.equals(balances, that.balances) &&
                Objects.equals(denominations, that.denominations) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashierName, balances, denominations, timestamp);
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "cashierName='" + cashierName + '\'' +
                ", balances=" + balances +
                ", denominations=" + denominations +
                ", timestamp=" + timestamp +
                '}';
    }
}
