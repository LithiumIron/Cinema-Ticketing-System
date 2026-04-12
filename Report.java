import java.io.*;
import java.util.*;
 
/*
 DataStorage - persistent TXT-based storage for Customers and Staff.
 File: data/users.txt
 Format per line: ROLE|userID|username|password|extraField
 CUSTOMER|C001|alice|pass123|10        (extraField = rewardPoints)
 STAFF|S001|sarah|staff123|Sarah Lim   (extraField = staffName
 */
public class DataStorage {
 
    private static final String FILE_PATH = "data/users.txt";
    private static final String BOOKING_FILE = "data/bookings.txt";
    
    private List<Customer> customers;
    private List<Staff>    staffList;
    private List<Booking> bookings = new ArrayList<>();
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public DataStorage() {
        this.customers = new ArrayList<>();
        this.staffList = new ArrayList<>();
        ensureFileExists();
    }
 
    // ----------------------------------------------------------------
    // SETUP
    // ----------------------------------------------------------------
    private void ensureFileExists() {
        File dir  = new File("data");
        File file = new File(FILE_PATH);
        try {
            if (!dir.exists())  dir.mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            System.out.println("  [Storage] Warning: could not create data file - " + e.getMessage());
        }
    }
 
    // ================================================================
    // CREATE
    // ================================================================
 
    public boolean addCustomer(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException("Customer cannot be null.");
        if (findCustomerByID(customer.getCustomerID()) != null) {
            System.out.println("  [Storage] Customer ID already exists: " + customer.getCustomerID());
            return false;
        }
        if (findByUsername(customer.getUsername()) != null) {
            System.out.println("  [Storage] Username already taken: " + customer.getUsername());
            return false;
        }
        customers.add(customer);
        saveAll();
        return true;
    }
 
    public boolean addStaff(Staff staff) {
        if (staff == null)
            throw new IllegalArgumentException("Staff cannot be null.");
        if (findStaffByID(staff.getStaffID()) != null) {
            System.out.println("  [Storage] Staff ID already exists: " + staff.getStaffID());
            return false;
        }
        if (findByUsername(staff.getUsername()) != null) {
            System.out.println("  [Storage] Username already taken: " + staff.getUsername());
            return false;
        }
        staffList.add(staff);
        saveAll();
        return true;
    }
    
    public boolean addBooking(String customerID, Booking booking) {
      if (booking == null)
        throw new IllegalArgumentException("Booking cannot be null.");
      bookings.add(booking);
      saveAllBookings(customerID, booking);
      return true;
}
 
    // ================================================================
    // READ
    // ================================================================
 
    public Customer findCustomerByID(String customerID) {
        for (Customer c : customers)
            if (c.getCustomerID().equals(customerID)) return c;
        return null;
    }
 
    public Customer findCustomerByUsername(String username) {
        for (Customer c : customers)
            if (c.getUsername().equalsIgnoreCase(username)) return c;
        return null;
    }
 
    public Staff findStaffByID(String staffID) {
        for (Staff s : staffList)
            if (s.getStaffID().equals(staffID)) return s;
        return null;
    }
 
    public Staff findStaffByUsername(String username) {
        for (Staff s : staffList)
            if (s.getUsername().equalsIgnoreCase(username)) return s;
        return null;
    }
    
    // Unified lookup across both lists (for login)
    public User findByUsername(String username) {
        User u = findCustomerByUsername(username);
        if (u != null) return u;
        return findStaffByUsername(username);
    }
 
    public User loginLookup(String username, String password) {
        for (Customer c : customers)
            if (c.login(username, password)) return c;
        for (Staff s : staffList)
            if (s.login(username, password)) return s;
        return null;
    }
 
    public List<Customer> getAllCustomers() {
        return Collections.unmodifiableList(customers);
    }
 
    public List<Staff> getAllStaff() {
        return Collections.unmodifiableList(staffList);
    }
    
