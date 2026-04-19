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

public double calculateSubTotal(){
    if (booking == null) return 0.0;

    double ticketTotal = 0.0;
    Ticket[] tickets = booking.getTickets();
    if (tickets != null && tickets.length > 0) {
        ticketTotal = Ticket.calculateTicketTotal(tickets);
    }

    double foodTotal = 0.0;
    FoodOrder foodOrder = booking.getFoodOrder();
    if (foodOrder != null) {
        foodTotal = foodOrder.getTotalPrice();
    }

    return ticketTotal + foodTotal;
}

public boolean applyReward(String rewardCode, List<Reward> allRewards) {
    if (rewardCode == null || allRewards == null) return false;

    for (Reward reward : allRewards) {
        if (rewardCode.equals(reward.getRewardID())) {
            
            this.subTotal = calculateSubTotal();
            
            if (reward instanceof Discount) {
                double percentage = ((Discount) reward).getPercentage();
                this.discountAmt = this.subTotal * (percentage / 100.0);
                this.tax = calculateTax();
                this.totalAmount = calculateTotalAmount();
                return true;
            }
            
            if (reward instanceof Freebie) {
                Freebie freebie = (Freebie) reward;
                System.out.println("Freebie successfully redeemed: " + freebie.getFreebieItems());
                this.discountAmt = 0.0; 
                this.tax = calculateTax(); 
                this.totalAmount = calculateTotalAmount();
                return true;
            }
        }
    }
    System.out.println("Invalid reward code.");
    return false; 
}

public double calculateTotalAmount(){
	return subTotal + calculateTax() - discountAmt;
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


public String toString(String rewardCode, List<Reward> allRewards) {
    double currentTax = calculateTax();
    double currentTotal = calculateTotalAmount();
    String discountDisplay = String.format("-RM %.2f", this.discountAmt);

    if (rewardCode != null && !rewardCode.isEmpty()) {
        for (Reward r : allRewards) {
            if (r.getRewardID().equals(rewardCode)) {
                
                if (r instanceof Discount) {
                    Discount d = (Discount) r; 
                    
                    double percentage = d.getPercentage();
                    discountDisplay = String.format("-RM %6.2f (%.0f%%)", this.discountAmt, percentage);
                } 

                break;
            }
        }
    }

    return String.format("%-16s %s\n" + "%-16s %s\n\n" +
                        "%-16s  RM %6.2f\n" + "%-16s  RM %6.2f\n" +
                        "%-16s %s\n" +      
                        "%-16s %s\n" + 
                        "%-16s  RM %6.2f\n",
                        "Payment ID    :", paymentID,
                        "Payment Date  :", paymentDate,
                        "Sub Total     :", subTotal,
                        "Tax (8%)      :", currentTax, 
                        "Discount      :", discountDisplay, 
                        " ",               "--------------------",
                        "Total         :", currentTotal); 
}

public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Payment)) return false;
        return paymentID.equals(((Payment) obj).paymentID);
    }
}
