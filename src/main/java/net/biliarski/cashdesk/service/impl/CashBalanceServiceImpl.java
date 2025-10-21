package net.biliarski.cashdesk.service.impl;

import net.biliarski.cashdesk.dto.BalanceResponse;
import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.service.CashBalanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CashBalanceServiceImpl implements CashBalanceService {

    @Override
    public BalanceResponse getCashierBalance(String cashierName) {
        BalanceResponse balanceResponse = new BalanceResponse();

        balanceResponse.setCashierName(cashierName);
        balanceResponse.setBalances(calcBalance(generateDummyDenominations()));
        balanceResponse.setDenominations(generateDummyDenominations());
        balanceResponse.setTimestamp(LocalDateTime.now());
        return balanceResponse;
    }

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



    private Map<Currency, Integer> generateDummyBalances( Map<Currency, Map<Integer, Integer>> denominations) {

        return Map.of(
            Currency.BGN, 1500,
            Currency.EUR, 2000
        );
    }

    private Map<Currency, Map<Integer, Integer>> generateDummyDenominations() {
        return Map.of(
            Currency.BGN, Map.of(
                50, 21,  // 20 bills of 50 BGN
                10, 50   // 50 bills of 10 BGN
            ),
            Currency.EUR, Map.of(
                100, 10,  // 10 bills of 100 EUR
                50, 20    // 20 bills of 50 EUR
            )
        );
    }
}
