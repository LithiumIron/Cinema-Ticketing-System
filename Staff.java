public class Staff extends User {
 
    private String staffID;
    private String staffName;
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public Staff(){
    	  this(" ", " ", " ", " ");
    }
    public Staff(String staffID, String staffName, String username, String password) {
        super(staffID, username, password, "STAFF");
        this.staffID   = staffID;
        this.staffName = staffName;
    }
 
    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------
    public String getStaffID()   { return staffID; }
    public String getStaffName() { return staffName; }
 
    // ----------------------------------------------------------------
    // Setters
    // ----------------------------------------------------------------
    public void setStaffName(String staffName) {
        if (staffName == null || staffName.isBlank())
            throw new IllegalArgumentException("Staff name cannot be empty.");
        this.staffName = staffName;
    }
 
    // ----------------------------------------------------------------
    // Abstract implementations
    // ----------------------------------------------------------------
    public void displayMenu() {
        System.out.println("\n  ===============================");
        System.out.println("  |         STAFF MENU           |");
        System.out.println("  +------------------------------+");
        System.out.println("  |  [1] View My Profile         |");
        System.out.println("  |  [2] Update My Name          |");
        System.out.println("  |  [3] View All Users          |");
        System.out.println("  |  [4] Generate Reports        |");
        System.out.println("  |  [5] Delete a Customer       |");
        System.out.println("  |  [6] Manage System           |");
        System.out.println("  |  [0] Logout                  |");
        System.out.println("  ================================");
    }
 
    public String getDisplayName() {
        return "Staff: " + staffName + " (" + getUsername() + ")";
    }
 
    // ----------------------------------------------------------------
    // Polymorphism
    // ----------------------------------------------------------------
    public String toString() {
        return String.format("Staff [ID=%-6s | Name=%-15s | Username=%s]",
                staffID, staffName, getUsername());
    }
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Staff)) return false;
        return staffID.equals(((Staff) obj).staffID);
    }
}
 