    // BOOKING - READ
    public List<Booking> getAllBookings() {
        return Collections.unmodifiableList(bookings);
    }
    // BOOKING - READ by customer (match by customerID prefix in file)
    public List<Booking> getBookingsByCustomer(String customerID) {
        List<Booking> result = new ArrayList<>();
        File file = new File(BOOKING_FILE);
        if (!file.exists()) return result;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = br.readLine()) != null) {
              String[] parts = line.split("\\|", -1);
              if (parts.length >= 1 && parts[0].trim().equals(customerID))
                  result.add(bookings.get(bookings.size() - 1)); // placeholder
          }
      } catch (IOException e) {
        System.out.println("  [Storage] Error reading bookings: " + e.getMessage());
      }
      return result;
    }
    // ================================================================
    // UPDATE
    // ================================================================
 
    public boolean updateCustomerPoints(String customerID, int newPoints) {
        Customer c = findCustomerByID(customerID);
        if (c == null) {
            System.out.println("  [Storage] Customer not found: " + customerID);
            return false;
        }
        c.setRewardPoint(newPoints);
        saveAll();
        return true;
    }
 
    public boolean updateStaffName(String staffID, String newName) {
        Staff s = findStaffByID(staffID);
        if (s == null) {
            System.out.println("  [Storage] Staff not found: " + staffID);
            return false;
        }
        s.setStaffName(newName);
        saveAll();
        return true;
    }
 
    // ================================================================
    // DELETE
    // ================================================================
 
    public boolean deleteCustomer(String customerID) {
        Customer c = findCustomerByID(customerID);
        if (c == null) {
            System.out.println("  [Storage] Customer not found: " + customerID);
            return false;
        }
        customers.remove(c);
        saveAll();
        return true;
    }
 
    public boolean deleteStaff(String staffID) {
        Staff s = findStaffByID(staffID);
        if (s == null) {
            System.out.println("  [Storage] Staff not found: " + staffID);
            return false;
        }
        staffList.remove(s);
        saveAll();
        return true;
    }
 
    // ================================================================
    // FILE I/O - SAVE (write all records to TXT)
    // ================================================================
 
    private void saveAll() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Customer c : customers) {
                // Format: CUSTOMER|customerID|username|password|rewardPoints
                bw.write("CUSTOMER|" + c.getCustomerID()  + "|"
                                     + c.getUsername()    + "|"
                                     + c.getPassword()    + "|"   
                                     + c.getRewardPoint());
                bw.newLine();
            }
            for (Staff s : staffList) {
                // Format: STAFF|staffID|username|password|staffName
                bw.write("STAFF|"    + s.getStaffID()    + "|"
                                     + s.getUsername()   + "|"
                                     + s.getPassword()   + "|"
                                     + s.getStaffName());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("  [Storage] Error saving data: " + e.getMessage());
        }
    }
    private void saveAllBookings(String customerID, Booking booking) {
      try (BufferedWriter bw = new BufferedWriter(
              new FileWriter(BOOKING_FILE, true))) {  // true = append mode
          // Format: customerID|showId|bookingDate|bookingTime|ticketCount
          String showId    = booking.getShow() != null ? booking.getShow().getShowId() : "N/A";
          String movieTitle= booking.getShow() != null ? booking.getShow().getMovie().getTitle() : "N/A";
          String showDate  = booking.getShow() != null ? booking.getShow().getStartDate() : "N/A";
          String showTime  = booking.getShow() != null ? booking.getShow().getStartTime() : "N/A";
          int    ticketCnt = booking.getTickets() != null ? booking.getTickets().length : 0;

          bw.write(customerID  + "|" +
                   showId      + "|" +
                   movieTitle  + "|" +
                   showDate    + "|" +
                  showTime    + "|" +
                  ticketCnt);
          bw.newLine();
      } catch (IOException e) {
          System.out.println("  [Storage] Error saving booking: " + e.getMessage());
      }
   }

    // ================================================================
    // FILE I/O - LOAD (read TXT on startup)
    // ================================================================
 
    public void loadAll() {
        customers.clear();
        staffList.clear();
 
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) return;
 
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;
 
                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) {
                    System.out.println("  [Storage] Skipping malformed line " + lineNum);
                    continue;
                }
 
                String role      = parts[0].trim();
                String id        = parts[1].trim();
                String username  = parts[2].trim();
                String password  = parts[3].trim();
                String extra     = parts[4].trim();
 
                if (role.equals("CUSTOMER")) {
                    int pts = 0;
                    try { pts = Integer.parseInt(extra); }
                    catch (NumberFormatException ignored) {}
                    Customer c = new Customer(id, username, password);
                    c.setRewardPoint(pts);
                    customers.add(c);
 
                } else if (role.equals("STAFF")) {
                    Staff s = new Staff(id, extra, username, password);
                    staffList.add(s);
                }
            }
            System.out.println("  [Storage] Loaded "
                + customers.size() + " customer(s), "
                + staffList.size()  + " staff member(s) from file.");
 
        } catch (IOException e) {
            System.out.println("  [Storage] Error loading data: " + e.getMessage());
        }
        loadAllBookings();
    }
    
    private void loadAllBookings() {
       File file = new File(BOOKING_FILE);
       if (!file.exists() || file.length() == 0) return;
       try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = br.readLine()) != null) {
             line = line.trim();
             if (!line.isEmpty())
                 bookings.add(new Booking()); // skeleton - full object lives in teammate's session
          }
       } catch (IOException e) {
        System.out.println("  [Storage] Error loading bookings: " + e.getMessage());
       }
    }
    // ================================================================
    // DISPLAY HELPERS
    // ================================================================
 
    public void displayAllCustomers() {
        System.out.println("\n  --- Registered Customers ---");
        if (customers.isEmpty()) {
            System.out.println("  (No customers found.)");
            return;
        }
        for (Customer c : customers)
            System.out.println("  " + c);
    }
 
    public void displayAllStaff() {
        System.out.println("\n  --- Registered Staff ---");
        if (staffList.isEmpty()) {
            System.out.println("  (No staff found.)");
            return;
        }
        for (Staff s : staffList)
            System.out.println("  " + s);
    }
 
    // ================================================================
    // ID GENERATION
    // ================================================================
 
    public String generateCustomerID() {
        return "C" + String.format("%03d", customers.size() + 1);
    }
 
    public String generateStaffID() {
        return "S" + String.format("%03d", staffList.size() + 1);
    }
}
 
