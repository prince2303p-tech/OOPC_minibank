package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Customer;
import model.SavingsAccount;
import persistence.ReportGenerator;
import persistence.StatePersister;
import persistence.TransactionLog;
import repository.Repository;
import util.AnnotationValidator;
import util.StatementFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BankService {
    private final Repository<Account, String> repository = new Repository<>();
    private final AtomicInteger counter = new AtomicInteger(1000);

    public Account openAccount(String name, String email, String mobile, String line, String city, String pin, long initial) throws BankException {
        String accNo = "ACC" + counter.incrementAndGet();
        Customer.Address addr = new Customer.Address(line, city, pin);
        Customer customer = new Customer(name, email, mobile, addr);
        Account account = new SavingsAccount(accNo, name, customer, initial, 4.0);

        try {
            AnnotationValidator.validate(customer);
            AnnotationValidator.validate(account);
        } catch (Exception e) {
            throw new BankException("Validation failed: " + e.getMessage());
        }

        repository.save(accNo, account);
        TransactionLog.log("ACCOUNT_OPEN: " + accNo + " by " + name + " with " + initial);
        return account;
    }

    public Account getAccount(String accNo) throws AccountNotFoundException {
        return repository.findById(accNo).orElseThrow(() -> new AccountNotFoundException("Account " + accNo + " not found."));
    }

    public void deposit(String accNo, long amount) throws BankException {
        Account acc = getAccount(accNo);
        acc.deposit(amount);
        TransactionLog.log("DEPOSIT: " + accNo + " amount " + amount);
    }

    public void withdraw(String accNo, long amount) throws BankException {
        Account acc = getAccount(accNo);
        acc.withdraw(amount);
        TransactionLog.log("WITHDRAW: " + accNo + " amount " + amount);
    }

    public void transfer(String from, String to, long amount) throws BankException {
        if (from.equals(to)) {
            throw new InvalidAmountException("Source and destination accounts must be different.");
        }
        Account src = getAccount(from);
        Account dst = getAccount(to);
        src.transferTo(dst, amount);
        TransactionLog.log("TRANSFER: From " + from + " to " + to + " amount " + amount);
    }

    public String statement(String accNo) throws AccountNotFoundException {
        Account acc = getAccount(accNo);
        return StatementFormatter.format(acc);
    }

    public List<Account> listAccountsSortedByBalance() {
        List<Account> list = new ArrayList<>(repository.findAll());
        Collections.sort(list);
        return list;
    }

    public void saveState() {
        StatePersister.saveAccounts(new ArrayList<>(repository.findAll()));
    }

    public void loadState() {
        List<Account> accounts = StatePersister.loadAccounts();
        for (Account a : accounts) {
            repository.save(a.getAccountNumber(), a);
        }
    }

    public void generateDailyReport() {
        ReportGenerator.generateDailyReport(repository.findAll());
    }
}