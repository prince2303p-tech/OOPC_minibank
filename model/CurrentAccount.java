package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.contract.Premium;

public class CurrentAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolder, Customer customer, long balance, long overdraftLimit) {
        super(accountNumber, accountHolder, customer, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (balance + overdraftLimit < amount) {
            throw new InsufficientFundsException("Overdraft limit exceeded.", amount - (balance + overdraftLimit));
        }
        this.balance -= amount;
        this.transactions.add("Withdrew: " + amount + " (Overdraft used if bal < 0) | Balance: " + this.balance);
    }
}