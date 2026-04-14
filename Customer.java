import java.util.ArrayList;
import java.util.List;

 class Customer extends User {
 
    private String customerID;
    private int    rewardPts;
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    public Customer(String customerID, String username, String password) {
        super(customerID, username, password, "CUSTOMER");
        this.customerID  = customerID;
        this.rewardPts = 0;
    }
 
    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------
    public String getCustomerID()  { return customerID; }
    public int    getRewardPts() { return rewardPts; }
 
    // ----------------------------------------------------------------
    // Setters
    // ----------------------------------------------------------------
    public void setRewardPts(int RewardPts) {
        if (RewardPts < 0)
            throw new IllegalArgumentException("Reward points cannot be negative.");
        this.rewardPts = RewardPts;
    }
 
    // ----------------------------------------------------------------
    // Abstract implementations
    // ----------------------------------------------------------------
    public void displayMenu() {
        System.out.println("\n  ================================");
        System.out.println("  |       CUSTOMER MENU          |");
        System.out.println("  +===============================");
        System.out.println("  |  [1] View My Profile         |");
        System.out.println("  |  [2] Delete My Account       |");
        System.out.println("  |  [0] Logout                  |");
        System.out.println("  ================================");
    }
 
    public String getDisplayName() {
        return "Customer: " + getUsername() + " | Points: " + rewardPts;
    }
 
    // ----------------------------------------------------------------
    // Polymorphism
    // ----------------------------------------------------------------
    public String toString() {
        return String.format("Customer[ID=%-6s | Username=%-15s | RewardPoints=%d]",
                customerID, getUsername(), rewardPts);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        return customerID.equals(((Customer) obj).customerID);
    }
}
