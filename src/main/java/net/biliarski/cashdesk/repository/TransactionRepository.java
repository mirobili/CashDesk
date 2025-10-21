package net.biliarski.cashdesk.repository;

import net.biliarski.cashdesk.model.Transaction;
import java.io.IOException;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction) throws IOException;
    List<Transaction> findAll() throws IOException;
}