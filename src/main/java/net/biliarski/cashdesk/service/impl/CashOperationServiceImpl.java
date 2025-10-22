package net.biliarski.cashdesk.service.impl;

import net.biliarski.cashdesk.controller.CashOperationController;
import net.biliarski.cashdesk.dto.CashOperationRequest;
import net.biliarski.cashdesk.dto.CashOperationResponse;
import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.repository.impl.TransactionFileRepository;
import net.biliarski.cashdesk.service.CashOperationService;
import net.biliarski.cashdesk.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import java.util.Map;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CashOperationServiceImpl implements CashOperationService {


    private TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);
    private Map<String, Set<Integer>> currencyDenominationsMap = new HashMap<>();

    @Value("${denominations.file.path}")
    private String denominationsFilePath;

    public CashOperationServiceImpl(TransactionService service) {
        this.transactionService = service;
    }

    @Override
    public CashOperationResponse processCashOperation(CashOperationRequest request) throws IOException {


        loadCurrencyDenominations(denominationsFilePath);
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

        transactionService.createTransaction(tx);

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
            int multiplier;
            multiplier = t.getType().getOperation();


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




    public  Map<Integer, Integer> getCashierDenominations(String cashier , Currency currency, int amount) throws IllegalArgumentException, IOException {
        Map<Integer, Integer> availableDenos = getAvailableDenominationsForCashier(cashier, currency);

        // Sort denominations descending (largest first)
        List<Integer> denomOrder = new ArrayList<>(availableDenos.keySet());
        denomOrder.sort(Collections.reverseOrder());

        Map<Integer, Integer> result = new LinkedHashMap<>();
        int remaining = amount;

        // First greedy fill using largest denominations
        for (int denom : denomOrder) {
            int countNeeded = remaining / denom;
            int countAvailable = availableDenos.getOrDefault(denom, 0);
            int countToUse = Math.min(countNeeded, countAvailable);

            if (countToUse > 0) {
                result.put(denom, countToUse);
                remaining -= denom * countToUse;
            }
        }

        if (remaining != 0) {
            throw new IllegalArgumentException("Cannot fulfill amount with available denominations for cashier " + cashier);
        }

        // For BGN, attempt to replace one of the highest denomination bills
        if (currency == Currency.BGN && !denomOrder.isEmpty()) {
            int highestDenom = denomOrder.get(0);
            int highestCount = result.getOrDefault(highestDenom, 0);

            // Only try if we have at least 2 or more of the highest bills (can adjust threshold)
            if (highestCount >= 2) {
                // Try to replace one highest bill by smaller denominations summing to same value
                int denomValue = highestDenom;
                Map<Integer, Integer> smallerDenos = new LinkedHashMap<>();
                int valueLeft = denomValue;

                // Start from the next smaller denomination(s)
                for (int i = 1; i < denomOrder.size(); i++) {
                    int denom = denomOrder.get(i);
                    int avail = availableDenos.getOrDefault(denom, 0);
                    int used = result.getOrDefault(denom, 0);
                    int free = avail - used;

                    if (free <= 0) continue;

                    int needed = valueLeft / denom;
                    if (needed > free) needed = free;

                    if (needed > 0) {
                        smallerDenos.put(denom, needed);
                        valueLeft -= denom * needed;
                        if (valueLeft == 0) break;
                    }
                }

                // If exact replacement found, apply it
                if (valueLeft == 0) {
                    // Remove one highest bill
                    result.put(highestDenom, highestCount - 1);
                    // Add smaller denominations
                    for (Map.Entry<Integer, Integer> entry : smallerDenos.entrySet()) {
                        int denom = entry.getKey();
                        int count = entry.getValue();
                        result.put(denom, result.getOrDefault(denom, 0) + count);
                    }
                    // Remove highest denomination if count zero
                    if (result.get(highestDenom) == 0) {
                        result.remove(highestDenom);
                    }
                }
            }
        }

        // Clean up zero counts
        result.entrySet().removeIf(e -> e.getValue() == 0);

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
