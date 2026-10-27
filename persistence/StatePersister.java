package persistence;

import model.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class StatePersister {
    private static final String DATA_FILE = "data/accounts.ser";

    private StatePersister() {}

    public static void saveAccounts(List<Account> accounts) {
        try {
            Path path = Paths.get("data");
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(new ArrayList<>(accounts));
            }
        } catch (IOException e) {
            System.err.println("StatePersister Save Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Account> loadAccounts() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("StatePersister Load Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}