import java.io.*;
import java.util.*;

public class Seat {

    // Hall no
    public static final int SMALL_HALL = 1;
    public static final int MEDIUM_HALL = 2;
    public static final int LARGE_HALL = 3;
    
    // Hall's rows and columns number
    private static final int SMALL_ROWS = 4;
    private static final int SMALL_COLS = 11;
    private static final int MEDIUM_ROWS = 5;
    private static final int MEDIUM_COLS = 15;
    private static final int LARGE_ROWS = 11;
    private static final int LARGE_COLS = 14;
    
    // Seat storage: Map hall no to seat array
    private static Map<Integer, Seat[][]> hallSeats = new HashMap<>();

    private int row;
    private int col;
    private int seatNo;
    private boolean isBooked;
    
    public Seat(){
        this(0,0);
    }
    
    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
        this.seatNo = (row + 1) * 10 + (col + 1);
        this.isBooked = false;
    }
    
    // Getter
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getSeatNo() { return seatNo; }
    public boolean isBooked() { return isBooked; }
    
    // Setter
    private static int getRow(int hallNo) {
        switch (hallNo) {
            case SMALL_HALL: return SMALL_ROWS;
            case MEDIUM_HALL: return MEDIUM_ROWS;
            case LARGE_HALL: return LARGE_ROWS;
            default: return 0;
        }
    }
    
    private static int getCol(int hallNo) {
        switch (hallNo) {
            case SMALL_HALL: return SMALL_COLS;
            case MEDIUM_HALL: return MEDIUM_COLS;
            case LARGE_HALL: return LARGE_COLS;
            default: return 0;
        }
    }
    public void setSeatNo(int seatNo) { this.seatNo = seatNo; }
    public void setBooked(boolean booked) { this.isBooked = booked; }
    
    // other methods
    public static void initializeAllHalls() {
        // Small Hall
        Seat[][] smallSeats = new Seat[SMALL_ROWS][SMALL_COLS];
        for (int i = 0; i < SMALL_ROWS; i++) {
            for (int j = 0; j < SMALL_COLS; j++) {
                smallSeats[i][j] = new Seat(i, j);
            }
        }
        hallSeats.put(SMALL_HALL, smallSeats);
        
        // Medium Hall
        Seat[][] mediumSeats = new Seat[MEDIUM_ROWS][MEDIUM_COLS];
        for (int i = 0; i < MEDIUM_ROWS; i++) {
            for (int j = 0; j < MEDIUM_COLS; j++) {
                mediumSeats[i][j] = new Seat(i, j);
            }
        }
        hallSeats.put(MEDIUM_HALL, mediumSeats);
        
        // Large Hall
        Seat[][] largeSeats = new Seat[LARGE_ROWS][LARGE_COLS];
        for (int i = 0; i < LARGE_ROWS; i++) {
            for (int j = 0; j < LARGE_COLS; j++) {
                largeSeats[i][j] = new Seat(i, j);
            }
        }
        hallSeats.put(LARGE_HALL, largeSeats);
        
        // Load existing bookings
        loadFromFile();
    }
    

    
    // ========== DISPLAY SEAT MAP ==========
    public static void displaySeatMap(int hallNo) {
        Seat[][] seats = hallSeats.get(hallNo);
        int rows = getRow(hallNo);
        int cols = getCol(hallNo);

        // Print rows from top to bottom
        for (int i = rows - 1; i >= 0; i--) {
            System.out.print((char)('A' + i) + "| ");
            
            for (int j = 0; j < cols; j++) {
                if (seats[i][j].isBooked) {
                    System.out.print("X ");
                } else {
                    System.out.print("o ");
                }
                
                // Add spaces based on hall type (refer to seat layout)
                if (hallNo == SMALL_HALL && j == 4) {
                    System.out.print(" ");
                } else if (hallNo == MEDIUM_HALL && (j == 4 || j == 9)) {
                    System.out.print(" ");
                } else if (hallNo == LARGE_HALL && (j == 2 || j == 7 || j == 12)) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        
        System.out.println("-".repeat(cols * 2 + 10));
        
        // Print column numbers
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print(getColDisplay(j + 1) + " ");
            if (hallNo == SMALL_HALL && j == 4) {
                System.out.print(" ");
            } else if (hallNo == MEDIUM_HALL && (j == 4 || j == 9)) {
                System.out.print(" ");
            } else if (hallNo == LARGE_HALL && (j == 2 || j == 7 || j == 12)) {
                System.out.print(" ");
            }
        }
        System.out.println();
        
        System.out.println("\nCapacity: " + (rows * cols) + " seats");
        System.out.println("Available: " + getAvailableCount(hallNo) + " seats");
        System.out.println("Booked: " + getBookedCount(hallNo) + " seats");
        System.out.println("Seat Slot: o = Available, X = Booked");
    }
    
    // ========== BOOK A SEAT ==========
    public static boolean bookSeat(int hallNo, String seatInput) {
        Seat[][] seats = hallSeats.get(hallNo);
        int rows = getRow(hallNo);
        int cols = getCol(hallNo);
        
        if (seats == null) {
            System.out.println("\nHall not found!");
            Utils.pause();
            return false;
        }
        //validate blank input
        if(seatInput.trim().isEmpty()){
            System.out.println("Invalid input! Please enter a seat number (e.g. A5)");
            Utils.pause();
            return false;
        }
        
        //combine seat input (e.g., C5 or Da)
        char rowChar = seatInput.charAt(0);
        String colDisplay = seatInput.substring(1);
        
        int rowIndex = getRowIndex(rowChar);
        int colIndex = getColIndex(colDisplay, hallNo);

  
        if (rowIndex >= 0 && rowIndex < rows && colIndex >= 0 && colIndex < cols) {
            Seat seat = seats[rowIndex][colIndex]; 
            
            if (seat.isBooked()) {
                System.out.println("\nSeat " + seatInput + " is already booked!");
                Utils.pause();
                return false;

            } else {
                seat.setBooked(true);
                System.out.println("\nSeat " + seatInput + " booked successfully!");
                saveToFile();
                Utils.pause();
                return true;
            }
        } else 
            System.out.println("\nInvalid seat! Please enter a valid seat number (e.g. A5)");
            Utils.pause();
            return false;
        
        
        

    }
    
    // ========== CANCEL BOOKING ==========
    public static boolean cancelBooking(int hallNo, String seatInput) {
        Seat[][] seats = hallSeats.get(hallNo);
        int rows = getRow(hallNo);
        int cols = getCol(hallNo);
        
        if (seats == null) {
             
            System.out.println("Hall not found!");
            Utils.pause();
            return false;
        }

        //validate blank input
        if(seatInput.trim().isEmpty()){
            System.out.println("Invalid input! Please enter a seat number (e.g. A5)");
            Utils.pause();
            return false;
        }

        char rowChar = seatInput.charAt(0);
        String colDisplay = seatInput.substring(1);
        
        int rowIndex = getRowIndex(rowChar);
        int colIndex = getColIndex(colDisplay, hallNo);

        if (rowIndex >= 0 && rowIndex < rows && colIndex >= 0 && colIndex < cols) {
            Seat seat = seats[rowIndex][colIndex]; 
            
            if (!seat.isBooked()) {
                System.out.println("\nSeat " + seatInput + " is not booked!");
            } else {
                seat.setBooked(false);
                System.out.println("\nSeat " + seatInput + " cancelled successfully!");
                saveToFile();
            }
        } 
        else 
            System.out.println("\nInvalid seat! Please enter a valid seat number (e.g. A5)");
        
        
        Utils.pause();
        return true;
    }
    
    // ========== GET AVAILABLE/BOOKED COUNTS ==========
    private static int getAvailableCount(int hallNo) {
        Seat[][] seats = hallSeats.get(hallNo);
        int rows = getRow(hallNo);
        int cols = getCol(hallNo);
        
        if (seats == null) return 0;
        
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!seats[i][j].isBooked) count++;
            }
        }
        return count;
    }
    
    private static int getBookedCount(int hallNo) {
        return (getRow(hallNo) * getCol(hallNo)) - getAvailableCount(hallNo);
    }
    
    //======================================================

    private static int getRowIndex(char rowChar) {
        switch (rowChar) {
            case 'A': return 0;
            case 'B': return 1;
            case 'C': return 2;
            case 'D': return 3;
            case 'E': return 4;
            case 'F': return 5;
            case 'G': return 6;
            case 'H': return 7;
            case 'I': return 8;
            case 'J': return 9;
            case 'K': return 10;
            default: return -1;
        }
    }
    
    private static int getColIndex(String colDisplay, int hallNo) {
        int maxCol = getCol(hallNo);
        
        try {
            int num = Integer.parseInt(colDisplay);
            if (num >= 1 && num <= maxCol) return num - 1;
        } catch (NumberFormatException e) {
            switch (colDisplay.toLowerCase()) {
                case "a": return 9;
                case "b": return 10;
                case "c": return 11;
                case "d": return 12;
                case "e": return 13;
                case "f": return 14;
                case "g": return 15;
            }
        }
        return -1;
    }
    
    private static String getColDisplay(int col) {
        if (col <= 9) return String.valueOf(col);
        if (col == 10) return "a";
        if (col == 11) return "b";
        if (col == 12) return "c";
        if (col == 13) return "d";
        if (col == 14) return "e";
        if (col == 15) return "f";
        return "";
    }

    // ========== FILE STORAGE ==========
    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("seats_data.txt"))) {
            for (Map.Entry<Integer, Seat[][]> entry : hallSeats.entrySet()) {
                int hallNo = entry.getKey();
                Seat[][] seats = entry.getValue();
                int rows = getRow(hallNo);
                int cols = getCol(hallNo);
                
                writer.println("HALL:" + hallNo);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        writer.println("SEAT:" + i + "," + j + "," + seats[i][j].isBooked);
                    }
                }
                writer.println("END_HALL");
            }
        } catch (IOException e) {
            System.out.println("Error saving seat data!");
        }
    }
    
    private static void loadFromFile() {
        File file = new File("seats_data.txt");
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Integer currentHall = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HALL:")) {
                    currentHall = Integer.parseInt(line.substring(5));
                } else if (line.startsWith("SEAT:") && currentHall != null) {
                    String[] parts = line.substring(5).split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    boolean booked = Boolean.parseBoolean(parts[2]);
                    
                    Seat[][] seats = hallSeats.get(currentHall);
                    if (seats != null && row < seats.length && col < seats[0].length) {
                        seats[row][col].setBooked(booked);
                    }
                }
            }
            System.out.println("Seat data is loaded!");
        } catch (IOException e) {
            System.out.println("Error loading seat data.");
        }
    }


    public String toString() {
        char rowLetter = (char)('A' + row);
        String status = isBooked ? "BOOKED" : "AVAILABLE";
        return String.format("Seat[%s%d | No:%d | %s]", 
                             rowLetter, (col + 1), seatNo, status);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Seat){
            Seat seat = (Seat) obj;
            return this.row == seat.row && this.col == seat.col;
        }
        else
            return false;
    }

}
