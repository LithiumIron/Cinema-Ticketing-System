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
        int customerPts = customer.getRewardPts();
        int totalRedeemPts = calculateTotalRedeemPts(redeemQty);
        int latestPts = customerPts - totalRedeemPts;
        customer.setRewardPts(latestPts);

        List<Redemption> customerReward = customer.getRedemptions();
        customerReward.add(this);

        int previousQty = reward.getRewardQty();
        reward.setRewardQty(previousQty - redeemQty);
        reward.updateOutOfStockStatus();
    }
}
