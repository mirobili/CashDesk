package net.biliarski.cashdesk.service.impl;

import net.biliarski.cashdesk.controller.CashOperationController;
import net.biliarski.cashdesk.dto.CashOperationRequest;
import net.biliarski.cashdesk.dto.CashOperationResponse;
import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.model.Transaction.TransactionType;
import net.biliarski.cashdesk.repository.TransactionRepository;
import net.biliarski.cashdesk.repository.impl.TransactionFileRepository;
import net.biliarski.cashdesk.service.CashOperationService;
import net.biliarski.cashdesk.service.TransactionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;


import java.util.Map;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CashOperationServiceImpl implements CashOperationService {


    private TransactionService service;
    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);
    private Map<String, Set<Integer>> currencyDenominationsMap = new HashMap<>();

    @Override
    public CashOperationResponse processCashOperation(CashOperationRequest request) throws IOException {


        service = new TransactionServiceImpl(new TransactionFileRepository("src/main/data/transactions.txt"));
        loadCurrencyDenominations(  "src/main/data/currency_denominations.txt");
        Map<Integer, Integer> denos = request.getDenominations();

        int amount = 0;
        switch (request.getType()) {
            case DEPOSIT:
            case LOAD:

                    amount = denos.entrySet().stream()
                        .mapToInt(e -> e.getKey() * e.getValue())
                        .sum();
                    denos = request.getDenominations();

//                    validateDenominations(request.getCurrency().name(), denos);
                    try {
                        validateDenominations(request.getCurrency().name(), denos);
                    } catch (IllegalArgumentException e) {
                        logger.error("Validation failed for denominations: {}", e.getMessage());
                        return CashOperationResponse.error("Invalid denominations: " + e.getMessage());
                    }

                break;
            case WITHDRAW:
            case RETURN_TO_VAULT:

                 //   amount = request.getAmount();
//                    denos = getCashierDenominations(request.getCashierName(), request.getCurrency(), amount);
//                    logger.info("Denominations for cashier " + request.getCashierName() + ": " + denos);

                amount = request.getAmount();
                try {
                    denos = getCashierDenominations(request.getCashierName(), request.getCurrency(), amount);
                    logger.info("Denominations for cashier " + request.getCashierName() + ": " + denos);
                } catch (Exception e) {
                    logger.error("Error getting denominations for cashier " + request.getCashierName(), e);
                    // Return or set error response here, depending on your method's signature
                    return CashOperationResponse.error("Failed to get denominations: " + e.getMessage());
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type");
        }

        Transaction tx = new Transaction(request.getCashierName(), amount, request.getCurrency(),
                request.getType(), denos );

        service.createTransaction(tx);

        // Create and populate response
        CashOperationResponse response = new CashOperationResponse();
        response.setTransactionId(tx.getId());
        response.setCashierName(tx.getCashierName());
        response.setAmount(tx.getAmount());
        response.setCurrency(tx.getCurrency());
        response.setType(tx.getType());
        response.setTimestamp(tx.getTimestamp());
        response.setDenominations( tx.getDenominations() );
        response.setSuccess( true);

        return response;
    }

    public Map<Integer, Integer> getAvailableDenominationsForCashier(String cashierName, Currency currency) throws IOException {
        TransactionFileRepository repository = new TransactionFileRepository("src/main/data/transactions.txt");
        List<Transaction> all = repository.findAll();

        logger.info("Calculating available denominations for cashier {} and currency {}", cashierName, currency);

        Map<Integer, Integer> availableDenominations = new HashMap<>();

        for (Transaction t : all) {
            if (!t.getCashierName().equalsIgnoreCase(cashierName)) {
                continue;
            }
            if (!t.getCurrency().equals(currency)) {
                continue;
            }

          //  int multiplier = (t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;

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


            t.getDenominations().forEach((deno, count) -> {
                int currentCount = availableDenominations.getOrDefault(deno, 0);
                int updatedCount = currentCount + multiplier * count;
                if (updatedCount < 0) {
                    logger.warn("Denomination {} count became negative ({}), resetting to zero", deno, updatedCount);
                    updatedCount = 0;  // Avoid negative denomination count
                }
                availableDenominations.put(deno, updatedCount);
            });
        }

        // Remove denominations with zero count for cleaner map
        availableDenominations.entrySet().removeIf(e -> e.getValue() == null || e.getValue() == 0);

        logger.info("Available denominations computed: {}", availableDenominations);
        return availableDenominations;
    }

//    public Map<Integer, Integer> getAvailableDenominationsForCashier(String cashierName, Currency currency) throws IOException {
//
////        service = new TransactionServiceImpl(new TransactionFileRepository("src/main/data/transactions.txt"));
//        TransactionFileRepository repository = new TransactionFileRepository("src/main/data/transactions.txt");
//        List<Transaction> all = repository.findAll();
//
//
//        logger.info("Available denominations for cashier " + cashierName + ": " + currency);
//
//       // List<Transaction> all = service.getAllTransactions();
//        Map<Integer, Integer> availableDenominations = new HashMap<>();
//
//        for (Transaction t : all) {
//            if (!t.getCashierName().equalsIgnoreCase(cashierName)) {
//                continue;
//            }
//            if (!t.getCurrency().equals(currency)) {
//                continue;
//            }
//
//            // For deposits add denominations count, for withdrawals subtract count
//            int multiplier = (t.getType() == Transaction.TransactionType.DEPOSIT) ? 1 : -1;
//
//            t.getDenominations().forEach((deno, count) ->
//                    availableDenominations.merge(deno, multiplier * count, Integer::sum)
//            );
//        }
//
//        // Remove denominations with zero or negative counts (not available)
//        availableDenominations.entrySet().removeIf(e -> e.getValue() == null || e.getValue() <= 0);
//
//        return availableDenominations;
//    }
//    public Map<Integer, Integer> getCashierDenominations(String cashierName, Currency currency, int amount) throws IllegalArgumentException, IOException {
//
//        logger.info("Available denominations for cashier " + " getCashierDenominations" +  cashierName + "," + currency+ ", " + amount);
//
//        Map<Integer, Integer> availableDenos = getAvailableDenominationsForCashier(cashierName, currency);
//
//        logger.info("Available denominations for cashier " + cashierName + ": " + availableDenos);
//
//
//        // Extract denominations and sort descending (largest first)
//        List<Integer> denomOrder = new ArrayList<>(availableDenos.keySet());
//        Collections.sort(denomOrder, Collections.reverseOrder());
//
//        Map<Integer, Integer> result = new LinkedHashMap<>();
//
//        for (int denom : denomOrder) {
//            int needed = amount / denom;
//            int available = availableDenos.getOrDefault(denom, 0);
//            int count = Math.min(needed, available);
//
//            if (count > 0) {
//                result.put(denom, count);
//                amount -= denom * count;
//            }
//        }
//
//        if (amount != 0) {
//            throw new IllegalArgumentException(
//                    "Cannot fulfill amount with available denominations for cashier " + cashierName);
//        }
//
//        return result;
//    }

//    public Map<Integer, Integer> getCashierDenominations_neww(String cashierName, Currency currency, int amount) throws IllegalArgumentException, IOException {
//        Map<Integer, Integer> availableDenos = getAvailableDenominationsForCashier(cashierName, currency);
//
//        if (currency != Currency.BGN) {
//            // General greedy algorithm for other currencies
//            List<Integer> denomOrder = new ArrayList<>(availableDenos.keySet());
//            Collections.sort(denomOrder, Collections.reverseOrder());
//
//            Map<Integer, Integer> result = new LinkedHashMap<>();
//            int remaining = amount;
//
//            for (int denom : denomOrder) {
//                int needed = remaining / denom;
//                int available = availableDenos.getOrDefault(denom, 0);
//                int count = Math.min(needed, available);
//                if (count > 0) {
//                    result.put(denom, count);
//                    remaining -= denom * count;
//                }
//            }
//            if (remaining != 0) throw new IllegalArgumentException("Cannot fulfill amount with available denominations for cashier " + cashierName);
//            return result;
//        }
//
//        // Custom logic for BGN
//        int remaining = amount;
//        Map<Integer, Integer> result = new LinkedHashMap<>();
//        int max50 = Math.min(remaining / 50, availableDenos.getOrDefault(50, 0));
//
//        // Try combinations starting from max number of 50s down to zero
//        for (int fifties = max50; fifties >= 0; fifties--) {
//            int remAfter50 = remaining - fifties * 50;
//            int twentiesAvail = availableDenos.getOrDefault(20, 0);
//            int tensAvail = availableDenos.getOrDefault(10, 0);
//
//            for (int twenties = 0; twenties <= twentiesAvail; twenties++) {
//                int remAfter20 = remAfter50 - twenties * 20;
//                if (remAfter20 < 0) break;
//
//                if (remAfter20 % 10 == 0) {
//                    int tensNeeded = remAfter20 / 10;
//                    if (tensNeeded <= tensAvail) {
//                        // success
//                        result.clear();
//                        if (fifties > 0) result.put(50, fifties);
//                        if (twenties > 0) result.put(20, twenties);
//                        if (tensNeeded > 0) result.put(10, tensNeeded);
//                        return result;
//                    }
//                }
//            }
//        }
//
//        throw new IllegalArgumentException("Cannot fulfill amount with available denominations for cashier " + cashierName);
//    }

    public Map<Integer, Integer> getCashierDenominations(String cashierName, Currency currency, int amount) throws IllegalArgumentException, IOException {
        Map<Integer, Integer> availableDenos = getAvailableDenominationsForCashier(cashierName, currency);

        // Sort denominations descending (largest first)
        List<Integer> denomOrder = new ArrayList<>(availableDenos.keySet());
        Collections.sort(denomOrder, Collections.reverseOrder());

        Map<Integer, Integer> result = new LinkedHashMap<>();
        int remaining = amount;

        if (currency == Currency.BGN) {
            // Special logic for BGN: try largest bill then smaller notes per your requirement
            // For example, try one 50, then fill rest with 10s (or a mix)
            int count50 = Math.min(1, availableDenos.getOrDefault(50, 0));
            if (count50 > 0 && remaining >= 50) {
                result.put(50, count50);
                remaining -= 50 * count50;
            }

            // For the rest, greedily use 10s and then smaller if necessary
            int count10 = Math.min(remaining / 10, availableDenos.getOrDefault(10, 0));
            if (count10 > 0) {
                result.put(10, count10);
                remaining -= 10 * count10;
            }

            // You can extend this with 20 BGN bills or other denominations as needed
            if (remaining > 0 && availableDenos.containsKey(20)) {
                int count20 = Math.min(remaining / 20, availableDenos.get(20));
                if (count20 > 0) {
                    result.put(20, count20);
                    remaining -= 20 * count20;
                }
            }
        } else {
            // General case for other currencies: greedy approach largest-to-smallest
            for (int denom : denomOrder) {
                int needed = remaining / denom;
                int available = availableDenos.getOrDefault(denom, 0);
                int count = Math.min(needed, available);

                if (count > 0) {
                    result.put(denom, count);
                    remaining -= denom * count;
                }
            }
        }

        if (remaining != 0) {
            throw new IllegalArgumentException("Cannot fulfill amount with available denominations for cashier " + cashierName);
        }

        return result;
    }




    public void loadCurrencyDenominations(String currencyFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(currencyFilePath));
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                String currencyCode = parts[0].trim();
                Set<Integer> denos = Arrays.stream(parts[1].split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                currencyDenominationsMap.put(currencyCode, denos);
            }
        }
    }

    public void validateDenominations(String currencyCode, Map<Integer, Integer> denominations) {
        Set<Integer> allowedDenos = currencyDenominationsMap.get(currencyCode);
        if (allowedDenos == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
        for (Integer denom : denominations.keySet()) {
            if (!allowedDenos.contains(denom)) {
                throw new IllegalArgumentException("Denomination " + denom + " not supported for currency " + currencyCode);
            }
        }
    }

}
