public class Redemption {
    // data properties
    private String redemptionID;
    private Reward reward;
    private String redeemRewardStatus;
    private static int redeemCounter = 1;

    //no arg-con
    public Redemption(){
        this(null);
    }

    //cons with arg
    public Redemption(Reward reward) {
        this.redemptionID = generateRedeemID();
        this.reward = reward;
        this.redeemRewardStatus = "Unused";
        redeemCounter++;
    }

    //getter
    public String getRedemptionID(){
        return redemptionID;
    }

    public Reward getReward() {
        return reward;
    }

    public String getRedeemRewardStatus() {
        return redeemRewardStatus;
    }
    
    //setter
    public void setRedeemRewardStatus(String redeemRewardStatus) {
        this.redeemRewardStatus = redeemRewardStatus;
    }

    //other methods
    public String toString() {
        return reward.toString() + "\nStatus: " + redeemRewardStatus;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Redemption)) return false;
        return this.redemptionID.equals(((Redemption) obj).redemptionID);
    }

    public String generateRedeemID(){
        return String.format("RD%03d", redeemCounter);
    }

    public int calculateTotalRedeemPts(int redeemQty){
        int totalRedeemPts = this.reward.getRedeemPts() * redeemQty;
        return totalRedeemPts;
    }
}
