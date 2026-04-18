import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    //data properties
    private String bookingId;
    private Show show;
    private FoodOrder foodOrder;
    private Ticket [] tickets;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private static int bookingCounter;

    //non-arg constructor
    public Booking(){
        this(new Show(), new FoodOrder(), new Ticket[0]);
    }

    //constructor with argument
    public Booking(Show show, FoodOrder foodOrder, Ticket [] tickets){
        this.bookingId = generateBookingId();
        this.show = show;
        this.foodOrder = foodOrder;
        this.tickets = tickets;
        this.bookingDate = LocalDate.now();
        this.bookingTime = LocalTime.now();
        bookingCounter++;
    }

    public Show getShow(){
        return show;
    }

    public FoodOrder getFoodOrder(){
        return foodOrder;
    }

    public Ticket[] getTickets(){
        return tickets;
    }

    //other methods
    public String toString(){
        return "Booking Movie :" + this.getShow().getMovie() +
               "\nVenue : " +
               "\nCinema :" + this.getShow().getCinema() +
               "\nHall : Hall " + this.getShow().getHall().getHallId() +
               "\nFood Order :" + this.getFoodOrder();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Booking)) return false;
        return bookingId.equals(((Booking) obj).bookingId);
    }

    private String generateBookingId(){
        return String.format("B%03d", bookingCounter);
    }
}
