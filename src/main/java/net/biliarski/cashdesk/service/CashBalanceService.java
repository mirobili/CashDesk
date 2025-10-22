package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.dto.BalanceReport;
import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.service.impl.TransactionServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CashBalanceService {

    List<BalanceReport> getBalanceReport(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException;

}
