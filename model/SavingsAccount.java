package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.contract.InterestBearing;

public class SavingsAccount extends Account implements InterestBearing {
    private static final long serialVersionUID = 1L;
    private final double interestRate;

    public SavingsAccount(String accountNumber, String accountHolder, Customer customer, long balance, double interestRate) {
        super(accountNumber, accountHolder, customer, balance);
        this.interestRate = interestRate;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public long computeMonthlyInterest() {
        return Math.round(getBalance() * (interestRate / 100.0) / 12.0);
    }

    @Override
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        long minBalance = 500;
        if (getBalance() - amount < minBalance) {
            throw new InsufficientFundsException("Maintaining minimum balance of 500 is required.", amount - (getBalance() - minBalance));
        }
        super.withdraw(amount);
    }
}