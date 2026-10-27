import concurrency.AccountWorker;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.BankInfo;
import model.Customer;
import model.SavingsAccount;
import model.contract.WithdrawRule;
import service.BankService;

import static util.Validator.isValidEmail;
import static util.Validator.isValidMobile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class DatabaseSession implements AutoCloseable {
    public DatabaseSession() {
        System.out.println("[Session] MiniBank Storage Connected.");
    }

    @Override
    public void close() {
        System.out.println("[Session] MiniBank Storage Safely Disconnected.");
    }
}

public class Main {
    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("CHARUSAT MiniBank", "CSPIT Changa Branch");
        System.out.println("==================================================");
        System.out.println("   " + bankInfo.name() + " - " + bankInfo.branch());
        System.out.println("==================================================");

        BankService service = new BankService();
        service.loadState();

        WithdrawRule anonRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.getBalance() >= amount;
            }
        };

        WithdrawRule lambdaRule = (account, amount) -> account.getBalance() >= amount;

        if (anonRule.equals(lambdaRule)) {}

        try (DatabaseSession session = new DatabaseSession();
             Scanner scanner = new Scanner(System.in)) {

            if (session.toString() != null) {}

            boolean running = true;
            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Open Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. Print Statement");
                System.out.println("6. List Accounts (Sorted by Balance)");
                System.out.println("7. Run Multithreaded Race/Sync Test");
                System.out.println("8. Generate End-of-Day Report");
                System.out.println("9. Exit");
                System.out.print("Enter choice (1-9): ");

                String input = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                try {
                    switch (choice) {
                        case 1 -> {
                            System.out.print("Account Holder Name: ");
                            String name = scanner.nextLine().trim();

                            System.out.print("Email: ");
                            String email = scanner.nextLine().trim();
                            if (!isValidEmail(email)) {
                                System.out.println("Invalid email format.");
                                break;
                            }

                            System.out.print("Mobile: ");
                            String mobile = scanner.nextLine().trim();
                            if (!isValidMobile(mobile)) {
                                System.out.println("Invalid mobile number.");
                                break;
                            }

                            System.out.print("Address Line: ");
                            String line = scanner.nextLine().trim();
                            System.out.print("City: ");
                            String city = scanner.nextLine().trim();
                            System.out.print("Pincode: ");
                            String pin = scanner.nextLine().trim();

                            System.out.print("Initial Deposit: ");
                            long initial = Long.parseLong(scanner.nextLine().trim());

                            Account acc = service.openAccount(name, email, mobile, line, city, pin, initial);
                            System.out.println("Account successfully opened: " + acc);
                        }
                        case 2 -> {
                            System.out.print("Enter Account Number: ");
                            String accNo = scanner.nextLine().trim();
                            System.out.print("Enter Deposit Amount: ");
                            long amount = Long.parseLong(scanner.nextLine().trim());
                            service.deposit(accNo, amount);
                            System.out.println("Deposit successful.");
                        }
                        case 3 -> {
                            System.out.print("Enter Account Number: ");
                            String accNo = scanner.nextLine().trim();
                            System.out.print("Enter Withdrawal Amount: ");
                            long amount = Long.parseLong(scanner.nextLine().trim());
                            service.withdraw(accNo, amount);
                            System.out.println("Withdrawal successful.");
                        }
                        case 4 -> {
                            System.out.print("Source Account Number: ");
                            String src = scanner.nextLine().trim();
                            System.out.print("Destination Account Number: ");
                            String dst = scanner.nextLine().trim();
                            System.out.print("Transfer Amount: ");
                            long amount = Long.parseLong(scanner.nextLine().trim());
                            service.transfer(src, dst, amount);
                            System.out.println("Transfer successful.");
                        }
                        case 5 -> {
                            System.out.print("Enter Account Number: ");
                            String accNo = scanner.nextLine().trim();
                            System.out.println(service.statement(accNo));
                        }
                        case 6 -> {
                            System.out.println("\nAll Accounts (Sorted by Balance Descending):");
                            for (Account a : service.listAccountsSortedByBalance()) {
                                System.out.println(a);
                            }
                        }
                        case 7 -> {
                            System.out.println("Executing 10 threads doing 1000 deposits each of 1 unit...");
                            Customer tempCust = new Customer("TestUser", "test@test.com", "9876543210", new Customer.Address("L", "C", "000000"));
                            Account raceAcc = new SavingsAccount("RACE001", "TestUser", tempCust, 0, 0);
                            Thread[] workers = new Thread[10];
                            for (int i = 0; i < 10; i++) {
                                workers[i] = new Thread(new AccountWorker(raceAcc, 1000, 1), "Worker-" + i);
                                workers[i].start();
                            }
                            for (Thread t : workers) {
                                t.join();
                            }
                            System.out.println("Concurrency Test Finished. Final Balance: " + raceAcc.getBalance() + " (Expected: 10000)");
                        }
                        case 8 -> {
                            service.generateDailyReport();
                            System.out.println("Daily Report generated at data/daily_report.txt:");
                            Files.lines(Paths.get("data", "daily_report.txt")).forEach(System.out::println);
                        }
                        case 9 -> {
                            running = false;
                            service.saveState();
                            System.out.println("State saved. Thank you for using MiniBank!");
                        }
                        default -> System.out.println("Invalid option. Please choose between 1 and 9.");
                    }
                } catch (InsufficientFundsException e) {
                    System.err.println("Transaction Failed: " + e.getMessage() + " (Shortfall: " + e.getShortfall() + ")");
                } catch (BankException e) {
                    System.err.println("Bank Exception: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    System.out.println("Transaction cycle complete.");
                }
            }
        }
    }
}