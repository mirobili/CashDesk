package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionsTest {

    @Autowired
    private TransactionService transactionService;


    @BeforeEach
    public void setup() throws Exception {
        // Create temp file for repository storage


       // tempFile = Files.createTempFile("transactions_test", ".txt");
        //TransactionRepository repo = new TransactionFileRepository(tempFile.toString());

    }

    @AfterEach
    public void cleanup() throws Exception {
        // Delete temp file after tests
//       if( 1 ==0 ) {
//           Files.deleteIfExists(tempFile);
//       }
    }

    @Test
    public void testCreateAndRetrieveTransaction() throws Exception {
        Transaction tx = new Transaction("cashier2", 40, Currency.EUR,
                Transaction.TransactionType.DEPOSIT, Map.of(10, 2, 20, 1));
        transactionService.createTransaction(tx);

        List<Transaction> all = transactionService.getAllTransactions();
        assertFalse(all.isEmpty()); // Check list is not empty
        Transaction lastTransaction = all.get(all.size() - 1); // Get last element
        assertEquals(tx.getId(), lastTransaction.getId()); // Compare IDs or other fields

    }
    @Test
    public void testCreateAndRetrieveTransactions() throws Exception {
//        Transaction tx = new Transaction("cashier2", 40, Currency.EUR,
//                Transaction.TransactionType.DEPOSIT, Map.of(10, 2, 20, 1));
//        transactionService.createTransaction(tx);
//
//
//        Transaction tx2 = new Transaction("cashier2", 90, Currency.EUR,
//                Transaction.TransactionType.WITHDRAW, Map.of(50, 1, 20, 2));
//        transactionService.createTransaction(tx2);
//
//        Transaction tx3 = new Transaction("cashier2", 60, Currency.EUR,
//                Transaction.TransactionType.DEPOSIT, Map.of(20, 2, 10, 2));
//        transactionService.createTransaction(tx3);
//
//        Transaction tx4 = new Transaction("cashier2", 120, Currency.EUR,
//                Transaction.TransactionType.DEPOSIT, Map.of(50, 1, 20, 1,10,3));
//        transactionService.createTransaction(tx4);
//
//        Transaction tx5 = new Transaction("cashier2", 120, Currency.EUR,
//                Transaction.TransactionType.WITHDRAW, Map.of(50, 1, 20, 1,10,3));
//        transactionService.createTransaction(tx5);
//
//        Transaction tx6 = new Transaction("cashier2", 200, Currency.BGN,
//                Transaction.TransactionType.DEPOSIT, Map.of(50, 2, 10, 10));
//        transactionService.createTransaction(tx6);
//
//        List<Transaction> all = transactionService.getAllTransactions();
//  //      assertFalse(all.isEmpty());
////        assertEquals(tx.getId(), all.get(0).getId());
//
//        assertFalse(all.isEmpty()); // Check list is not empty
//        Transaction lastTransaction = all.get(all.size() - 1); // Get last element
//        assertEquals(tx.getId(), lastTransaction.getId()); // Compare IDs or other fields
//
//        Transaction retrieved = all.get(0);
//        assertEquals("cashier1", retrieved.getCashierName());
//        assertEquals(100, retrieved.getAmount());
//        assertEquals(Currency.EUR, retrieved.getCurrency());
//        assertEquals(Transaction.TransactionType.DEPOSIT, retrieved.getType());
//
//        Map<Integer, Integer> deno = retrieved.getDenominations();
//        assertEquals(2, deno.get(50));
//        assertEquals(1, deno.get(20));
    }

    // More tests can be added for edge cases, invalid inputs, etc.
}
