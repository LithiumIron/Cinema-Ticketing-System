import java.util.*;
import java.io.*;

public class Report {
 
    private String reportID;
    private String reportType;
    private Date   reportDate;
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public Report(){
    	   this(" ", " ");
    }
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
                    c.getCustomerID(), c.getUsername(), c.getRewardPts());
            totalPoints += c.getRewardPts();
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
        printHeader("BOOKING SUMMARY REPORT");

        File file = new File("data/bookings.txt");
        if (!file.exists()) {
            System.out.println("  No booking records found.");
            printFooter();
            return;
        }

        int totalBookings = 0;
        int totalTickets  = 0;
        double totalRevenue  = 0.0;
        Map<String, Integer> movieCount = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;

                // parts: customerID|movieTitle|showDate|showTime|hallId|ticketCount|foodTotal
                String movieTitle = parts[1].trim();
                int    tickets    = 0;
                double food       = 0.0;

                try { tickets = Integer.parseInt(parts[5].trim()); }
                catch (NumberFormatException ignored) {}
                try { food = Double.parseDouble(parts[6].trim()); }
                catch (NumberFormatException ignored) {}

                totalBookings++;
                totalTickets  += tickets;
                totalRevenue  += food;
                movieCount.put(movieTitle, movieCount.getOrDefault(movieTitle, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("  Error reading booking file: " + e.getMessage());
            printFooter();
            return;
        }

        System.out.printf("  %-20s : %d%n",  "Total Bookings",  totalBookings);
        System.out.printf("  %-20s : %d%n",  "Total Tickets",   totalTickets);
        System.out.printf("  %-20s : RM%.2f%n", "Total Food Revenue", totalRevenue);
        System.out.println();

        // Most booked movies
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(movieCount.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        System.out.printf("  %-25s %s%n", "Movie", "Bookings");
        System.out.println("  " + "-".repeat(35));
        for (Map.Entry<String, Integer> e : sorted)
            System.out.printf("  %-25s %d%n", e.getKey(), e.getValue());

        printFooter();
    }
    // ================================================================
    // REPORT - Peak Hour
    // ================================================================
    public void generatePeakHourReport(DataStorage storage) {
        printHeader("PEAK HOUR REPORT");

        File file = new File("data/bookings.txt");
        if (!file.exists()) {
            System.out.println("  No booking records found.");
            printFooter();
            return;
        }

        Map<String, Integer> hourCount = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length < 4) continue;

                String showTime = parts[3].trim(); // format HH:mm
                if (showTime.contains(":")) {
                    String hour = showTime.split(":")[0] + ":00";
                    hourCount.put(hour, hourCount.getOrDefault(hour, 0) + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("  Error reading booking file: " + e.getMessage());
            printFooter();
            return;
        }

        if (hourCount.isEmpty()) {
            System.out.println("  No showtime data available.");
            printFooter();
            return;
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(hourCount.entrySet());
        sorted.sort(Map.Entry.comparingByKey());
 
        // Find peak
        String peakHour  = sorted.get(0).getKey();
        int peakCount = 0;
        for (Map.Entry<String, Integer> e : sorted) {
            if (e.getValue() > peakCount) {
                peakCount = e.getValue();
                peakHour  = e.getKey();
            }
        }

        System.out.printf("  %-10s  %s%n", "Showtime", "Bookings");
        System.out.println("  " + "-".repeat(30));
        for (Map.Entry<String, Integer> e : sorted) {
            String marker = e.getKey().equals(peakHour) ? "  <- PEAK" : "";
            System.out.printf("  %-10s  %d%s%n", e.getKey(), e.getValue(), marker);
        }

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
        customers.sort((a, b) -> b.getRewardPts() - a.getRewardPts());
 
        System.out.printf("  %-5s %-18s %-14s%n", "Rank", "Username", "Reward Points");
        System.out.println("  " + "-".repeat(42));
 
        int rank = 1;
        for (Customer c : customers) {
            String bar = "?".repeat(Math.min(c.getRewardPts(), 10));
            System.out.printf("  %-5d %-18s %-5d %s%n",
                    rank++, c.getUsername(), c.getRewardPts(), bar);
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
            totalPoints += c.getRewardPts();
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
