package net.biliarski.cashdesk.controller;

import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.service.CashBalanceService;

import net.biliarski.cashdesk.service.TransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cash-balance")
public class CashBalanceController {

    private final CashBalanceService cashBalanceService;

    @Value("${app.api.key}")
    private String configuredApiKey;

    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);
    private TransactionService  transactionService;


    @Autowired
    public CashBalanceController(CashBalanceService cashBalanceService, TransactionService  transactionService) {
        this.cashBalanceService = cashBalanceService;
        this.transactionService = transactionService;

    }

    @GetMapping(value = {"/",""}, produces = "application/json")
    public ResponseEntity<?> getBalanceReport(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String cashier
    ) throws IOException {

        logger.info("getBalanceReport: cashier=" + cashier + ", dateFrom=" + dateFrom + ", dateTo=" + dateTo);

        if (!configuredApiKey.equals(apiKey)) {
             return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(cashBalanceService.getBalanceReport(cashier, dateFrom , dateTo ));
    }

    @GetMapping(value = "/transactionsList", produces = "application/json")
    public ResponseEntity<List<Transaction>> getTransactionsList(
            @RequestHeader("FIB-X-AUTH") String apiKey
    ) throws IOException
    {
        if (!configuredApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        List<Transaction> transactions = transactionService.getAll();
        return ResponseEntity.ok(transactions);
    }

}
