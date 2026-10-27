package model.contract;

public interface InterestBearing {
    double getInterestRate();
    long computeMonthlyInterest();
    default void logInterestPlan() {
        System.out.println("Interest Rate: " + getInterestRate() + "% per annum.");
    }
}