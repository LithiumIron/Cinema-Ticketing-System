import java.io.*;
import java.util.*;

/**
 * Hall class represents a cinema hall.
 * Manages hall data (ID, type) and provides CRUD operations with file persistence.
 * 
 * ID Generation: uses a persistent counter (hall_counter.txt) to ensure 
 * unique IDs even after halls are deleted.
 */
public class Hall {
    
    // ==================== CONSTANTS & STATIC FIELDS ====================
    private static final String FILE_NAME = "halls.txt";//store hall data permanently
    private static final String COUNTER_FILE = "hall_counter.txt";//store next available hall id number
    private static List<Hall> hallList = new ArrayList<>();
    private static int nextIdNumber = 1;   // next available ID number (H001 -> 1)

    // ==================== Data fields====================
    private String hallId;
    private String type; // Small / Medium / Large

    // ==================== Constructors ====================

    //no agr cons
    public Hall() {
        this("");
    }
    //cons used when loading data from file
    public Hall(String hallId, String type) {
        this.hallId = hallId;
        this.type = type;
    }
    //cons used when creating new hall (auto-generated id)
    public Hall(String type) {
        this.hallId = generateNextId();
        this.type = type;
    }

    // ==================== Getter ====================
    public String getHallId() { return hallId; }
    public String getType()   { return type; }

    // ==================== Setter ====================
    public void setType(String type) { this.type = type; }

    // ==================== to string ====================
    public String toString() {
        return "ID: " + getHallId() + " | Type: " + getType();
    }

