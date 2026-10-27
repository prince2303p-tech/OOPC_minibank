package model;

public enum MenuOption {
    OPEN_ACCOUNT(1, "Open Account"),
    DEPOSIT(2, "Deposit"),
    WITHDRAW(3, "Withdraw"),
    TRANSFER(4, "Transfer"),
    STATEMENT(5, "Print Statement"),
    LIST_ACCOUNTS(6, "List Accounts"),
    CONCURRENCY_TEST(7, "Run Concurrency Test"),
    DAILY_REPORT(8, "Generate Daily Report"),
    EXIT(9, "Exit");

    private final int code;
    private final String label;

    MenuOption(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static MenuOption fromCode(int code) {
        for (MenuOption opt : values()) {
            if (opt.code == code) {
                return opt;
            }
        }
        return null;
    }
}