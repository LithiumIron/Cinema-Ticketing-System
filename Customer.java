import java.util.List;
import java.util.ArrayList;

 class Customer extends User {
 
    private String customerID;
    private int    rewardPts;
    //private final List<Redemption> redemptions;
 
    // ----------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------
    
    public Customer(){
    	   this(" ", " ", " ");
    }
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

    /**
     * Check if this customer has a redemption for the given reward code
     * whose status is "Unused".
     */
   /* public boolean selectReward(String rewardCode) {
        if (rewardCode == null) {
            return false;
        }

        for (Redemption redemption : redemptions) {
            Reward reward = redemption.getReward();
            if (reward == null) {
                continue;
            }

            boolean sameCode = rewardCode.equalsIgnoreCase(reward.getRewardID());
            boolean unused = "Unused".equalsIgnoreCase(redemption.getRedeemRewardStatus());

            if (sameCode && unused) {
                return true;
            }
        }

        return false;
    }*/

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        return customerID.equals(((Customer) obj).customerID);
    }
}
