package atm_project;
import java.util.*;
import java.sql.*;

public class ATM_Project {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in); // Scanner object for input
        try {
            int pin;
            int balance = 0;
            int Addamount = 0;
            int Takeamount = 0;
            String name = null;

            // JDBC connectivity
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_project", "root", "root");
            Statement stmt = con.createStatement();

            // Ask for PIN input
            System.out.print("Enter your PIN: ");
            pin = in.nextInt(); // Get the PIN from user input

            // Query to fetch account details based on entered PIN
            ResultSet rs = stmt.executeQuery("SELECT * FROM account WHERE account_number = " + pin);

            // If the account is found, proceed
            if (rs.next()) {
                name = rs.getString("name");
                balance = rs.getInt("balance");
                System.out.println("Welcome " + name);

                while (true) {
                    // Display options to the user
                    System.out.println("\nPress 1 to check your balance");
                    System.out.println("Press 2 to add amount");
                    System.out.println("Press 3 to withdraw amount");
                    System.out.println("Press 4 to get receipt");
                    System.out.println("Press 5 to Exit");
                    System.out.print("Choose an option: ");
                    int option = in.nextInt();

                    switch (option) {
                        case 1:
                            // Check balance
                            System.out.println("Your current balance is: " + balance);
                            break;

                        case 2:
                            // Add amount
                            System.out.print("Enter the amount to add: ");
                            Addamount = in.nextInt();
                            balance += Addamount;
                            System.out.println("Successfully credited. Your new balance is: " + balance);

                            // Update the balance in the database
                            stmt.executeUpdate("UPDATE account SET balance = " + balance + " WHERE account_number = " + pin);
                            break;

                        case 3:
                            // Withdraw amount
                            System.out.print("Enter the amount to withdraw: ");
                            Takeamount = in.nextInt();
                            if (balance < Takeamount) {
                                System.out.println("Insufficient balance.");
                            } else {
                                balance -= Takeamount;
                                System.out.println("Successfully debited. Your new balance is: " + balance);

                                // Update the balance in the database
                                stmt.executeUpdate("UPDATE account SET balance = " + balance + " WHERE account_number = " + pin);
                            }
                            break;

                        case 4:
                            // Print receipt
                            System.out.println("===== SBI Bank Receipt =====");
                            System.out.println("Account Holder: " + name);
                            System.out.println("Current Balance: " + balance);
                            System.out.println("Amount Deposited: " + Addamount);
                            System.out.println("Amount Withdrawn: " + Takeamount);
                            System.out.println("Thank you for banking with us!");
                            break;

                        case 5:
                            // Exit
                            System.out.println("Thank you for using the ATM. Goodbye!");
                            con.close();
                            in.close();
                            return;

                        default:
                            System.out.println("Invalid option. Please choose between 1 and 5.");
                            break;
                    }
                }
            } else {
                System.out.println("Invalid PIN. Access denied.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
