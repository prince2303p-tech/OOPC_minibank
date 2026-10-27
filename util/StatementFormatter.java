package util;

import model.Account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class StatementFormatter {
    private StatementFormatter() {}

    public static String format(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("               ACCOUNT STATEMENT                  \n");
        sb.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Account Number : ").append(account.getAccountNumber()).append("\n");
        sb.append("Holder Name    : ").append(account.getAccountHolder()).append("\n");
        sb.append("Current Balance: ").append(account.getBalance()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("Transactions:\n");
        for (String tx : account.getTransactions()) {
            sb.append("  * ").append(tx).append("\n");
        }
        sb.append("==================================================\n");
        return sb.toString();
    }
}