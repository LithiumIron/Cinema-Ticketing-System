import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Payment {
//data properties
private String paymentID;
private Booking booking;
private double subTotal;
private double tax;
private double discountAmt;
private double totalAmount;
private String paymentDate;

private static int counter = 0;
private static final double TAX_RATE = 0.08;// 8% tax


//-------------------------- constructors -------------------
public Payment(){
	this.paymentID = String.format("P%05d", counter++);
	this.booking = new Booking();
	this.paymentDate = " ";
	this.subTotal = 0.0;
	this.tax = 0.0;
	this.discountAmt = 0.0;
	this.totalAmount = 0.0;
}

public Payment(Booking booking){

    this.paymentID = String.format("P%05d", counter++);
    this.booking = booking;
    this.paymentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    this.subTotal = calculateSubTotal();
    this.tax = calculateTax();
    this.discountAmt = 0.0;
    this.totalAmount = calculateTotalAmount();

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
public double calculateTax(){
	return subTotal * TAX_RATE;
}

/*
select cinema > select movie > select show > select seat > select food order > confirm booking >
booking() > type code > selectReward() > isRewardTypeDiscount() > 
calculateSubTotal() calculateDiscountAmt() > calculateTax() > calculatetotalAmt() > confirm payment > payment()
*/

public Redemption findUnusedRedemption(String rewardCode, Customer customer) {
    if (rewardCode == null || customer == null) return null;

    for (Redemption redemption : customer.getRedemptions()) {
        Reward reward = redemption.getReward();
        if (reward == null) continue;

        boolean sameCode = rewardCode.equalsIgnoreCase(reward.getRewardCode());
        boolean unused = "Unused".equalsIgnoreCase(redemption.getRedeemRewardStatus());

        if (sameCode && unused) {
            return redemption; 
        }
    }
    return null; 
}

public boolean isMinimumSpendMet(Redemption redemption) {
    return this.subTotal >= redemption.getReward().getRequiredSpend();
}

public void applyReward(Redemption redemption) {
    Reward reward = redemption.getReward();

    if (reward instanceof Discount) {
        double percentage = ((Discount) reward).getPercentage();
        this.discountAmt = this.subTotal * (percentage / 100.0);
    } else {
        this.discountAmt = 0.0; 
    }

    this.totalAmount = this.subTotal + this.tax - this.discountAmt;
    redemption.setRedeemRewardStatus("Used");
}

public double calculateSubTotal(){
	if (booking == null) {
		return 0.0;
	}

	Ticket[] tickets = booking.getTickets();
	FoodOrder foodOrder = booking.getFoodOrder();

	double ticketTotal = (tickets == null) ? 0.0 : Ticket.calculateTicketTotal(tickets);
	double foodTotal = (foodOrder == null) ? 0.0 : foodOrder.getTotalPrice();

	return ticketTotal + foodTotal;
}

public double calculateTotalAmount(){
	return subTotal + calculateTax();
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


public String toString() {
        return String.format("%-16s %s\n" + "%-16s %s\n\n" +
                        "%-16s RM %.2f\n" + "%-16s RM %.2f\n" +
                        "%-16s -RM %.2f\n" + "%-16s %s\n" +
                        "%-16s RM %.2f\n",
                        "Payment ID   :",    paymentID,
                        "Payment Date :" ,  paymentDate,
                        "Sub Total    :",    subTotal,
                        "Tax (8%)     :",    tax,
                        "Discount     :",    discountAmt,    // 直接用属性
                        " ",              "--------------------",
                        "Total        :",    totalAmount);   // 直接用属性
    }
}
