public class Discount extends Reward{
    //data properties
    private double percentage;
    private static int sequence = 1;

    //no-arg constructor
    public Discount(){
        this("", 0, 0, 0.0);
    }

    //constructor with args
    public Discount(String rewardDesc, int redeemPts, int rewardQty, double percentage){
        super(generateNewCode(), rewardDesc, "Discount", redeemPts, rewardQty);
        this.percentage = percentage;
        sequence++;
    }

    //getter
    public double getPercentage(){
        return percentage;
    }

    public static int getSequence(){
        return sequence;
    }

    //setter
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }

    //other methods
    @Override
    public String toString(){
        return super.toString() +
            "\nDiscount Percentage : " + percentage + "%";
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Discount)) return false;
        return this.getRewardID().equals(((Discount) obj).getRewardID());
    }

    public static boolean isPercentageValid(double percentage){
        return percentage > 0 && percentage < 100;
    }

    private static String generateNewCode() {
        return String.format("D%03d", sequence);
    }
}
