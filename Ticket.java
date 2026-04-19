public class Ticket {
//data properties
private String ticketID;
private String ticketType;
private double ticketAmt;

private static int ticketCounter = 1;


//-------------------- methods --------------
//no arg cons
public Ticket(){
	this(" ", 0.0);
}
  
//cons with args
public Ticket(String ticketType, double ticketAmt){
	this.ticketID = generateTicketID();
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
public static String ticketHeader(){
	return String.format("\t\t\t\t%-15s \n\t\t\t\t%-15s \n\t\t\t\t%-15s\n\n%-15s %-17s %-12s\n%-15s %-17s %-12s\n", 
							"========", " Ticket", "========",
							"Ticket ID", "Ticket Type", "Ticket Price", "=========", "===========", "============" );
}

public static String noOfTickets(){
	return "Please enter number of tickets : ";
}

private String generateTicketID() {
    return String.format("T%03d", ticketCounter++);
}

public static double getTicketPriceByType(String typeName) {
        switch (typeName.toUpperCase()) {
            case "ADULT":
                return 28.00; 
            case "CHILD":
                return 15.00;
            case "SENIOR":
                return 12.00;
            default:
                return 0.00; 
        }
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

public static String sizeInvalidMsg(){
	return "Invalid size. Please enter again!";
}
public String toString(){
	return String.format("%-15s %-17s RM%.2f\n",
						ticketID, ticketType, ticketAmt);
	
}

public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ticket)) return false;
        return ticketID.equals(((Ticket) obj).ticketID);
}
    
}
