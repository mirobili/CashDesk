package net.biliarski.cashdesk.controller;
/*
import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.service.CashBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionListController {

    private final CashBalanceService cashBalanceService;

    public TransactionListController(CashBalanceService cashBalanceService) {
        this.cashBalanceService = cashBalanceService;
    }
    
    @GetMapping(produces = "application/json")
    public ResponseEntity<BalanceResponse> getCashierBalance(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestParam String cashierName) {
        
        // Validate API key
        if (!"f9Uie8nNf112hx8s".equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }
        
        // Get balance information from the service
        BalanceResponse response = cashBalanceService.getCashierBalance(cashierName);
        
        return ResponseEntity.ok(response);
    }


    //@GetMapping(produces = "application/json")
    @GetMapping(value = "/impr", produces = "application/json")
    //@GetMapping("/api/v1/cash-balance-impr")
    public ResponseEntity<BalanceResponse> getCashierBalanceImproved(
            @RequestHeader("FIB-X-AUTH") String apiKey,
            @RequestParam String cashierName) {

        // Validate API key
        if (!"f9Uie8nNf112hx8s".equals(apiKey)) {
            return ResponseEntity.status(401).build();
        }

        // Get balance information from the service
       // BalanceResponse response = cashBalanceService.getCashierBalance(cashierName);
        //BalanceResponse response = "teeeeeeeeeeeeeeeeeeeeeeeeeeeeesttttttttt" ;

        BalanceResponse response = new BalanceResponse();
        response.setCashierName("Heeeeeeeeeeeeeeeeeeeeeeeeeeeelllooooooooooo");
        // response.setBalances(generateDummyBalances());
        // response.setDenominations(generateDummyDenominations());
        // response.setTimestamp(LocalDateTime.now());


        return ResponseEntity.ok(response);
    }
}
*/