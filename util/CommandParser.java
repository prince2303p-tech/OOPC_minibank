package util;

import model.Command;

import java.util.Arrays;

public final class CommandParser {
    private CommandParser() {}

    public static Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new Command("", new String[0]);
        }
        String[] tokens = input.trim().split("\\s+");
        String keyword = tokens[0].toUpperCase();
        String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
        return new Command(keyword, args);
    }
}