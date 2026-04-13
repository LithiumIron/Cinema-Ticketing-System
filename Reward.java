public abstract class Reward {
    private String rewardID;
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
    protected Reward(String rewardID, String rewardDesc, String rewardType, int redeemPts, int rewardQty){
        this.rewardID = rewardID;
        this.rewardDesc = rewardDesc;
        this.rewardType = rewardType;
        this.redeemPts = redeemPts;
        this.rewardQty = rewardQty;
        this.rewardStatus = "Listed";
    }

    //getter
    public String getRewardID(){
        return rewardID;
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

    public void setRewardPts(int redeemPts){
        this.redeemPts = redeemPts;
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
        return "Reward ID : " + rewardID +
            "\nReward Description : " + rewardDesc +
            "\nReward Type : " + rewardType +
            "\nRedeem Points : " + redeemPts +
            "\nReward Quantity : " + rewardQty +
            "\nReward Status : " + rewardStatus;
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

    public void updateOutOfStockStatus(){
        if (this.rewardQty == 0) 
            setRewardStatus("Out of Stock");
    }
}
