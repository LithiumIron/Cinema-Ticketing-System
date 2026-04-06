
public class EWalletPayment extends PaymentMethod {
//data properties
private int walletType;
private String walletOwner;
private String walletAccID;

//----------------------- constructors --------------------
public EWalletPayment(){
	this(0, 0, " ", " ");
	
}

public EWalletPayment(int methodType, int walletType,
						String walletOwner, String walletAccID){
	super(methodType);
	this.walletType = walletType;
	this.walletOwner = walletOwner;
	this.walletAccID = walletAccID;
}

//---------------------- getters ---------------------------
public int getWalletType(){
	return walletType;
}

public String getWalletOwner(){
	return walletOwner;
}

public String getWalletAccID(){
	return walletAccID;
}

//--------------------- setters ---------------------------
public void setWalletType(int walletType){
	this.walletType = walletType;
}

public void setWalletOwner(String walletOwner){
	this.walletOwner = walletOwner;
}

public void setWalletAccID(String walletAccID){
	this.walletAccID = walletAccID;
}

//--------------------- other methods ---------------------
public String eWalletType(){
	return "Please select an e-wallet type.\n" +
			"1. Touch 'n Go\n" +
			"2. GrabPay\n" +
			"3. ShopeePay\n" +
			"Enter your preferred e-wallet type : ";
}

public String validateEWalletType(int walletType){
	if(walletType < 1 || walletType > 3)
		return "Invalid e-wallet type! Please select again.\n";
	else
		return "\n";
}

public String validateWalletOwner(){
	//validate wallet owner
	if(walletOwner == null || walletOwner.trim().isEmpty()){
		return "Wallet owner name cannot be empty!";
	//.trim().isEmpty - to see whether front and back of the name is empty or not

	}else if(!walletOwner.trim().matches("[A-Z ]+")){
		//[A-Z }+ means wallet owner can only accept capital letters with space
		// + sign means can include more than 1 letters
		return "Wallet owner name must contain capital letters only!";
	}else{
		return "";
	}
}

public String validateWalletAccID(){
	if(walletAccID.trim().isEmpty()){
		System.out.println("Wallet account ID cannot be empty!\n");
	} else if(!walletAccID.matches("\\d+")){
		System.out.println("Wallet account ID must contain digits only!\n");
	} else if(!walletAccID.startsWith("01")){
		System.out.println("Wallet account ID must start with '01'!\n");
	} else if(walletAccID.startsWith("010") || walletAccID.startsWith("011")){
		//ID that start with 010 and 011 must exactly 11 digits
		if(walletAccID.length() != 11)
			System.out.println("Wallet account ID starting with 010 or 011 must be 11 digits in total.\n");
	} else{
		//all other ID must exactly 10 digits
		if(walletAccID.length() != 10)
			System.out.println("Wallet account ID must be 10 digits in total.\n");
	}
	return "";
} 

public String toString(){
	if(walletType == 1)
		return "\nWallet Type       : Touch n' Go" +
		"\nWallet owner name : " + walletOwner +
		"\nWallet account ID : " + maskAccID(walletAccID); 
	
	if(walletType == 2)
		return "\nWallet Type       : GrabPay" +
		"\nWallet owner name : " + walletOwner +
		"\nWallet account ID : " + maskAccID(walletAccID);
		
	if(walletType == 3)
		return "\nWallet Type       : ShopeePay" +
		"\nWallet owner name : " + walletOwner +
		"\nWallet account ID : " + maskAccID(walletAccID);
		
	return "";
    
}
public static String eWalletHeader(){
	return String.format("%-25s \n%-25s \n%-25s\n", 
							" =========================", "|    E-wallet Payment     |", " =========================");
}


private String maskAccID(String accountID){
	String first3 = accountID.substring(0, 3);
	String last4 = accountID.substring(walletAccID.length() - 4);
	String masked = "*".repeat(accountID.length() - 7);//remaining middle digits
	
	return first3 + masked + last4;
}
}