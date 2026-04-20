import java.util.List;
import java.util.ArrayList;

public class TicketValidation {
    private Ticket ticket;
    private Show showtime;

    public TicketValidation() {
        this.ticket = new Ticket();
        this.showtime = new Show();
    }

    public TicketValidation(Ticket ticket, Show showtime) {
        this.ticket = ticket;
        this.showtime = showtime;
    }

    public Ticket getTicket() { return ticket; }
    public Show getShowtime() { return showtime; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    public void setShowtime(Show showtime) { this.showtime = showtime; }

    // ----------------------------------------------------------------
    // Validates a ticket against its show
    // ----------------------------------------------------------------
    public String validateTicket(Ticket ticket, Show showtime) {
        if (ticket == null)
            return "Validation Failed: Ticket is null.";
        if (showtime == null)
            return "Validation Failed: Showtime is null.";

        String type = ticket.getTicketType();
        if (type == null || type.trim().isEmpty())
            return "Validation Failed: Ticket [" + ticket.getTicketID() + "] has no ticket type assigned.";

        if (ticket.getTicketAmt() <= 0.0)
            return "Validation Failed: Ticket [" + ticket.getTicketID() + "] has an invalid amount.";

        if (!showtime.isStartTimeInFuture(showtime.getStartDate(), showtime.getStartTime()))
            return "Validation Failed: Show [" + showtime.getShowId() + "] has already passed. " +
                   "Ticket [" + ticket.getTicketID() + "] is no longer valid.";

        return "Validation Successful: Ticket [" + ticket.getTicketID() + "] (" + type + ")" +
               " is valid for Show [" + showtime.getShowId() + "] on " +
               showtime.getStartDate() + " at " + showtime.getStartTime() + ".";
    }

    // ----------------------------------------------------------------
    // Checks ticket status by ticket ID - searches across all bookings
    // ----------------------------------------------------------------
    public String checkTicketStatus(String ticketId, Show showtime) {
        if (ticketId == null || ticketId.trim().isEmpty())
            return "Status Check Failed: Ticket ID is null or empty.";
        if (showtime == null)
            return "Status Check Failed: Showtime is null.";
        if (this.ticket == null)
            return "Status Check Failed: No ticket found with ID [" + ticketId + "].";
        if (!this.ticket.getTicketID().equalsIgnoreCase(ticketId.trim()))
            return "Status Check Failed: Ticket [" + ticketId + "] does not match this record.";

        boolean isUpcoming = showtime.isStartTimeInFuture(showtime.getStartDate(), showtime.getStartTime());
        String showStatus = isUpcoming ? "Upcoming" : "Already Screened";

        return "Ticket Status:"
                + "\n  Ticket ID    : " + this.ticket.getTicketID()
                + "\n  Ticket Type  : " + this.ticket.getTicketType()
                + "\n  Ticket Price : RM" + String.format("%.2f", this.ticket.getTicketAmt())
                + "\n  Show ID      : " + showtime.getShowId()
                + "\n  Show Date    : " + showtime.getStartDate()
                + "\n  Show Time    : " + showtime.getStartTime() + " - " + showtime.getEndTime()
                + "\n  Show Status  : " + showStatus;
    }

    // ----------------------------------------------------------------
    // STAFF FUNCTION 1:
    // Staff scans/enters a Ticket ID - system finds it in DataStorage
    // and validates it against its booking's show automatically
    // ----------------------------------------------------------------
    public static void staffValidateByTicketID(String ticketId, DataStorage storage) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.out.println("  Please enter a valid Ticket ID.");
            return;
        }

        // Search all bookings in storage for the matching ticket
        Ticket foundTicket = null;
        Show   foundShow   = null;

        for (Booking b : storage.getAllBookings()) {
            if (b.getTickets() == null) continue;
            for (Ticket t : b.getTickets()) {
                if (t.getTicketID().equalsIgnoreCase(ticketId.trim())) {
                    foundTicket = t;
                    foundShow   = b.getShow();
                    break;
                }
            }
            if (foundTicket != null) break;
        }

        if (foundTicket == null) {
            System.out.println("  Ticket [" + ticketId + "] not found in the system.");
            return;
        }

        // Run validation and status check
        TicketValidation tv = new TicketValidation(foundTicket, foundShow);
        System.out.println("\n" + tv.validateTicket(foundTicket, foundShow));
        System.out.println("\n" + tv.checkTicketStatus(ticketId, foundShow));
    }

    // ----------------------------------------------------------------
    // STAFF FUNCTION 2:
    // Staff enters a Customer ID - system shows and validates ALL
    // tickets across all bookings for that customer
    // ----------------------------------------------------------------
	public static void staffValidateByCustomerID(String customerID, DataStorage storage) {
    	if (customerID == null || customerID.trim().isEmpty()) {
        	System.out.println("  Please enter a valid Customer ID.");
        	return;
    	}

   
    	List<Booking> allBookings = storage.getAllBookings();
    	List<Booking> customerBookings = new ArrayList<>();

    	for (Booking b : allBookings) {
        	if (b.getCustomerID() != null && b.getCustomerID().equals(customerID)) {
            	customerBookings.add(b);
        	}
    	}

    	if (customerBookings.isEmpty()) {
        	System.out.println("  No bookings found for Customer [" + customerID + "].");
        	return;
    	}

    	System.out.println("\n  === Ticket Validation for Customer [" + customerID + "] ===");
    	int bookingNum = 1;
    	for (Booking b : customerBookings) {
        	System.out.println("\n  -- Booking " + bookingNum++ + " --");
        	Show show = b.getShow();
        	Ticket[] tickets = b.getTickets();

        	if (show == null) {
            	System.out.println("  No show linked to this booking.");
            	continue;
        	}
        	if (tickets == null || tickets.length == 0) {
            	System.out.println("  No tickets found in this booking.");
            	continue;
        	}
        	for (Ticket t : tickets) {
            	TicketValidation tv = new TicketValidation(t, show);
            	System.out.println("  " + tv.validateTicket(t, show));
       		}
    	}
	}
    @Override
	public boolean equals(Object obj) {
    	if (this == obj) return true;
    	if (obj == null) return false;
        if (!(obj instanceof TicketValidation)) return false;

    	TicketValidation other = (TicketValidation) obj;

    
    	return this.ticket.equals(other.ticket) &&
           this.showtime.equals(other.showtime);
	}
	
    public String toString() {
        return "TicketValidation {"
                + "\n  Ticket   : " + (ticket != null ? ticket.getTicketID() : "null")
                + "\n  Showtime : " + (showtime != null ? showtime.getShowId() : "null")
                + "\n}";
    }
}
