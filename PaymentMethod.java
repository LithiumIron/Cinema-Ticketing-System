
public class PaymentMethod {
//data properties
private int methodType;


//--------------------- constructors -------------------
public PaymentMethod(){
	this(0);
}

public PaymentMethod(int methodType){
	this.methodType = methodType;
	
}
//-------------------- getters ----------------------
public int getMethodType(){
	return methodType;
}


//------------------- setters -----------------------
public void setMethodType(int methodType){
	this.methodType = methodType;
}

//------------------- other methods -----------------
public String toString(){
	return "\nPlease select a payment method.\n" +
			"1. Credit card payment\n" +
			"2. E-wallet payment\n" +
			"Enter your choice for payment method : ";
}

public static String validatePayMethod(int payMethod){
	if(payMethod == 1) return "Credit Card is selected\n.";
	if(payMethod == 2) return "E-wallet is selected\n.";	

	return "Invalid payment method!\n";
	}
public static String paymentMethodHeader(){
	return String.format("\n%-18s \n%-18s \n%-18s", 
							" ======================", "|    Payment Method    |", " ======================");
}
}


    
