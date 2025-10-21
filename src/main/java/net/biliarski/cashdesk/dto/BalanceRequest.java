package net.biliarski.cashdesk.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class BalanceRequest {
    private String cashierName;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    // Getters and Setters
    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceRequest that = (BalanceRequest) o;
        return Objects.equals(cashierName, that.cashierName) &&
                Objects.equals(dateFrom, that.dateFrom) &&
                Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashierName, dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "BalanceRequest{" +
                "cashierName='" + cashierName + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
