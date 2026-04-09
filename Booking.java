import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class Booking {
    //data properties
    private Show show;
    private FoodOrder foodOrder;
    private Ticket[] tickets;
    private LocalDate bookingDate;
    private LocalTime bookingTime;

    //non-arg constructor
    public Booking(){
        this(new Show(), new FoodOrder(), new Ticket[0]);
    }

    //constructor with argument
    public Booking(Show show, FoodOrder foodOrder, Ticket [] tickets){
        this.show = show;
        this.foodOrder = foodOrder;
        this.tickets = tickets;
        this.bookingDate = LocalDate.now();
        this.bookingTime = LocalTime.now();
    }

    //getter
    public Show getShow() {
        return show;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public Ticket[] getTickets() {
        return tickets;
    }

    //setter
    public void setShow(Show show) {
        this.show = show;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public void setTickets(Ticket[] tickets) {
        this.tickets = tickets;
    }

    //other methods
    public String toString() {
        return "Show: " + show.toString() + 
        "\nFood Order: " + foodOrder.toString() + 
        "\nTickets: " + Arrays.toString(tickets) + 
        "\nBooking Date: " + bookingDate + 
        "\nBooking Time: " + bookingTime;
    }
}
