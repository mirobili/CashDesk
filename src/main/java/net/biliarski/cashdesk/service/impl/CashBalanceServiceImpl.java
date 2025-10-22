package net.biliarski.cashdesk.service.impl;

import net.biliarski.cashdesk.controller.CashOperationController;
import net.biliarski.cashdesk.dto.BalanceReport;
import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.service.CashBalanceService;
import net.biliarski.cashdesk.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashBalanceServiceImpl implements CashBalanceService {
    private final TransactionService transactionService;
    // private ProcessHandleImpl logger;
    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);
    @Autowired
    public CashBalanceServiceImpl(TransactionService transactionService) {
         this.transactionService = transactionService;
    }


//    TransactionService transactionService) {
//        this.transactionService = transactionService;
//

   // BalanceResponse
  //  @Override
//    public BalanceResponse getCashierBalance(String cashierName) {
//        BalanceResponse balanceResponse = new BalanceResponse();
//
//        balanceResponse.setCashierName(cashierName);
//        balanceResponse.setBalances(calcBalance(generateDummyDenominations()));
//        balanceResponse.setDenominations(generateDummyDenominations());
//        balanceResponse.setTimestamp(LocalDateTime.now());
//        return balanceResponse;
//    }

   //  @Override
    //public Map<String, Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>>> getCashBalanceReport(LocalDate dateFrom, LocalDate dateTo, String cashierName) {
    public Map<String, Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>>> getCashBalanceReport(
                LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException {

            List<Transaction> all = transactionService.getAll();
            Map<String, Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>>> result = new HashMap<>();

            for (Transaction t : all) {
                LocalDate date = t.getTimestamp().toLocalDate();

                if ((dateFrom != null && date.isBefore(dateFrom)) ||
                        (dateTo != null && date.isAfter(dateTo))) {
                    continue;
                }

                if (cashierNameFilter != null && !cashierNameFilter.isBlank() &&
                        !t.getCashierName().equalsIgnoreCase(cashierNameFilter)) {
                    continue;
                }

                String dateStr = date.toString();
                String cashierName = t.getCashierName();
                String currencyStr = t.getCurrency().name();

                result.putIfAbsent(dateStr, new HashMap<>());
                Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>> cashierMap = result.get(dateStr);

                cashierMap.putIfAbsent(cashierName, new HashMap<>());
                Map<String, TransactionServiceImpl.CashierDailyBalance> currencyMap = cashierMap.get(cashierName);

                TransactionServiceImpl.CashierDailyBalance balance = currencyMap.getOrDefault(currencyStr, new TransactionServiceImpl.CashierDailyBalance());


                int multiplier;
                switch (t.getType()) {
                    case DEPOSIT:
                    case LOAD:
                        multiplier = 1;
                        break;
                    case WITHDRAW:
                    case RETURN_TO_VAULT:
                        multiplier = -1;
                        break;
                    default:
                        multiplier = 0; // or throw exception if unknown type
                }


                balance.amount += t.getDenominations().entrySet().stream()
                        .mapToInt(e -> e.getKey() * e.getValue() * multiplier)
                        .sum();

                t.getDenominations().forEach((deno, count) ->
                        balance.denominations.merge(deno, count * multiplier, Integer::sum)
                );

                currencyMap.put(currencyStr, balance);
            }

            return result;
        }

//@Override
    public Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>> getCashBalanceReport2(
            LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException {

        List<Transaction> all = transactionService.getAll();

        // Map of cashier -> currency -> balance
        Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>> result = new HashMap<>();

        for (Transaction t : all) {
            LocalDate date = t.getTimestamp().toLocalDate();

            if ((dateFrom != null && date.isBefore(dateFrom)) || (dateTo != null && date.isAfter(dateTo))) {
                continue;
            }

            if (cashierNameFilter != null && !cashierNameFilter.isBlank() &&
                    !t.getCashierName().equalsIgnoreCase(cashierNameFilter)) {
                continue;
            }

            String cashierName = t.getCashierName();
            String currencyStr = t.getCurrency().name();

            result.putIfAbsent(cashierName, new HashMap<>());
            Map<String, TransactionServiceImpl.CashierDailyBalance> currencyMap = result.get(cashierName);

            TransactionServiceImpl.CashierDailyBalance balance = currencyMap.getOrDefault(currencyStr, new TransactionServiceImpl.CashierDailyBalance());

            int multiplier;
            switch (t.getType()) {
                case DEPOSIT:
                case LOAD:
                    multiplier = 1000;
                    break;
                case WITHDRAW:
                case RETURN_TO_VAULT:
                    multiplier = -1000;
                    break;
                default:
                    multiplier = 0; // or throw exception if unknown type
            }

            balance.amount += t.getDenominations().entrySet().stream()
                    .mapToInt(e -> e.getKey() * e.getValue() * multiplier)
                    .sum();

            t.getDenominations().forEach((deno, count) ->
                    balance.denominations.merge(deno, count * multiplier, Integer::sum)
            );

            currencyMap.put(currencyStr, balance);
        }

        return result;
    }




    //}
//
//
//    public List<BalanceReport> getBalanceReport(String cashier,
//                                                // String currency,
//                                                LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException {
//        // Get all transactions from transaction service
//        List<Transaction> allTransactions = transactionService.getAll();
//
//        Map<String, BalanceReport> reportMap = new HashMap<>();
//
//        // Calculate balances for entire history filtered by cashier and currency
//        for (Transaction t : allTransactions) {
//            if (cashier != null && !t.getCashierName().equals(cashier)) continue;
//            // if (currency != null && !t.getCurrency().equals(currency)) continue;
//
//            String key = t.getCashierName() + "|" + t.getCurrency().toString();
//            BalanceReport br = reportMap.computeIfAbsent(key, k -> new BalanceReport(t.getCashierName(), t.getCurrency().toString()));
////            int sign = (t.getType().equals("LOAD") || t.getType().equals("DEPOSIT")) ? 1 : -1;
//            int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;
//
//            br.amount += sign * t.getAmount();
//
//            for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
//                br.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
//            }
//        }
//
//        // Filter reports by date range for presentation only
//        if (dateFrom != null || dateTo != null) {
//            Map<String, BalanceReport> filteredMap = new HashMap<>();
//
//            for (Transaction t : allTransactions) {
//                if (cashier != null && !t.getCashierName().equalsIgnoreCase(cashier)) continue;
//                // if (currency != null && !t.getCurrency().equals(currency)) continue;
//                if (dateFrom != null && t.getTimestamp().isBefore(dateFrom)) continue;
//                if (dateTo != null && t.getTimestamp().isAfter(dateTo)) continue;
//
//                String key = t.getCashierName() + "|" + t.getCurrency().toString();
//                BalanceReport br = filteredMap.computeIfAbsent(key, k -> new BalanceReport(t.getCashierName(), t.getCurrency().toString()));
//                int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;
//                br.amount += sign * t.getAmount();
//
//                for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
//                    br.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
//                }
//            }
//
//            return new ArrayList<>(filteredMap.values());
//        }
//
//        return new ArrayList<>(reportMap.values());
//    }
//


    @Override
    public List<BalanceReport> getBalanceReport(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException {

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
//            int sign = (t.getType() == Transaction.TransactionType.LOAD ||
//                    t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;

            int sign = t.getType().getOperation();

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

    public List<BalanceReport> getBalanceReport_prpl1(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException {

        logger.info("getBalanceReport: cashier=" + cashier + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo);

        // Get all transactions sorted by timestamp ascending
        List<Transaction> allTransactions = transactionService.getAll().stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());

        // Map to keep running balance per cashier+currency key
        Map<String, BalanceReport> runningBalances = new HashMap<>();

        List<BalanceReport> resultReports = new ArrayList<>();

        for (Transaction t : allTransactions) {
            String key = t.getCashierName() + "|" + t.getCurrency();

            BalanceReport currentBalance = runningBalances.computeIfAbsent(key, k -> {
                BalanceReport br = new BalanceReport();
                br.cashier = t.getCashierName();
                br.currency = t.getCurrency().toString();
                br.amount = 0;
                br.denominations = new HashMap<>();
                return br;
            });

            int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;

            // Update running balance amount
            currentBalance.amount += sign * t.getAmount();

            // Update running denomination counts
            for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
                currentBalance.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
            }

            // Create snapshot for this transaction with current cumulative balance and timestamp
            BalanceReport snapshot = new BalanceReport();
            snapshot.cashier = currentBalance.cashier;
            snapshot.currency = currentBalance.currency;
            snapshot.amount = currentBalance.amount;
            snapshot.denominations = new HashMap<>(currentBalance.denominations);
            snapshot.timestamp = t.getTimestamp();

            // Apply filters for output report
            if (cashier != null && !snapshot.cashier.equalsIgnoreCase(cashier)) {
                continue;
            }
            LocalDate txDate = snapshot.timestamp.toLocalDate();
            if (dateFrom != null && txDate.isBefore(dateFrom)) {
                continue;
            }
            if (dateTo != null && txDate.isAfter(dateTo)) {
                continue;
            }

            resultReports.add(snapshot);
        }

        return resultReports;
    }

    public List<BalanceReport> getBalanceReport_working(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException {

        logger.info("getBalanceReport: cashier=" + cashier + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo);
        List<Transaction> allTransactions = transactionService.getAll();

        List<BalanceReport> reports = new ArrayList<>();

        for (Transaction t : allTransactions) {
            if (cashier != null && !t.getCashierName().equalsIgnoreCase(cashier)) continue;

            if (dateFrom != null && t.getTimestamp().toLocalDate().isBefore(dateFrom)) continue;
            if (dateTo != null && t.getTimestamp().toLocalDate().isAfter(dateTo)) continue;

            int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;

            BalanceReport br = new BalanceReport();
            br.cashier = t.getCashierName();
            br.currency = t.getCurrency().toString();
            br.timestamp = t.getTimestamp(); // add the transaction timestamp
            br.amount = sign * t.getAmount();

            // Add all denominations with sign applied
            br.denominations = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
                br.denominations.put(entry.getKey(), sign * entry.getValue());
            }

            reports.add(br);
        }

        return reports;
    }
//    public List<BalanceReport> getBalanceReport(String cashier, LocalDate dateFrom, LocalDate dateTo) throws IOException {
//        // Get all transactions from transaction service
//
//         List<Transaction> allTransactions = transactionService.getAll();
//
//
//
//        Map<String, BalanceReport> reportMap = new HashMap<>();
//
//        // Calculate balances for entire history filtered by cashier and currency
//        for (Transaction t : allTransactions) {
//            if (cashier != null && !t.getCashierName().equalsIgnoreCase(cashier)) continue;
//         //   if (currency != null && !t.getCurrency().equalsIgnoreCase(currency)) continue;
//
//            String key = t.getCashierName() + "|" + t.getCurrency();
//            BalanceReport br = reportMap.computeIfAbsent(key, k -> new BalanceReport(t.getCashierName(), t.getCurrency().toString()));
//            int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;
//            br.amount += sign * t.getAmount();
//
//            for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
//                br.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
//            }
//        }
//
//        // Filter reports by date range for presentation only
//        if (dateFrom != null || dateTo != null) {
//            Map<String, BalanceReport> filteredMap = new HashMap<>();
//
//            for (Transaction t : allTransactions) {
//                if (cashier != null && !t.getCashierName().equalsIgnoreCase(cashier)) continue;
//               // if (currency != null && !t.getCurrency().equalsIgnoreCase(currency)) continue;
//                if (dateFrom != null && t.getTimestamp().isBefore(ChronoLocalDateTime.from(dateFrom))) continue;
//                if (dateTo != null && t.getTimestamp().isAfter(ChronoLocalDateTime.from(dateTo))) continue;
//
//                String key = t.getCashierName() + "|" + t.getCurrency();
//                BalanceReport br = filteredMap.computeIfAbsent(key, k -> new BalanceReport(t.getCashierName(), t.getCurrency().toString()));
//                int sign = (t.getType() == Transaction.TransactionType.LOAD || t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;
//                br.amount += sign * t.getAmount();
//
//                for (Map.Entry<Integer, Integer> entry : t.getDenominations().entrySet()) {
//                    br.denominations.merge(entry.getKey(), sign * entry.getValue(), Integer::sum);
//                }
//            }
//
//            return new ArrayList<>(filteredMap.values());
//        }
//
//        return new ArrayList<>(reportMap.values());
//    }
//




    private Map<Currency, Integer> calcBalance(Map<Currency, Map<Integer, Integer>> denominations) {
        Map<Currency, Integer> balances = new HashMap<>();

        for (Map.Entry<Currency, Map<Integer, Integer>> entry : denominations.entrySet()) {
            Currency currency = entry.getKey();
            Map<Integer, Integer> currencyDenoms = entry.getValue();

            // sum up (denomination × count)
            int total = currencyDenoms.entrySet().stream()
                    .mapToInt(e -> e.getKey() * e.getValue())
                    .sum();

            balances.put(currency, total);
        }

        return balances;
    }





}
