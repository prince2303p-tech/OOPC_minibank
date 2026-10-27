package persistence;

import model.Account;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;

public final class ReportGenerator {
    private ReportGenerator() {}

    public static void generateDailyReport(Collection<Account> accounts) {
        Path reportPath = Paths.get("data", "daily_report.txt");
        try {
            Path parent = reportPath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("=== DAILY BANK REPORT - ").append(LocalDate.now()).append(" ===\n");
            long total = 0;
            for (Account acc : accounts) {
                sb.append(acc.getAccountNumber()).append(" | ")
                  .append(acc.getAccountHolder()).append(" | Balance: ")
                  .append(acc.getBalance()).append("\n");
                total += acc.getBalance();
            }
            sb.append("------------------------------------------\n");
            sb.append("Total Accounts: ").append(accounts.size()).append("\n");
            sb.append("Total Bank Liquidity: ").append(total).append("\n");

            Files.writeString(reportPath, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Report Generation Error: " + e.getMessage());
        }
    }
}