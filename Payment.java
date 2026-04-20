import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class Payment {
	
//data properties
private String paymentID;
private Booking booking;
private double subTotal;
private double tax;
private double discountAmt;
private double totalAmount;
private String paymentDate;

private static int counter = 1;
private static final double TAX_RATE = 0.08;// 8% tax


//-------------------------- constructors -------------------
public Payment(){
	this.paymentID = String.format("P%05d", counter++);
	this.booking = new Booking();
	this.paymentDate = "";
	this.subTotal = 0.0;
	this.tax = 0.0;
	this.discountAmt = 0.0;
	this.totalAmount = 0.0;
}

public Payment(Booking booking, double subTotal, double tax, double discountAmt){
    this.paymentID = String.format("P%05d", counter++);
    this.booking = booking;
    this.paymentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    this.subTotal = subTotal;
    this.tax = tax;
    this.discountAmt = discountAmt;
    this.totalAmount = subTotal + tax - discountAmt;
} 
//------------------------ getters -----------------------
public String getPaymentID(){
	return paymentID;
}

public String getPaymentDate(){
	return paymentDate;
}

public Booking getBooking(){
	return booking;
}

public double getSubTotal(){
	return subTotal;
}

public double getTax(){
	return tax;
}

public double getDiscountAmt(){
	return discountAmt;
}

public double getTotalAmount(){
	return totalAmount;
}

//---------------------- setters ----------------------
public void setSubTotal(double subTotal){
	this.subTotal = subTotal;
}

public void setDiscountAmt(double discountAmt){
	this.discountAmt = discountAmt;
}

//---------------------- other methods -----------------
// ADD these static methods below your existing instance methods

public static double calculateSubTotal(double ticketTotal, double foodTotal) {
    return ticketTotal + foodTotal;
}

public static double calculateTax(double subTotal) {
    return subTotal * TAX_RATE;
}

public static double calculateTotalAmount(double subTotal, double discountAmt) {
    return subTotal + calculateTax(subTotal) - discountAmt;
}

public static String toString(double ticketTotal, double foodTotal,
                                         double discountAmt, String rewardCode,
                                         List<Reward> allRewards) {
    double subTotal = calculateSubTotal(ticketTotal, foodTotal);
    double tax      = calculateTax(subTotal);
    double total    = calculateTotalAmount(subTotal, discountAmt);

    String discountDisplay = String.format("-RM %.2f", discountAmt);
    if (rewardCode != null && !rewardCode.isEmpty()) {
        for (Reward r : allRewards) {
            if (r.getRewardID().equals(rewardCode) && r instanceof Discount) {
                discountDisplay = String.format("-RM %6.2f (%.0f%%)", discountAmt,
                                               ((Discount) r).getPercentage());
                break;
            }
        }
    }

    return String.format(
        "%-16s  RM %6.2f\n" +
        "%-16s  RM %6.2f\n" +
        "%-16s %s\n"        +
        "%-16s %s\n"        +
        "%-16s  RM %6.2f\n",
        "Sub Total     :", subTotal,
        "Tax (8%)      :", tax,
        "Discount      :", discountDisplay,
        " ",               "--------------------",
        "Total         :", total);
}

public static double applyDiscount(String rewardCode, double subTotal, List<Reward> allRewards) {
    for (Reward r : allRewards) {
        if (rewardCode.equals(r.getRewardID()) && r instanceof Discount) {
            return subTotal * (((Discount) r).getPercentage() / 100.0);
        }
    }
    return 0.0;
}

public static String paymentHeader(){
	return String.format("%-18s \n%-18s \n%-18s\n\n", 
							" ================", "|    Payment     |", " ================");
}

public static String confirmationMsg(){
	return "\nConfirm payment? (Y - Yes / N - No) : ";
}
public static String confirmMsg(){
	return "Payment confirmed! Thank you for your purchase.";
}
public static String cancelledMsg(){
	return "Payment cancelled. No charges were made.";
}
public static String invalidConfirmMsg(){
	return "Invalid input. Please enter Y or N only.";
}

public void earnRewardPts(Customer customer){
	int previousPts = customer.getRewardPts();
	int addPts = (int) this.totalAmount;
	customer.setRewardPts(previousPts + addPts);
}
}
