import java.util.InputMismatchException;
import java.util.Scanner;

public class ATMProgram {

    public static void main(String[] args) {
        CheckingAccount account = new CheckingAccount("12345678", "PINING GARCIA, JR.", 20000.00);

        Scanner scanner = new Scanner(System.in);

        int attempts = 0;
        boolean locked = false;

        while (true) {
            if (locked) {
                System.out.println("Account Unlocked. Please enter your correct pin.\n");
                locked = false; // unlock the account
                continue; // start the system again
            }

            System.out.print("Enter your PIN code: ");
            int pinCode = scanner.nextInt();

            if (account.validatePinCode(pinCode)) {
                System.out.println("Welcome, " + account.getAccountName() + "!");

                while (true) {
                    System.out.println("Choose a transaction:");
                    System.out.println("1. Withdraw");
                    System.out.println("2. Check Balance");
                    System.out.println("3. Deposit");
                    System.out.println("4. Exit");

                    int choice;
                    try {
                        System.out.print("Enter your Choice: ");
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid choice. Please try again.");
                        scanner.nextLine();
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            double withdrawAmount = 0;

                            //check for the user validation
                            try {
                                System.out.print("Enter amount to withdraw: ");
                                withdrawAmount = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid choice. Please try again.");
                                scanner.nextLine();
                                continue;
                            }

                            if (account.withdraw(withdrawAmount)) {
                                System.out.println("Successfully withdrew " + withdrawAmount + ". Your new balance is " + account.getBalance() + ".");
                            } else {
                                System.out.println("Insufficient funds.");
                            }
                            break;
                        case 2:
                            System.out.println("Your balance is " + account.getBalance() + ".");
                            break;
                        case 3:
                            double depositAmount = 0;

                            //check for user validation
                            try {
                                System.out.print("Enter amount to deposit: ");
                                depositAmount = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid choice. Please try again.");
                                scanner.nextLine();
                                continue;
                            }

                            account.deposit(depositAmount);
                            System.out.println("Successfully deposited " + depositAmount + ". Your new balance is " + account.getBalance() + ".");
                            break;
                        case 4:
                            System.out.println("Thank you for using this ATM. Goodbye!");
                            scanner.close();
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }

                    attempts = 0;

                    while (true) {
                        System.out.print("Do you want to perform another transaction? (Y/N) ");
                        String answer = scanner.next();
                        if (answer.equalsIgnoreCase("Y")) {
                            break; // exit the inner loop and perform another transaction
                        } else if (answer.equalsIgnoreCase("N")) {
                            System.out.println("Thank you for using this ATM. Goodbye!");
                            scanner.close();
                            System.exit(0);
                        } else {
                            System.out.println("Invalid choice. Please try again.");
                        }
                    }
                }
            } else {
                attempts++;
                if (attempts >= 3) {
                    locked = true;
                    attempts = 0;
                    System.out.println("Your account has been locked for 5 seconds. Please Wait.");
                    try {
                        Thread.sleep(5000); // wait for 5 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid PIN code. Please try again.");
                }
            }
        }
    }
}


class BankAccount {
    private String accountNumber;
    private String accountName;
    double balance;

    public BankAccount(String accountNumber, String accountName, double balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBalance() {
        return balance;
    }
}

class CheckingAccount extends BankAccount {
    private int transactionCount = 0;
    private int pinCode = 280281;

    public CheckingAccount(String accountNumber, String accountName, double balance) {
        super(accountNumber, accountName, balance);
    }

    public boolean validatePinCode(int pin) {
        return pin == pinCode;
    }

    public boolean withdraw(double amount) {
        if (amount > getBalance()) {
            return false;
        }

        transactionCount++;
        setBalance(getBalance() - amount);

        if (transactionCount % 3 == 0) {
            setBalance(getBalance() - 1.0);
        }

        return true;
    }

    public void deposit(double amount) {
        setBalance(getBalance() + amount);
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

}