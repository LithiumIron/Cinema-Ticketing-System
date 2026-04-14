import java.util.List;

public class Redemption {
    private Reward reward;
    private String redeemRewardStatus;

    public Redemption(){
        this(null);
    }

    public Redemption(Reward reward) {
        this.reward = reward;
        this.redeemRewardStatus = "Unused";
    }

    public Reward getReward() {
        return reward;
    }

    public String getRedeemRewardStatus() {
        return redeemRewardStatus;
    }
    
    public void setRedeemRewardStatus(String redeemRewardStatus) {
        this.redeemRewardStatus = redeemRewardStatus;
    }

    public String toString() {
        return reward.toString() + "\nStatus: " + redeemRewardStatus;
    }

    public boolean isRewardQtyEnough(int redeemQty){
        return redeemQty <= this.reward.getRewardQty();
    }

    public int calculateTotalRedeemPts(int redeemQty){
        int totalRedeemPts = this.reward.getRedeemPts() * redeemQty;
        return totalRedeemPts;
    }

    public boolean isRewardPtsEnough(int redeemQty, Customer customer){
        int totalRedeemPts = calculateTotalRedeemPts(redeemQty);
        return totalRedeemPts <= customer.getRewardPts();
    }

    public void redeemReward(Customer customer,int redeemQty){
        if (isRewardQtyEnough(redeemQty) && isRewardPtsEnough(redeemQty, customer)) {
            int totalRedeemPts = calculateTotalRedeemPts(redeemQty);
            customer.setRewardPts(customer.getRewardPts() - totalRedeemPts);
            this.reward.setRewardQty(this.reward.getRewardQty() - redeemQty);
        }
    }
}
