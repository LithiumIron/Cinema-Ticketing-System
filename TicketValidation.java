public class TicketValidation {
    // data properties
    private Ticket ticket;
    private Show showtime;
	//methods
    // no args cons
    public TicketValidation() {
        this(new Show(), new Ticket());
    }

    // cons with args
    public TicketValidation(Ticket ticket, Show showtime) {
        this.ticket = ticket;
        this.showtime = showtime;
    }

    // getters
    public Ticket getTicket() {
        return ticket;
    }

    public Show getShowtime() {
        return showtime;
    }

    // setters
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setShowtime(Show showtime) {
        this.showtime = showtime;
    }

    // -------------------------------------------------------
    // Validates a ticket against a given showtime.
    // Returns a status message indicating if the ticket is valid.
    // -------------------------------------------------------
    public String validateTicket(Ticket ticket, Show showtime) {
        if (ticket == null) {
            return "Validation Failed: Ticket is invalid.";
        }
        if (showtime == null) {
            return "Validation Failed: Showtime is invalid.";
        }

        // Check ticket type is not blank
        String type = ticket.getTicketType();
        if (type == null || type.trim().isEmpty()) {
            return "Validation Failed: Ticket [" + ticket.getTicketID() + "] has no ticket type assigned.";
        }

        // Check ticket amount is valid
        if (ticket.getTicketAmt() <= 0.0) {
            return "Validation Failed: Ticket [" + ticket.getTicketID() + "] has an invalid amount.";
        }

        // Check showtime has a valid show ID
        if (showtime.getShowId() == null || showtime.getShowId().trim().isEmpty()) {
            return "Validation Failed: Showtime has no valid Show ID.";
        }

        // Check showtime date and time are not in the past
        if (!showtime.isStartTimeInFuture(showtime.getStartDate(), showtime.getStartTime())) {
            return "Validation Failed: Showtime [" + showtime.getShowId() + "] for ticket ["
                    + ticket.getTicketID() + "] has already passed.";
        }

        return "Validation Successful: Ticket [" + ticket.getTicketID() + "] (" + type + ")"
                + " is valid for Show [" + showtime.getShowId() + "] on "
                + showtime.getStartDate() + " at " + showtime.getStartTime() + ".";
    }

    // -------------------------------------------------------
    // Checks the status of a ticket by its ID against a showtime.
    // Returns a status message describing the ticket's current state.
    // -------------------------------------------------------
    public String checkTicketStatus(String ticketId, Show showtime) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return "Status Check Failed: Ticket ID is null or empty.";
        }
        if (showtime == null) {
            return "Status Check Failed: Showtime is null.";
        }

        // Verify the stored ticket matches the given ticketId
        if (this.ticket == null) {
            return "Status Check Failed: No ticket is associated with this validation entry.";
        }

        if (!this.ticket.getTicketID().equalsIgnoreCase(ticketId.trim())) {
            return "Status Check: Ticket [" + ticketId + "] not found in this validation entry.";
        }

        // Check if show is upcoming or has already passed
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

    // -------------------------------------------------------
    // toString override
    // -------------------------------------------------------
    @Override
    public String toString() {
        return "TicketValidation {"
                + "\n  Ticket   : " + (ticket != null ? ticket.getTicketID() : "null")
                + "\n  Showtime : " + (showtime != null ? showtime.getShowId() : "null")
                + "\n}";
    }
}