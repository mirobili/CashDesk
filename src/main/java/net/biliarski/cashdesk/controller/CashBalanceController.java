package net.biliarski.cashdesk.controller;

import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.repository.impl.TransactionFileRepository;
import net.biliarski.cashdesk.service.CashBalanceService;
import net.biliarski.cashdesk.service.TransactionService;
import net.biliarski.cashdesk.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cash-balance")
public class CashBalanceController {

    private final CashBalanceService cashBalanceService;

    @Value("${app.api.key}")
    private String configuredApiKey;

    @Value("${transactions.file.path}")
    private String transactionsFilePath;

    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);


    public CashBalanceController(CashBalanceService cashBalanceService) {
        this.cashBalanceService = cashBalanceService;
    }
    
    @GetMapping(produces = "application/json")
    public ResponseEntity<BalanceResponse> getCashierBalance(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestParam String cashierName) {
        
        // Validate API key

        if (!configuredApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }
        
        // Get balance information from the service
        BalanceResponse response = cashBalanceService.getCashierBalance(cashierName);
        
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/list", produces = "application/json")
    public ResponseEntity<List<Transaction>> getTransactionsList2(
             @RequestHeader("FIB-X-AUTH") String apiKey
            //   @RequestParam String cashierName
    ) throws IOException
    {
        if (!configuredApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }


        TransactionService transactionService = new TransactionServiceImpl(new TransactionFileRepository(transactionsFilePath));
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }


    @GetMapping(value = {"/CashBalanceReport", "/report", ""}, produces = "application/json")
    public ResponseEntity<?> cashBalanceReport(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String cashierName
            ) throws IOException {

          logger.info("Received CashBalanceReport request with apiKey: {}", apiKey);
//        logger.info("Received CashBalanceReport configuredApiKeyKEY: {}", configuredApiKey);

        if (!configuredApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        TransactionService transactionService = new TransactionServiceImpl(new TransactionFileRepository(transactionsFilePath));
        Map<String, Map<String, Map<String, TransactionServiceImpl.CashierDailyBalance>>> report =  transactionService.getCashBalanceReport(dateFrom, dateTo, cashierName);

        return ResponseEntity.ok(report);
    }
}