    // ==================== equals ====================
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Hall)) return false;
        Hall other = (Hall) obj;
        return this.hallId != null && this.hallId.equalsIgnoreCase(other.hallId);
    }
    

    //generate next hall id
    private static String generateNextId() {
        String id = String.format("H%03d", nextIdNumber);
        nextIdNumber++;          // increment for next hall
        saveCounter();           // immediately save the new counter value
        return id;
    }

    //save counter value into file
    private static void saveCounter() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            bw.write(String.valueOf(nextIdNumber));//convert num to string
        } catch (IOException e) {
            System.out.println("Error saving hall counter.");
        }
    }

    //load counter from file
    private static void loadCounter() {
        File counterFile = new File(COUNTER_FILE);
        if (counterFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(counterFile))) {
                String line = br.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    nextIdNumber = Integer.parseInt(line.trim());
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading hall counter.");
            }
        }

        // If counter file doesn't exist, calculate from existing hall ids
        int max = 0;
        for (Hall h : hallList) {
            int num = Integer.parseInt(h.getHallId().substring(1));
            if (num > max) max = num;
        }
        nextIdNumber = max + 1;
        saveCounter();   // create the counter file for future runs
    }

    // ==================== FILE OPERATIONS ====================

    public static void loadFromFile() {
        // First load halls
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");//split using "|"
                    if (parts.length == 2) {
                        hallList.add(new Hall(parts[0], parts[1]));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error loading halls.");
            }
        }

        // Then load/initialize counter (depends on hallList)
        loadCounter();
    }

    private static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Hall h : hallList) {
                bw.write(h.getHallId() + "|" + h.getType());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving halls.");
        }
        // counter is saved in generateNextId, but we also save it here for safety
        saveCounter();
    }

    // ==================== METHODS ====================

    //---add hall by type---
    public static void addHallByType(int typeChoice) {
        String type = "";
        switch (typeChoice) {
            case 1: type = "Small";break;
            case 2: type = "Medium";break;
            case 3: type = "Large";break;
        };

        Hall h = new Hall(type);
        hallList.add(h);
        saveToFile();

        System.out.println("\nHall added successfully!");
        System.out.println("ID: " + h.getHallId());
        System.out.println("Type: " + h.getType());
    }

    //---find hall by id---
    private static Hall findHallById(String id) {
        for (Hall h : hallList) {
            if (h.getHallId().equalsIgnoreCase(id)) return h;
        }
        return null;
    }

    //---delete hall prompt---
    public static void deleteHallPrompt(Scanner scan) {
        displayAllHalls();

        System.out.print("Enter Hall ID to delete: ");
        String id = scan.nextLine().trim();

        Hall h = findHallById(id);
        if (h == null) {
            System.out.println("Hall ID not found!");
            Utils.pause();
            return;
        }

        System.out.print("Are you sure? (y/n): ");
        String confirm = scan.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            hallList.remove(h);
            saveToFile();
            System.out.println("Hall deleted successfully!");
            // Counter is NOT decreased, so IDs remain unique forever.
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================== DISPLAY METHODS ====================

    //---display hall summary---
    public static void displayHallSummary(Scanner scan) {
        int small = 0, medium = 0, large = 0;

        for (Hall h : hallList) {
            switch (h.getType()) {
                case "Small": small++;break;
                case "Medium": medium++;break;
                case "Large": large++;break;
            }
        }

        System.out.println("\n========= HALL SUMMARY =========");
        System.out.println("Small Hall  : " + small);
        System.out.println("Medium Hall : " + medium);
        System.out.println("Large Hall  : " + large);
        System.out.println("-------------------------------");
        System.out.println("Total Number of Halls: " + hallList.size());
        System.out.println("================================");

         Utils.pause();
    }

    //---display all halls---
    private static void displayAllHalls() {
        System.out.println("\n===== ALL HALLS =====");
        for (Hall h : hallList) {
            System.out.println("ID: " + h.getHallId() + " | Type: " + h.getType());
        }
        System.out.println("=====================\n");
    }

    //---display All Hall Details---
    public static void displayAllHallsDetailed(Scanner scan) {
        Utils.clearScreen(80);

        System.out.println("------------------------------------------");
        System.out.println("|             HALL DETAILS               |");
        System.out.println("----------------------+-------------------");
        System.out.println("|       Hall ID       |       Type       |");
        System.out.println("----------------------+-------------------");

        for (Hall h : hallList) {
            System.out.printf("|        %-5s        |      %-7s     |\n", h.getHallId(), h.getType());
            System.out.println("|---------------------+------------------|");
        }

        System.out.println("\nTotal halls: " + hallList.size());

         Utils.pause();
    }

    // ==================== SEAT MAP DISPLAY ====================
    public static void displaySeatMapMenu(Scanner scan) {
        while (true) {
            Utils.clearScreen(80);

            System.out.println("======= VIEW HALL SEAT MAP =======");
            System.out.println("1. Small Hall");
            System.out.println("2. Medium Hall");
            System.out.println("3. Large Hall");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            int c = Utils.getIntRange(scan, 0, 3);

            if (c == 0) return;

            Utils.clearScreen(80);

            switch (c) {
                case 1: printSmallHall();break;
                case 2: printMediumHall();break;
                case 3: printLargeHall();break;
            }

             Utils.pause();
        }
    }

    private static void printSmallHall() {
        System.out.println("        Small Hall");
        System.out.println("      ---------------");
        System.out.println("   __/    SCREEN     \\__");
        System.out.println("");
        System.out.println("D| o o o o o  o o o o o o ");
        System.out.println("C| o o o o o  o o o o o o ");
        System.out.println("B| o o o o o  o o o o o o ");
        System.out.println("A| o o o o o  o o o o o o ");
        System.out.println("--------------------------");
        System.out.println("   1 2 3 4 5  6 7 8 9 a b");
        System.out.println("\nHall Capacity: 44 seats");
    }

    private static void printMediumHall() {
        System.out.println("            Medium Hall");
        System.out.println("      -------------------------");
        System.out.println("  ___/         SCREEN         \\___");
        System.out.println("");
        System.out.println("E| o o o o o  o o o o o o  o o o o ");
        System.out.println("D| o o o o o  o o o o o o  o o o o ");
        System.out.println("C| o o o o o  o o o o o o  o o o o ");
        System.out.println("B| o o o o o  o o o o o o  o o o o ");
        System.out.println("A| o o o o o  o o o o o o  o o o o ");
        System.out.println("------------------------------------");
        System.out.println("   1 2 3 4 5  6 7 8 9 a b  c d e f");
        System.out.println("\nHall Capacity: 75 seats");
    }

    private static void printLargeHall() {
        System.out.println("            Large Hall");
        System.out.println("        -----------------");
        System.out.println("   ____/      SCREEN     \\____");
        System.out.println("");
        System.out.println("K| o o o  o o o o o o o o  o o ");
        System.out.println("J| o o o  o o o o o o o o  o o ");
        System.out.println("I| o o o  o o o o o o o o  o o ");
        System.out.println("H| o o o  o o o o o o o o  o o ");
        System.out.println("G| o o o  o o o o o o o o  o o ");
        System.out.println("F| o o o  o o o o o o o o  o o ");
        System.out.println("E| o o o  o o o o o o o o  o o ");
        System.out.println("D| o o o  o o o o o o o o  o o ");
        System.out.println("C| o o o  o o o o o o o o  o o ");
        System.out.println("B| o o o  o o o o o o o o  o o ");
        System.out.println("A| o o o  o o o o o o o o  o o ");
        System.out.println("---------------------------------");
        System.out.println("   1 2 3  4 5 6 7 8 9 a b  c d");
        System.out.println("\nHall Capacity: 143 seats");
    }


        //=========== customer Section ===========

                                                                                                                                                                                                    
    private int getSeatHallNumber() {
        switch (type) {
            case "Small": return Seat.SMALL_HALL;
            case "Medium": return Seat.MEDIUM_HALL;
            case "Large": return Seat.LARGE_HALL;
            default: return 1;
        }
}

// Display seat map for this hall
    public void displaySeatMap() {
        int hallNo = getSeatHallNumber();
        Utils.clearScreen(80);
        switch(hallNo) {
            case Seat.SMALL_HALL: 
                System.out.println("\n        " + getType() + " Hall");
                System.out.println("      ---------------");
                System.out.println("   __/    SCREEN     \\__");
                System.out.println("");
                break;
            case Seat.MEDIUM_HALL:
                System.out.println("            Medium Hall");
                System.out.println("      -------------------------");
                System.out.println("  ___/         SCREEN         \\___");
                System.out.println("");
                break;
            case Seat.LARGE_HALL:
                System.out.println("            Large Hall");
                System.out.println("        -----------------");
                System.out.println("   ____/      SCREEN     \\____");
                System.out.println("");
                break;
        }
        
        Seat.displaySeatMap(hallNo);
    }
    
    // Book a seat 
    public void bookSeat(String seatInput) {
        
        int hallNo = getSeatHallNumber();
        Seat.bookSeat(hallNo, seatInput);  
        
    }
    
    // Cancel a booking in this hall
    public void cancelBooking(String seatInput) {
        int hallNo = getSeatHallNumber();
        Seat.cancelBooking(hallNo, seatInput);
    }

}
