package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public final class TransactionLog {
    private static final Path LOG_PATH = Paths.get("data", "transactions.log");

    private TransactionLog() {}

    public static synchronized void log(String message) {
        try {
            Path parent = LOG_PATH.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            String line = LocalDateTime.now() + " - " + message + System.lineSeparator();
            Files.writeString(LOG_PATH, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("TransactionLog Error: " + e.getMessage());
        }
    }
}