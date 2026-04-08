import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Payment {
//data properties
private String paymentID;
private double subTotal;
private double tax;
private double discount;
private double totalAmount;
private String paymentDate;

private static int counter = 0;
private static final double TAX_RATE = 0.08;// 8% tax


//-------------------------- constructors -------------------
public Payment(){
	this.paymentID = String.format("P%05d", counter++);
	this.paymentDate = " ";
	this.subTotal = 0.0;
	this.tax = 0.0;
	this.discount = 0.0;
	this.totalAmount = 0.0;
}

public Payment(Ticket [] tic, FoodOrder fo){

    this.paymentID = String.format("P%05d", counter++);
    this.paymentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    this.subTotal = Ticket.calculateTicketTotal(tic) + fo.getTotalPrice();
    this.tax = calculateTax();
    this.discount = calculateDiscount();
    this.totalAmount = calculateTotalAmount();

} 
//------------------------ getters -----------------------
public String getPaymentID(){
	return paymentID;
}

public String getPaymentDate(){
	return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
}

public double getSubTotal(){
	return subTotal;
}

public double getTax(){
	return tax;
}

public double getDiscount(){
	if(subTotal >= 100){
		return 0.15; //15% dicount if subtotal >= 100
	} else if(subTotal >= 50){
		return 0.10;//10% dicount if subtotal >= 50
	}else
		return 0.00;//no dicount given
}

public double getTotalAmount(){
	return totalAmount;
}


//---------------------- setters ----------------------
public void setPaymentDate(){
	this.paymentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
}

public void setSubTotal(double subTotal){
	this.subTotal = subTotal;
}




//---------------------- other methods -----------------
public double calculateTax(){
	return subTotal * TAX_RATE;
}

public double calculateDiscount(){
	return subTotal * getDiscount();
}

public double calculateTotalAmount(){
	return subTotal + calculateTax() - calculateDiscount();
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


public String toString(){
	return String.format("%-16s %s\n" + "%-16s %s\n\n" +
            			"%-16s RM %.2f\n" + "%-16s RM %.2f\n" +
            			"%-16s RM %.2f\n" + "%-16s %s\n" +
            			"%-16s RM %.2f\n",
            			"Payment ID  :",    paymentID,
            			"Payment Date:" ,  getPaymentDate(),
            			"Sub Total   :",     subTotal,
            			"Tax (8%)    :",      calculateTax(),
            			"Discount    :", calculateDiscount(),
            			" ",              "--------------------",
            			"Total       :",   calculateTotalAmount());
}

}