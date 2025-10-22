package net.biliarski.cashdesk.service.impl;

import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.repository.TransactionRepository;
import net.biliarski.cashdesk.service.TransactionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) throws IOException {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() throws IOException {
        return repository.findAll();
    }
    public List<Transaction> getAll() throws IOException {
        return repository.findAll();
    }



    /*************************/
//    public Map<String, Map<String, Map<String, CashierDailyBalance>>> getCashBalanceReport(
//            LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException {
//
//        List<Transaction> all = repository.findAll();
//        Map<String, Map<String, Map<String, CashierDailyBalance>>> result = new HashMap<>();
//
//        for (Transaction t : all) {
//            LocalDate date = t.getTimestamp().toLocalDate();
//
//            if ((dateFrom != null && date.isBefore(dateFrom)) ||
//                    (dateTo != null && date.isAfter(dateTo))) {
//                continue;
//            }
//
//            if (cashierNameFilter != null && !cashierNameFilter.isBlank() &&
//                    !t.getCashierName().equalsIgnoreCase(cashierNameFilter)) {
//                continue;
//            }
//
//            String dateStr = date.toString();
//            String cashierName = t.getCashierName();
//            String currencyStr = t.getCurrency().name();
//
//            result.putIfAbsent(dateStr, new HashMap<>());
//            Map<String, Map<String, CashierDailyBalance>> cashierMap = result.get(dateStr);
//
//            cashierMap.putIfAbsent(cashierName, new HashMap<>());
//            Map<String, CashierDailyBalance> currencyMap = cashierMap.get(cashierName);
//
//            CashierDailyBalance balance = currencyMap.getOrDefault(currencyStr, new CashierDailyBalance());
//
//
//            int multiplier;
//            switch (t.getType()) {
//                case DEPOSIT:
//                case LOAD:
//                    multiplier = 1;
//                    break;
//                case WITHDRAW:
//                case RETURN_TO_VAULT:
//                    multiplier = -1;
//                    break;
//                default:
//                    multiplier = 0; // or throw exception if unknown type
//            }
//
//
//            balance.amount += t.getDenominations().entrySet().stream()
//                    .mapToInt(e -> e.getKey() * e.getValue() * multiplier)
//                    .sum();
//
//            t.getDenominations().forEach((deno, count) ->
//                    balance.denominations.merge(deno, count * multiplier, Integer::sum)
//            );
//
//            currencyMap.put(currencyStr, balance);
//        }
//
//        return result;
//    }

//   // @Override
//    public Map<String, Map<String, Map<String, CashierDailyBalance>>> getCashBalanceReport_olllll(
//            LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException {
//
//        List<Transaction> all = repository.findAll();
//        Map<String, Map<String, Map<String, CashierDailyBalance>>> result = new HashMap<>();
//
//        for (Transaction t : all) {
//            LocalDate date = t.getTimestamp().toLocalDate();
//
//            if ((dateFrom != null && date.isBefore(dateFrom)) ||
//                    (dateTo != null && date.isAfter(dateTo))) {
//                continue;
//            }
//
//            if (cashierNameFilter != null && !cashierNameFilter.isBlank() &&
//                    !t.getCashierName().equalsIgnoreCase(cashierNameFilter)) {
//                continue;
//            }
//
//            String dateStr = date.toString();
//            String currencyStr = t.getCurrency().name();
//
//            result.putIfAbsent(dateStr, new HashMap<>());
//            Map<String, Map<String, CashierDailyBalance>> currencyMap = result.get(dateStr);
//
//            currencyMap.putIfAbsent(currencyStr, new HashMap<>());
//            Map<String, CashierDailyBalance> cashierMap = currencyMap.get(currencyStr);
//
//            CashierDailyBalance balance = cashierMap.getOrDefault(t.getCashierName(), new CashierDailyBalance());
//
//            balance.amount += t.getAmount();
//
//            t.getDenominations().forEach((deno, count) ->
//                    balance.denominations.merge(deno, count, Integer::sum)
//            );
//
//            cashierMap.put(t.getCashierName(), balance);
//        }
//
//        return result;
//    }

//    //@Override
//    public Map<String, Map<String, CashierDailyBalance>> getCashBalanceReport_Old(
//            LocalDate dateFrom, LocalDate dateTo, String cashierNameFilter) throws IOException {
//
//        List<Transaction> all = repository.findAll();
//        Map<String, Map<String, CashierDailyBalance>> result = new HashMap<>();
//
//        for (Transaction t : all) {
//            LocalDate date = t.getTimestamp().toLocalDate();
//
//            // Filter by date range
//            if ((dateFrom != null && date.isBefore(dateFrom)) ||
//                    (dateTo != null && date.isAfter(dateTo))) {
//                continue;
//            }
//
//            // Filter by cashier name if given
//            if (cashierNameFilter != null && !cashierNameFilter.isBlank()
//                    && !t.getCashierName().equalsIgnoreCase(cashierNameFilter)) {
//                continue;
//            }
//
//            String dateStr = date.toString();
//            result.putIfAbsent(dateStr, new HashMap<>());
//            Map<String, CashierDailyBalance> cashierMap = result.get(dateStr);
//
//            CashierDailyBalance balance = cashierMap.getOrDefault(t.getCashierName(), new CashierDailyBalance());
//
//            // Sum amounts
//            balance.amount += t.getAmount();
//
//            // Sum denominations
//            t.getDenominations().forEach((deno, count) ->
//                    balance.denominations.merge(deno, count, Integer::sum)
//            );
//
//            cashierMap.put(t.getCashierName(), balance);
//        }
//
//        return result;
//    }

    // Helper class to hold daily totals and denominations
    public static class CashierDailyBalance {
        public int amount = 0;
        public Map<Integer, Integer> denominations = new HashMap<>();
    }
    /*************************/


}
