package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;
import model.contract.Transactable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Account implements Transactable, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    protected String accountNumber;

    @MaxLength(50)
    protected String accountHolder;

    protected Customer customer;

    @Positive
    protected long balance;

    protected List<String> transactions = new ArrayList<>();

    public Account(String accountNumber, String accountHolder, Customer customer, long initialBalance) {
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.customer = customer;
        this.balance = initialBalance;
        this.transactions.add("Account created with balance: " + initialBalance);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public Customer getCustomer() { return customer; }
    public List<String> getTransactions() { return Collections.unmodifiableList(transactions); }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        this.balance += amount;
        this.transactions.add("Deposited: " + amount + " | Balance: " + this.balance);
    }

    @Override
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (this.balance < amount) {
            throw new InsufficientFundsException("Insufficient balance.", amount - this.balance);
        }
        this.balance -= amount;
        this.transactions.add("Withdrew: " + amount + " | Balance: " + this.balance);
    }

    public void transferTo(Account target, long amount) throws InsufficientFundsException, InvalidAmountException {
        Account firstLock = this.accountNumber.compareTo(target.accountNumber) < 0 ? this : target;
        Account secondLock = this.accountNumber.compareTo(target.accountNumber) < 0 ? target : this;

        synchronized (firstLock) {
            synchronized (secondLock) {
                this.withdraw(amount);
                target.deposit(amount);
                this.transactions.add("Transferred " + amount + " to " + target.getAccountNumber());
                target.transactions.add("Received " + amount + " from " + this.getAccountNumber());
            }
        }
    }

    @Override
    public int compareTo(Account o) {
        return Long.compare(o.balance, this.balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return "Account{" + "number='" + accountNumber + '\'' + ", holder='" + accountHolder + '\'' + ", balance=" + balance + '}';
    }
}