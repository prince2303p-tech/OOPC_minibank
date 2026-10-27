package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.contract.InterestBearing;

public class FixedDepositAccount extends Account implements InterestBearing {
    private static final long serialVersionUID = 1L;
    private final int tenureMonths;
    private final double interestRate;
    private boolean matured;

    public FixedDepositAccount(String accountNumber, String accountHolder, Customer customer, long balance, int tenureMonths, double interestRate) {
        super(accountNumber, accountHolder, customer, balance);
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.matured = false;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public long computeMonthlyInterest() {
        return Math.round(getBalance() * (interestRate / 100.0) / 12.0);
    }

    public void setMatured(boolean matured) {
        this.matured = matured;
    }

    @Override
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (!matured) {
            throw new InsufficientFundsException("Premature withdrawal not allowed before maturity.", amount);
        }
        super.withdraw(amount);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }
}