# MiniBank - Core Java Banking System

A modular, fault-tolerant, multithreaded console banking application developed in Java. The project simulates a banking core covering OOP principles, custom annotations, exception handling, multithreading, persistence, NIO reporting, and generic repository architectures.

---

## 📌 Features

- **OOP Architecture:** Layered implementation using abstract `Account` classes and specialized accounts (`SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`).
- **Validation & Annotations:** Custom runtime annotations (`@Id`, `@Positive`, `@MaxLength`) validated through Java Reflection.
- **Robust Exception Handling:** Custom checked exception hierarchy (`BankException`, `InsufficientFundsException`, `InvalidAmountException`, `AccountNotFoundException`) with resource management using `AutoCloseable`.
- **Multithreading & Concurrency:** Thread-safe operations using `synchronized` blocks and concurrent thread pooling via `ExecutorService`.
- **Persistence & Logging:** Serialization of account states (`.ser`), append-only transaction logging, and automated end-of-day summary reports via Java NIO.
- **Generic Data Access:** Generic `Repository<T>` pattern with reflective key resolution based on `@Id`.

---

## 📂 Project Structure

```text
Minibank/
├── src/
│   ├── Main.java
│   ├── DatabaseSession.java
│   ├── concurrency/
│   │   ├── AccountWorker.java
│   │   └── TransactionProcessor.java
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── BankException.java
│   │   ├── InsufficientFundsException.java
│   │   └── InvalidAmountException.java
│   ├── model/
│   │   ├── annotation/
│   │   │   ├── Id.java
│   │   │   ├── MaxLength.java
│   │   │   └── Positive.java
│   │   ├── contract/
│   │   │   ├── InterestBearing.java
│   │   │   ├── Premium.java
│   │   │   ├── Transactable.java
│   │   │   └── WithdrawRule.java
│   │   ├── Account.java
│   │   ├── BankInfo.java
│   │   ├── Command.java
│   │   ├── CurrentAccount.java
│   │   ├── Customer.java
│   │   ├── FixedDepositAccount.java
│   │   ├── MenuOption.java
│   │   ├── SavingsAccount.java
│   │   └── TransactionType.java
│   ├── persistence/
│   │   ├── ReportGenerator.java
│   │   ├── StatePersister.java
│   │   └── TransactionLog.java
│   ├── repository/
│   │   └── Repository.java
│   ├── service/
│   │   └── BankService.java
│   └── util/
│       ├── AnnotationValidator.java
│       ├── CommandParser.java
│       ├── StatementFormatter.java
│       └── Validator.java
└── README.md
