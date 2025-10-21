package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.dto.BalanceResponse;

public interface CashBalanceService {
    BalanceResponse getCashierBalance(String cashierName);
}
