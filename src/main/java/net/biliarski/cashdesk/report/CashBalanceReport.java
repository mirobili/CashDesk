package net.biliarski.cashdesk.report;

import net.biliarski.cashdesk.controller.CashOperationController;
import net.biliarski.cashdesk.dto.BalanceReport;

import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.service.CashBalanceService;
import net.biliarski.cashdesk.service.TransactionService;
import net.biliarski.cashdesk.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashBalanceReport  {
    private final TransactionService transactionService;
    // private ProcessHandleImpl logger;
    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);
    @Autowired
    public CashBalanceReport(TransactionService transactionService) {
         this.transactionService = transactionService;
    }


   //  @Override
    public List<BalanceReport> getData(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException {

        List<Transaction> allTransactions = transactionService.getAll().stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());

        Map<String, BalanceReport> runningBalances = new HashMap<>();
        List<BalanceReport> resultReports = new ArrayList<>();

        for (Transaction t : allTransactions) {
            // Normalize cashier name (so "Martina" and "MARTINA" are treated as same person)
            String normalizedCashier = t.getCashierName().toUpperCase();
            String key = normalizedCashier + "|" + t.getCurrency();

            // Get or initialize running balance for cashier+currency
            BalanceReport currentBalance = runningBalances.computeIfAbsent(key, k -> {
                BalanceReport br = new BalanceReport();
                br.cashier = normalizedCashier;
                br.currency = t.getCurrency().toString();
                br.amount = 0;
                br.denominations = new HashMap<>();
                return br;
            });

            // Determine transaction sign
            int sign = (t.getType() == Transaction.TransactionType.LOAD ||
                    t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;

            // Update cumulative amount
            currentBalance.amount += sign * t.getAmount();

            // Update cumulative denominations
            for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
                currentBalance.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
            }

            // Create a snapshot with current cumulative totals
            BalanceReport snapshot = new BalanceReport();
            snapshot.timestamp = t.getTimestamp();
            snapshot.cashier = currentBalance.cashier;
            snapshot.currency = currentBalance.currency;
            snapshot.amount = currentBalance.amount;
            snapshot.denominations = new HashMap<>(currentBalance.denominations);

            // Filter by cashier and date range (if provided)
            if (cashier != null && !snapshot.cashier.equalsIgnoreCase(cashier)) continue;
            LocalDate txDate = snapshot.timestamp.toLocalDate();
            if (dateFrom != null && txDate.isBefore(dateFrom)) continue;
            if (dateTo != null && txDate.isAfter(dateTo)) continue;

            resultReports.add(snapshot);
        }

        return resultReports;
    }

}
