import java.util.Scanner;

public class Cinemadriver {
 
    private static final DataStorage storage = new DataStorage();
    private static final Scanner      scan    = new Scanner(System.in);
 
    private static User currentUser    = null;
    private static int  reportCounter  = 1;
 
    // ================================================================
    // ENTRY POINT
    // ================================================================
 
    public static void main(String[] args) {
        printBanner();
        storage.loadAll();
 
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showMainMenu();
            } else if (currentUser instanceof Customer) {
                showCustomerMenu((Customer) currentUser);
            } else if (currentUser instanceof Staff) {
                showStaffMenu((Staff) currentUser);
            }
        }
 
        System.out.println("\n  Thank you for using the Cinema System. Goodbye!\n");
    }
 
    // ================================================================
    // MAIN MENU
    // ================================================================
 
    private static boolean showMainMenu() {
        System.out.println("\n  +------------------------------+");
        System.out.println("  |         MAIN MENU            |");
        System.out.println("  |------------------------------|");
        System.out.println("  |  [1] Login                   |");
        System.out.println("  |  [2] Register as Customer    |");
        System.out.println("  |  [3] Register as Staff       |");
        System.out.println("  |  [0] Exit                    |");
        System.out.println("  +------------------------------+");
 
        int choice = readInt("  Select option: ", 0, 3);
        switch (choice) {
            case 1: handleLogin();            break;
            case 2: handleRegisterCustomer(); break;
            case 3: handleRegisterStaff();    break;
            case 0: return false;
        }
        return true;
    }
 
    // ================================================================
    // LOGIN
    // ================================================================
 
    private static void handleLogin() {
        System.out.println("\n  --- Login ---");
        try {
            System.out.print("  Username : ");
            String username = scan.nextLine().trim();
 
            System.out.print("  Password : ");
            String password = scan.nextLine().trim();
 
            if (username.isEmpty() || password.isEmpty())
                throw new IllegalArgumentException("Username and password cannot be empty.");
 
            User user = storage.loginLookup(username, password);
            if (user == null)
                throw new IllegalArgumentException("Invalid username or password.");
 
            currentUser = user;
            System.out.println("\n  Login successful! Welcome, " + user.getDisplayName());
 
        } catch (IllegalArgumentException e) {
            System.out.println("  Login failed - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error during login: " + e.getMessage());
        }
    }
 
    // ================================================================
    // REGISTER - CUSTOMER
    // ================================================================
 
    private static void handleRegisterCustomer() {
        System.out.println("\n  --- Register New Customer ---");
        try {
            System.out.print("  Choose username : ");
            String username = scan.nextLine().trim();
 
            if (username.isEmpty())
                throw new IllegalArgumentException("Username cannot be empty.");
            if (storage.findByUsername(username) != null)
                throw new IllegalArgumentException("Username already taken. Please choose another.");
 
            System.out.print("  Password (min 6 chars) : ");
            String password = scan.nextLine().trim();
 
            if (password.length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters.");
 
            String   customerID  = storage.generateCustomerID();
            Customer newCustomer = new Customer(customerID, username, password);
 
            if (storage.addCustomer(newCustomer)) {
                System.out.println("\n  Registration successful!");
                System.out.println("  Your Customer ID : " + customerID);
                System.out.println("  You can now log in with your username and password.");
            }
 
        } catch (IllegalArgumentException e) {
            System.out.println("  Registration error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }
 
    // ================================================================
    // REGISTER - STAFF
    // ================================================================
 
    private static void handleRegisterStaff() {
        System.out.println("\n  --- Register New Staff ---");
        try {
            System.out.print("  Full name       : ");
            String staffName = scan.nextLine().trim();
 
            if (staffName.isEmpty())
                throw new IllegalArgumentException("Staff name cannot be empty.");
 
            System.out.print("  Choose username : ");
            String username = scan.nextLine().trim();
 
            if (username.isEmpty())
                throw new IllegalArgumentException("Username cannot be empty.");
            if (storage.findByUsername(username) != null)
                throw new IllegalArgumentException("Username already taken. Please choose another.");
 
            System.out.print("  Password (min 6 chars) : ");
            String password = scan.nextLine().trim();
 
            if (password.length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters.");
 
            String staffID  = storage.generateStaffID();
            Staff  newStaff = new Staff(staffID, staffName, username, password);
 
            if (storage.addStaff(newStaff)) {
                System.out.println("\n  Staff registration successful!");
                System.out.println("  Your Staff ID : " + staffID);
                System.out.println("  You can now log in with your username and password.");
            }
 
        } catch (IllegalArgumentException e) {
            System.out.println("  Registration error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }
 
    // ================================================================
    // CUSTOMER MENU
    // ================================================================
 
    private static void showCustomerMenu(Customer customer) {
        customer.displayMenu();
        int choice = readInt("  Select option: ", 0, 2);
 
        switch (choice) {
            case 1: viewProfile(customer);            break;
            case 2: handleDeleteOwnAccount(customer); break;
            case 0:
                customer.logout();
                currentUser = null;
                break;
        }
    }
 
    // ---------- View Profile ----------
    private static void viewProfile(User user) {
        System.out.println("\n  --- My Profile ---");
        System.out.println("  " + user);
    }
 
    // ---------- Delete Own Account ----------
    private static void handleDeleteOwnAccount(Customer customer) {
        System.out.println("\n  --- Delete Account ---");
        System.out.println("  WARNING: This will permanently delete your account.");
        System.out.print("  Type YES to confirm: ");
        String confirm = scan.nextLine().trim();
 
        if (confirm.equals("YES")) {
            storage.deleteCustomer(customer.getCustomerID());
            System.out.println("  Your account has been deleted.");
            currentUser = null;
        } else {
            System.out.println("  Account deletion cancelled.");
        }
    }
 
    // ================================================================
    // STAFF MENU
    // ================================================================
 
    private static void showStaffMenu(Staff staff) {
        staff.displayMenu();
        int choice = readInt("  Select option: ", 0, 5);
 
        switch (choice) {
            case 1: viewProfile(staff);           break;
            case 2: handleUpdateStaffName(staff); break;
            case 3: handleViewAllUsers();         break;
            case 4: showReportMenu();             break;
            case 5: handleDeleteCustomer();       break;
            case 0:
                staff.logout();
                currentUser = null;
                break;
        }
    }
 
    // ---------- Update Staff Name ----------
    private static void handleUpdateStaffName(Staff staff) {
        System.out.println("\n  --- Update Name ---");
        try {
            System.out.print("  New full name: ");
            String newName = scan.nextLine().trim();
 
            if (newName.isEmpty())
                throw new IllegalArgumentException("Name cannot be empty.");
 
            staff.setStaffName(newName);
            storage.updateStaffName(staff.getStaffID(), newName);
            System.out.println("  Name updated to: " + newName);
 
        } catch (IllegalArgumentException e) {
            System.out.println("  Error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }
 
    // ---------- View All Users ----------
    private static void handleViewAllUsers() {
        storage.displayAllCustomers();
        storage.displayAllStaff();
    }
 
    // ---------- Delete a Customer ----------
    private static void handleDeleteCustomer() {
        System.out.println("\n  --- Delete Customer ---");
        storage.displayAllCustomers();
 
        if (storage.getAllCustomers().isEmpty()) return;
 
        try {
            System.out.print("  Enter Customer ID to delete: ");
            String id = scan.nextLine().trim();
 
            if (id.isEmpty())
                throw new IllegalArgumentException("Customer ID cannot be empty.");
 
            Customer target = storage.findCustomerByID(id);
            if (target == null)
                throw new IllegalArgumentException("Customer not found: " + id);
 
            System.out.println("  Found: " + target);
            System.out.print("  Type YES t`o confirm deletion: ");
            String confirm = scan.nextLine().trim();
 
            if (confirm.equals("YES")) {
                storage.deleteCustomer(id);
                System.out.println("  Customer deleted successfully.");
            } else {
                System.out.println("  Deletion cancelled.");
            }
 
        } catch (IllegalArgumentException e) {
            System.out.println("  Error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }
 
    // ================================================================
    // REPORT MENU
    // ================================================================
 
    private static void showReportMenu() {
        System.out.println("\n  --- Reports ---");
        System.out.println("  [1] Customer Summary");
        System.out.println("  [2] Staff Summary");
        System.out.println("  [3] Top Reward Points");
        System.out.println("  [4] System Overview");
        System.out.println("  [5] Booking Summary");
        System.out.println("  [6] Peak Hours");
        System.out.println("  [0] Back");
 
        int choice = readInt("  Select report: ", 0, 6);
        if (choice == 0) return;
 
        String reportID = "RPT" + String.format("%03d", reportCounter++);
 
        try {
            switch (choice) {
                case 1: new Report(reportID, "CUSTOMER_SUMMARY").generateCustomerSummaryReport(storage); break;
                case 2: new Report(reportID, "STAFF_SUMMARY").generateStaffSummaryReport(storage);       break;
                case 3: new Report(reportID, "TOP_REWARDS").generateTopRewardReport(storage);            break;
                case 4: new Report(reportID, "SYSTEM_OVERVIEW").generateSystemOverviewReport(storage);   break;
                case 5: new Report(reportID, "BOOKING_SUMMARY").generateBookingSummaryReport(storage);   break;
                case 6: new Report(reportID, "PEAK_HOURS").generatePeakHourReport(storage);              break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  Report error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error generating report: " + e.getMessage());
        }
    }
 
    // ================================================================
    // HELPER - integer input with validation (no InputHelper class)
    // ================================================================
 
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scan.nextLine().trim());
                if (val < min || val > max)
                    throw new IllegalArgumentException(
                            "Please enter a number between " + min + " and " + max + ".");
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input - please enter a whole number.");
            } catch (IllegalArgumentException e) {
                System.out.println("  " + e.getMessage());
            }
        }
    }
 
    // ================================================================
    // BANNER
    // ================================================================
 
    private static void printBanner() {
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |   CINEMA TICKETING & CONCESSION SYSTEM   |");
        System.out.println("  |          User Management Module          |");
        System.out.println("  +------------------------------------------+");
    }
}
