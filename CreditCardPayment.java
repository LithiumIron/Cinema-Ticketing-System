import java.time.LocalDate;
public class CreditCardPayment extends PaymentMethod {
//data properties
private String cardHolder;
private String cardNumber;
private int cardCvc;
private int expireMonth;
private int expireYear;

//----------------------- constructors -----------------
public CreditCardPayment(){
	this(0, " ", " ", 0, 0, 0);
	
}

public CreditCardPayment(int methodType, String cardHolder, String cardNumber, int cardCvc,
 							int expireMonth, int expireYear){
	super(methodType);
	this.cardHolder = cardHolder;
	this.cardNumber = cardNumber;
	this.cardCvc = cardCvc;
	this.expireMonth = expireMonth;
	this.expireYear = expireYear;
	
}


//---------------------- getters ----------------------
public String getCardHolder(){
	return cardHolder;
}

public String getCardNumber(){
	return cardNumber;
}

public int getCardCvc(){
	return cardCvc;
}

public int getExpireMonth(){
	return expireMonth;
}

public int getExpireYear(){
	return expireYear;
}

//---------------------- setters ---------------------
public void setCardHolder(String cardHolder){
	this.cardHolder = cardHolder;
}

public void setCardNumber(String cardNumber){
	this.cardNumber = cardNumber;
}

public void setCardCvc(int cardCvc){
	this.cardCvc = cardCvc;
}

public void setExpireMonth(int expireMonth){
	this.expireMonth = expireMonth;
}

public void setExpireYear(int expireYear){
	this.expireYear = expireYear;
}
//---------------------- other methods ---------------
public String validateCardHolder(){
	//validate card holder
	if(cardHolder == null || cardHolder.trim().isEmpty()){
		return "Card holder name cannot be empty!";
	//.trim().isEmpty - to see whether front and back of the name is empty or not

	}else if(!cardHolder.trim().matches("[A-Z ]+")){
		//[A-Z }+ means card holder can only accept capital letters with space
		// + sign means can include more than 1 letters
		return "Card holder name must contain capital letters only!";
	}else{
		return "";
	}
}

public String validateCardNum(){	
	//validate card number
	if(cardNumber == null || !cardNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}"))
		//card number must have 16 digits and cannot be null
		return "Invalid card number. Must be 16 digits and follow the format!";
	else
		return "";
}

public String validateCardCvc(){
	//validate card cvc - card validation code
	if(cardCvc < 100 || cardCvc > 9999)
		return "Invalid card cvc! (100-9999)";
	else
		return "";
}
	
	LocalDate today = LocalDate.now();
	int currentMonth = today.getMonthValue();
	int currentYear = today.getYear() % 100;

public String validateMonth(){	
	//validate expire month
	if(expireMonth < 1 || expireMonth > 12)
		return "Invalid expiry month!";
	else
		return "";
}

public String validateYear(){
	//validate expire year
	if(expireYear < currentYear || expireYear > currentYear + 10)
		return "Invalid expiry year! Enter last 2 digits only";
		
	else
		return "";
}

public String validateExpire(){
	//validate whether card has expired in current year 
	if(expireYear == currentYear && expireMonth < currentMonth)
		return "Card has expired.";
	else	
		return "Card is valid.";
}

public static String creditCardHeader(){
	return String.format("%-28s \n%-28s \n%-28s\n", 
							" ===========================", "|    Credit Card Payment    |", 
								" ===========================");
}
public String toString(){
	return "\nCard holder name  : " + cardHolder +
		"\nCard expire(MM/YY): " + String.format("%02d", expireMonth) + "/" + expireYear;
}
    
}