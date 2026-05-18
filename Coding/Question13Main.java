// Q13

// Custom Exception
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Wallet Class
class Wallet {
    private double balance;

    public Wallet(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (balance < amount) {
            throw new InsufficientBalanceException("Declined: Insufficient funds. Required: $" 
                    + amount + ", Available: $" + balance);
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

// Main Runner Execution
public class Question13Main {
    public static void main(String[] args) {
        // 1. Create wallet with $100
        Wallet wallet = new Wallet(100.0);
        System.out.println("Initial Balance: $" + wallet.getBalance());

        // 2. Deposit $50
        wallet.deposit(50.0);
        System.out.println("After depositing $50: $" + wallet.getBalance());

        // 3 & 4. Try to withdraw $200 and catch exception
        try {
            System.out.println("Attempting to withdraw $200...");
            wallet.withdraw(200.0);
        } catch (InsufficientBalanceException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }

        // 5. Print final balance
        System.out.println("Final Balance: $" + wallet.getBalance());
    }
}
