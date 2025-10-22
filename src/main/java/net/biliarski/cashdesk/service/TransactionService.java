package net.biliarski.cashdesk.service;

import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.service.impl.TransactionServiceImpl;
//import net.biliarski.cashdesk.service.TransactionService;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface TransactionService {
    Transaction createTransaction(Transaction transaction) throws IOException;

    List<Transaction> getAllTransactions() throws IOException;

    List<Transaction> getAll() throws IOException;

}