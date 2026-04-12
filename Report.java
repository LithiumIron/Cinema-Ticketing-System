import java.util.*;

public class Report {
 
    private String reportID;
    private String reportType;
    private Date   reportDate;
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public Report(String reportID, String reportType) {
        this.reportID   = reportID;
        this.reportType = reportType;
        this.reportDate = new Date();
    }
 
    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------
    public String getReportID()   { return reportID; }
    public String getReportType() { return reportType; }
    public Date   getReportDate() { return reportDate; }
 
    // ================================================================
    // REPORT - Customer Summary
    // ================================================================
 
    public void generateCustomerSummaryReport(DataStorage storage) {
        if (storage == null)
            throw new IllegalArgumentException("DataStorage cannot be null.");
 
        List<Customer> customers = new ArrayList<>(storage.getAllCustomers());
        printHeader("CUSTOMER SUMMARY REPORT");
 
        if (customers.isEmpty()) {
            System.out.println("  No customers registered.");
            printFooter();
            return;
        }
 
        System.out.printf("  %-8s %-18s %-14s%n", "ID", "Username", "Reward Points");
        System.out.println("  " + "-".repeat(42));
 
        int totalPoints = 0;
        for (Customer c : customers) {
            System.out.printf("  %-8s %-18s %-14d%n",
                    c.getCustomerID(), c.getUsername(), c.getRewardPoint());
            totalPoints += c.getRewardPoint();
        }
 
        System.out.println("  " + "-".repeat(42));
        System.out.printf("  Total customers: %-5d Total points: %d%n",
                customers.size(), totalPoints);
        printFooter();
    }
 
    // ================================================================
    // REPORT - Staff Summary
    // ================================================================
 
    public void generateStaffSummaryReport(DataStorage storage) {
        if (storage == null)
            throw new IllegalArgumentException("DataStorage cannot be null.");
 
        List<Staff> staffList = new ArrayList<>(storage.getAllStaff());
        printHeader("STAFF SUMMARY REPORT");
 
        if (staffList.isEmpty()) {
            System.out.println("  No staff registered.");
            printFooter();
            return;
        }
 
        System.out.printf("  %-8s %-18s %-15s%n", "ID", "Username", "Staff Name");
        System.out.println("  " + "-".repeat(42));
 
        for (Staff s : staffList) {
            System.out.printf("  %-8s %-18s %-15s%n",
                    s.getStaffID(), s.getUsername(), s.getStaffName());
        }
 
        System.out.println("  " + "-".repeat(42));
        System.out.println("  Total staff: " + staffList.size());
        printFooter();
    }
    // ================================================================
    // REPORT - Booking Summary
    // ================================================================
    public void generateBookingSummaryReport(DataStorage storage) {
       List<Booking> bookings = new ArrayList<>(storage.getAllBookings());
       printHeader("BOOKING SUMMARY REPORT");

       System.out.println("  Total Bookings : " + bookings.size());

       // Count tickets across all bookings
       int totalTickets = 0;
       for (Booking b : bookings) {
         if (b.getTickets() != null)
            totalTickets += b.getTickets().length;
       }
       System.out.println("  Total Tickets  : " + totalTickets);
       printFooter();
   }
    
    
    // ================================================================
    // REPORT - Top Reward Points
    // ================================================================
 
    public void generateTopRewardReport(DataStorage storage) {
        if (storage == null)
            throw new IllegalArgumentException("DataStorage cannot be null.");
 
        List<Customer> customers = new ArrayList<>(storage.getAllCustomers());
        printHeader("TOP REWARD POINTS REPORT");
 
        if (customers.isEmpty()) {
            System.out.println("  No customers registered.");
            printFooter();
            return;
        }
 
        // Sort descending by reward points
        customers.sort((a, b) -> b.getRewardPoint() - a.getRewardPoint());
 
        System.out.printf("  %-5s %-18s %-14s%n", "Rank", "Username", "Reward Points");
        System.out.println("  " + "-".repeat(42));
 
        int rank = 1;
        for (Customer c : customers) {
            String bar = "?".repeat(Math.min(c.getRewardPoint(), 10));
            System.out.printf("  %-5d %-18s %-5d %s%n",
                    rank++, c.getUsername(), c.getRewardPoint(), bar);
        }
        printFooter();
    }
 
    // ================================================================
    // REPORT - Full System Overview
    // ================================================================
 
    public void generateSystemOverviewReport(DataStorage storage) {
        if (storage == null)
            throw new IllegalArgumentException("DataStorage cannot be null.");
 
        printHeader("SYSTEM OVERVIEW REPORT");
        System.out.println("  Total Customers : " + storage.getAllCustomers().size());
        System.out.println("  Total Staff     : " + storage.getAllStaff().size());
 
        int totalPoints = 0;
        for (Customer c : storage.getAllCustomers())
            totalPoints += c.getRewardPoint();
        System.out.println("  Total Reward Pts: " + totalPoints);
 
        printFooter();
    }
 
    // ================================================================
    // PRINT HELPERS
    // ================================================================
 
    private void printHeader(String title) {
        System.out.println("\n  +------------------------------------------+");
        System.out.printf ("  :  %-40s:%n", title);
        System.out.println("  :  Report ID : " + String.format("%-27s", reportID) + ":");
        System.out.println("  :  Generated : " + String.format("%-27s", reportDate) + ":");
        System.out.println("  :------------------------------------------:");
    }
 
    private void printFooter() {
        System.out.println("  +------------------------------------------+");
    }
 
    // ----------------------------------------------------------------
    // Polymorphism
    // ----------------------------------------------------------------
    public String toString() {
        return String.format("Report[ID=%s | Type=%-20s | Date=%s]",
                reportID, reportType, reportDate);
    }
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Report)) return false;
        return reportID.equals(((Report) obj).reportID);
    }
}
