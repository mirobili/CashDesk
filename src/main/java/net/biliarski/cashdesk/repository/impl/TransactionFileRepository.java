package net.biliarski.cashdesk.repository.impl;

import net.biliarski.cashdesk.model.Currency;
import net.biliarski.cashdesk.model.Transaction;
import net.biliarski.cashdesk.repository.TransactionRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;

public class TransactionFileRepository implements TransactionRepository {

    private final Path filePath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TransactionFileRepository(String filename) {
        this.filePath = Paths.get(filename);
    }

    @Override
    public synchronized Transaction save(Transaction transaction) throws IOException {
        String line = toLine(transaction);
        Files.write(filePath, (line + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() throws IOException {
        if (!Files.exists(filePath)) {
            return List.of();
        }
        return Files.lines(filePath)
                .map(this::fromLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Methods to convert Transaction to/from line
//    private String toLine(Transaction tx) { /* ... as previously shown ... */ }
//    private Transaction fromLine(String line) { /* ... as previously shown ... */ }

    private String toLine(Transaction tx) {
        String denoString = tx.getDenominations().entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));
        return String.join("|",
                tx.getId(),
                tx.getTimestamp().format(formatter),
                tx.getCashierName(),
                String.valueOf(tx.getAmount()),
                tx.getCurrency().name(),
                tx.getType().name(),
                denoString);
    }

    private Transaction fromLine(String line) {
        try {
            String[] parts = line.split("\\|");
            LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Map<Integer, Integer> denoMap = Arrays.stream(parts[6].split(","))
                    .map(entry -> entry.split(":"))
                    .collect(Collectors.toMap(
                            a -> Integer.parseInt(a[0]),
                            a -> Integer.parseInt(a[1])
                    ));

            return new Transaction(
                    //String id,
                    //LocalDateTime timestamp,
                    parts[0],
                    timestamp,
                    parts[2],
                    Integer.parseInt(parts[3]),
                    Currency.valueOf(parts[4]),
                    Transaction.TransactionType.valueOf(parts[5]),
                    denoMap
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
