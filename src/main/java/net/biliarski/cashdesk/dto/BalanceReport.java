package net.biliarski.cashdesk.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BalanceReport {
    public LocalDateTime   timestamp;
    public String cashier;
    public String currency;
    public int amount = 0;
    public Map<Integer, Integer> denominations = new HashMap<>();

    public BalanceReport( ) {

    }

}