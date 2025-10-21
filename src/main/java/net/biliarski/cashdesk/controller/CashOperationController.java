package net.biliarski.cashdesk.controller;

import net.biliarski.cashdesk.dto.CashOperationRequest;
import net.biliarski.cashdesk.dto.CashOperationResponse;
import net.biliarski.cashdesk.service.CashOperationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/v1/cash-operation")
public class CashOperationController {

    @Value("${app.api.key}")
    private String configuredApiKey;
    private final CashOperationService cashOperationService;
    private static final Logger logger = LoggerFactory.getLogger(CashOperationController.class);

    public CashOperationController(CashOperationService cashOperationService) {
        this.cashOperationService = cashOperationService;
    }

    @PostMapping
    // public ResponseEntity<CashOperationResponse> processCashOperation(
    public ResponseEntity<?> processCashOperation(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestBody CashOperationRequest request) throws IOException {


        logger.info("Received CashBalanceReport request with apiKey: {}", apiKey);
        logger.info("Received CashBalanceReport configuredApiKeyKEY: {}", configuredApiKey);

        //  logger.info("CashOperationRequest",request.toString());
        logger.info("Received CashOperationRequest: {}", request);
        // Validate API key
//        if (!"f9Uie8nNf112hx8s".equals(apiKey)) {
        if (!configuredApiKey.equals(apiKey)) {
            logger.warn("Unauthorized request with invalid API key");
            return ResponseEntity.status(401).build();
        }


        try {
            CashOperationResponse response = cashOperationService.processCashOperation(request);
            logger.info("CashOperation Response {}", response);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            // Log the exception details for debugging
            logger.error("Error processing cash operation", e);
            return ResponseEntity.status(500).body("Server error while processing operation");
        }
    }
}
