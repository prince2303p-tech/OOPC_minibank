package concurrency;

import model.Account;

public class AccountWorker implements Runnable {
    private final Account account;
    private final int iterations;
    private final long amount;

    public AccountWorker(Account account, int iterations, long amount) {
        this.account = account;
        this.iterations = iterations;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            account.deposit(amount);
        }
    }
}