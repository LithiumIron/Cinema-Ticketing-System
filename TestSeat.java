import java.util.Scanner;

public class TestSeat {
    public static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Initialize seats
        Seat.initializeAllHalls();
        
        //because hall 没有连跟movie我就这样testrun
        while (true) {
            System.out.println("\n=================================");
            System.out.println("        SELECT HALL");
            System.out.println("=================================");
            System.out.println("1. Small Hall");
            System.out.println("2. Medium Hall");
            System.out.println("3. Large Hall");
            System.out.println("4. Exit");
            System.out.println("=================================");
            System.out.print("Enter choice: ");
            
            int choice = scan.nextInt();
            scan.nextLine();
            
            Hall hall = null;
            String hallType = "";
            
            switch (choice) {
                case 1:
                    hallType = "Small";
                    break;
                case 2:
                    hallType = "Medium";
                    break;
                case 3:
                    hallType = "Large";
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }

            hall = new Hall(hallType);
            hallMenu(hall);
        }
    }
    
    private static void hallMenu(Hall hall) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("           " + hall.getType() + " Hall");
            System.out.println("=================================");
            System.out.println("1. View Seat Map");
            System.out.println("2. Book a Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Go back");
            System.out.println("=================================");
            System.out.print("Enter choice: ");
            
            int choice = scan.nextInt();
            scan.nextLine();
            
            switch (choice) {
                case 1:
                    hall.displaySeatMap();
                    Utils.pause(scan);
                    break;
                case 2:
                    hall.displaySeatMap();
                    System.out.print("Enter seat number (e.g., C5, Db): ");
                    String seat = scan.nextLine();
                    hall.bookSeat(seat);
                    break;
                case 3:
                    hall.displaySeatMap();
                    System.out.print("\nEnter seat number to cancel: ");
                    String cancelSeat = scan.nextLine();
                    hall.cancelBooking(cancelSeat);
                    break;
                case 4:
                    return;  
                default:
                    System.out.println("Invalid choice!");
            }// switch end

        }//while end

    }// method end

}//class end