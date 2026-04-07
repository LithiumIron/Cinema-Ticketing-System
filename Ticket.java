
public class Ticket {
//data properties
private String ticketID;
private String ticketType;
private double ticketAmt;

private static int counter = 0;


//-------------------- methods --------------
//no arg cons
public Ticket(){
	this.ticketID = String.format("T%03d", counter++);
	this.ticketType = " ";
	this.ticketAmt = 0.0;
}
  
//cons with args
public Ticket(String ticketType, double ticketAmt){
	this.ticketID = String.format("T%03d", counter++);
	this.ticketType = ticketType;
	this.ticketAmt = ticketAmt;

}

//getter
public String getTicketID(){
	return ticketID;
}

public String getTicketType(){
	return ticketType;
}

public double getTicketAmt(){
	return ticketAmt;
}


//setter
public void setTicketType(String ticketType){
	this.ticketType = ticketType;
}

public void setTicketAmt(double ticketAmt){
	this.ticketAmt = ticketAmt;
}


//------------------ other methods --------------------
public static String ticketHeader1(){
	return String.format("\n%-18s \n%-18s \n%-18s\n\n", 
							" ====================", "|      Ticket        |", " ====================");
}

public static String ticketHeader2(){
	return String.format("%-20s \n%-20s \n%-20s\n\n%-15s %-17s %-12s\n%-15s %-17s %-12s\n",
							" ======================", "|    Ticket Summary    |", " ======================",
							"Ticket ID", "Ticket Type", "Ticket Price", 
							"=========", "===========", "============");
}

public static String noOfTickets(){
	return "Please enter number of tickets : ";
}

public static String selectHallSize(){
	return "\nPlease select a hall size.\n" +
			"1. Small\n" +
			"2. Medium\n" +
			"3. Large\n" +
			"Enter your choice for hall size : ";
}
public static String selectTicketType1(){
	return "\n[Small Hall] Please select a ticket type.\n" +
			"1. Adult  - RM 15.00\n" +
			"2. Child  - RM 10.00\n" +
			"3. Senior - RM 10.00\n" +
			"Enter your choice for ticket ";
} 

public static String selectTicketType2(){
	return "\n[Medium Hall] Please select a ticket type.\n" +
			"1. Adult  - RM 20.00\n" +
			"2. Child  - RM 12.00\n" +
			"3. Senior - RM 12.00\n" +
			"Enter your choice for ticket ";
} 

public static String selectTicketType3(){
	return "\n[Large Hall] Please select a ticket type.\n" +
			"1. Adult  - RM 25.00\n" +
			"2. Child  - RM 15.00\n" +
			"3. Senior - RM 15.00\n" +
			"Enter your choice for ticket ";
} 

public static double calculateTicketTotal(Ticket [] tic){
	double ticketTotal = 0.0;
	for(Ticket t : tic){
		ticketTotal += t.getTicketAmt();
	}
	return ticketTotal;	
}

public static String printTotal(Ticket [] tic){
	return String.format("%-15s %-11s \t  RM%.2f", " ", "Total:", calculateTicketTotal(tic));
	
}

public static String invalidType(){
	return "Invalid ticket type! Please select again.";
}

public static String sizeInvalidMsg(){
	return "Invalid size. Please enter again!";
}
public String toString(){
	return String.format("%-15s %-17s RM%.2f\n",
						ticketID, ticketType, ticketAmt);
	
}

    
}
