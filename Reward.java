public abstract class Reward {
    private String rewardCode;
    private String rewardDesc;
    private String rewardType;
    private int redeemPts;
    private int rewardQty;
    private String rewardStatus;

    //no-arg constructor
    protected Reward(){
        this("","","",0,0);
    }

    // constructor with args
    protected Reward(String rewardCode, String rewardDesc, String rewardType, int redeemPts, int rewardQty){
        this.rewardCode = rewardCode;
        this.rewardDesc = rewardDesc;
        this.rewardType = rewardType;
        this.redeemPts = redeemPts;
        this.rewardQty = rewardQty;
        this.rewardStatus = "Listed";
    }

    //getter
    public String getRewardCode(){
        return rewardCode;
    }

    public String getRewardDesc(){
        return rewardDesc;
    }

    public String getRewardType(){
        return rewardType;
    }

    public int getRedeemPts(){
        return redeemPts;
    }

    public int getRewardQty(){
        return rewardQty;
    }

    public String getRewardStatus(){
        return rewardStatus;
    }

    //setter
    public void setRewardDesc(String rewardDesc){
        this.rewardDesc = rewardDesc;
    }

    public void setRedeemPts(int redeemPts){
        this.redeemPts = redeemPts;
    }

    public void setRewardQty(int rewardQty){
        this.rewardQty = rewardQty;
    }

    public void setRewardStatus(String rewardStatus){
        this.rewardStatus = rewardStatus;
    }

    //other method
    @Override
    public String toString(){
        return "Reward Code : " + rewardCode +
            "\nReward Description : " + rewardDesc +
            "\nRedeem Points : " + redeemPts +
            "\nReward Quantity" + rewardQty +
            "\nReward Status" + rewardStatus;
    }

    public static boolean isStringParamNotNullorEmpty(String str) {
        return str != null && !str.trim().isEmpty();    //for desc, status, cond
    }

    public static boolean isRedeemPtsValid(int redeemPts){
        return redeemPts > 0;
    }

    public static boolean isRewardQtyValid(int rewardQty){
        return rewardQty >= 0;
    }

    public static boolean isRewardStatusValid(String rewardStatus){
        return rewardStatus.equalsIgnoreCase("Listed") || rewardStatus.equalsIgnoreCase("Removed") 
            || rewardStatus.equalsIgnoreCase("Out of Stock");
    }

    public void updateOutOfStockStatus(){
        if (this.rewardQty == 0) {
            setRewardStatus("Out of Stock");
        }
    }
}
