package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.service.impl.TransactionServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction) throws IOException;
    List<Transaction> getAllTransactions() throws IOException;

    Map<String, Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>>> getCashBalanceReport(
            LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException;
}