package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AccountingLedgerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("..........Welcome to your Accounting Ledger app................");

        System.out.println("1. Add Deposit");
        System.out.println("2. Make Payment");
        System.out.println("3. Ledger ");
        System.out.println("4. Exit ");
        System.out.print("Please Choose an option: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addDeposit();
                break;
            case 2:
                makePayment();
                break;
            case 3:
                displayLedger();
                break;
            case 4:
                exit();
                break;
            default:
                System.out.println("Invalid option. Please choose again.");
        }
    }

    public static ArrayList<Ledger> getExpenses() {
        ArrayList<Ledger> expenses = new ArrayList<>();
        try {
            FileReader filereader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufreader = new BufferedReader(filereader);
            String input;
            bufreader.readLine();
            while ((input = bufreader.readLine()) != null) {
                String[] fileSplit = input.split(Pattern.quote("|"));
                String date = fileSplit[0];
                String time = fileSplit[1];
                String description = fileSplit[2];
                String vendor = fileSplit[3];
                double amount = Double.parseDouble(fileSplit[4]);
                expenses.add(new Ledger(date, time, description, vendor, amount));
            }
            bufreader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public static void addDeposit() {
        Scanner scanner = new Scanner(System.in);
        try (FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true)) {
            System.out.println("Please Enter your information correctly so we can process your Deposit");
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("Please enter the date and time (yyyy-MM-dd HH:mm:ss): " + date + " | " + time);
            System.out.println("Brief Description: ");
            String description = scanner.nextLine();
            System.out.println("Who is the Vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("How much does it cost");
            double price = scanner.nextDouble();
            scanner.nextLine();

            fileWriter.write(date + " | " + time + " | " + description + " | " + vendor + " | " + price + "\n");

            System.out.println("Deposit added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makePayment() {
        Scanner scanner = new Scanner(System.in);
        try (FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true)) {
            System.out.println("Please Enter your information correctly so we can process your Payment");
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("Please enter the date and time (yyyy-MM-dd HH:mm:ss): " + date + " | " + time);
            System.out.println("Brief Description: ");
            String description = scanner.nextLine();
            System.out.println("Who is the Vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("Enter Payment: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            fileWriter.write(date + " | " + time + " | " + description + " | " + vendor + " | " + (-amount) + "\n");

            System.out.println("Payment made successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static void displayLedger() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("..........Welcome to your ledger screen............");
        System.out.println("1. Display All Entries");
        System.out.println("2. Display Deposits Only");
        System.out.println("3. Display Payments Only");
        System.out.println("4. Month to Date");
        System.out.println("5. Previous Month");
        System.out.println("6. Year to Date");
        System.out.println("7. Previous Year");
        System.out.println("8. Search by Vendor");
        System.out.println("9. Back to the Report Page");
        System.out.println("10. Back to Home Screen");
        System.out.print("Please Choose an option: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                displayAllEntries();
                break;
            case 2:
                displayDeposits();
                break;
            case 3:
                displayPayments();
                break;
            case 4:
                displayMonthToDate();
                break;
            case 5:
                displayPreviousMonth();
                break;
            case 6:
                displayYearToDate();
                break;
            case 7:
                displayPreviousYear();
                break;
            case 8:
                searchByVendor();
                break;
            case 9:
                return; // Go back to the report page
            case 10:
                return; // Go back to the home screen
            default:
                System.out.println("Invalid option. Please choose again.");
        }
    }

    private static void displayAllEntries() {
        System.out.println("Displaying All Entries:");
        displayEntries(false, false);
    }

    private static void displayDeposits() {
        System.out.println("Displaying Deposits Only:");
        displayEntries(true, false);
    }

    private static void displayPayments() {
        System.out.println("Displaying Payments Only:");
        displayEntries(false, true);
    }

    private static void displayMonthToDate() {
        ArrayList<Ledger> expenses = getExpenses();
        double total = 0;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentMonth = currentDate.format(formatter).substring(0, 7);

        for (Ledger expense : expenses) {
            if (expense.getDate().startsWith(currentMonth)) {
                total += expense.getAmount();
                System.out.println(expense);
            }
        }
        System.out.println("Total for Month to Date: " + total);
    }

    private static void displayPreviousMonth() {
        ArrayList<Ledger> expenses = getExpenses();
        double total = 0;
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonth = currentDate.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String previousMonthString = previousMonth.format(formatter);

        for (Ledger expense : expenses) {
            if (expense.getDate().startsWith(previousMonthString)) {
                total += expense.getAmount();
                System.out.println(expense);
            }
        }
        System.out.println("Total for Previous Month: " + total);
    }

    private static void displayYearToDate() {
        ArrayList<Ledger> expenses = getExpenses();
        double total = 0;
        LocalDate currentDate = LocalDate.now();
        String currentYear = String.valueOf(currentDate.getYear());

        for (Ledger expense : expenses) {
            if (expense.getDate().startsWith(currentYear)) {
                total += expense.getAmount();
                System.out.println(expense);
            }
        }
        System.out.println("Total for Year to Date: " + total);
    }

    private static void displayPreviousYear() {
        ArrayList<Ledger> expenses = getExpenses();
        double total = 0;
        LocalDate currentDate = LocalDate.now();
        int previousYear = currentDate.minusYears(1).getYear();
        String previousYearString = String.valueOf(previousYear);

        for (Ledger expense : expenses) {
            if (expense.getDate().startsWith(previousYearString)) {
                total += expense.getAmount();
                System.out.println(expense);
            }
        }
        System.out.println("Total for Previous Year: " + total);
    }

    private static void searchByVendor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Vendor Name: ");
        String vendorName = scanner.nextLine();
        ArrayList<Ledger> expenses = getExpenses();
        double total = 0;

        for (Ledger expense : expenses) {
            if (expense.getVendor().equalsIgnoreCase(vendorName)) {
                total += expense.getAmount();
                System.out.println(expense);
            }
        }
        System.out.println("Total for " + vendorName + ": " + total);
    }

    private static void displayEntries(boolean depositsOnly, boolean paymentsOnly) {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/transactions.csv"))) {
            System.out.println("Date | Time | Description | Vendor | Amount");
            System.out.println("--------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);
                if ((depositsOnly && amount >= 0) || (paymentsOnly && amount < 0) || (!depositsOnly && !paymentsOnly)) {
                    System.out.printf("%s | %s | %s | %s | %s%n",
                            parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        System.out.println("Exiting the Accounting Ledger app bye bye!");
    }
}


