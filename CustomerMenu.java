import java.util.Scanner;
// food + seat only
public class CustomerMenu {
    public static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        // Initialize seats
        Seat.initializeAllHalls();
        boolean wantQuit=false;
        
        //because 没有specify which hall for which movie我就这样testrun
        while (!wantQuit) {
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
                    hall = new Hall(hallType);
                    hallMenu(hall);
                    break;
                case 2:
                    hallType = "Medium";
                    hall = new Hall(hallType);
                    hallMenu(hall);
                    break;
                case 3:
                    hallType = "Large";
                    hall = new Hall(hallType);
                    hallMenu(hall);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }

        //  !!!!! *built for us(dev) only, otherwise loop, cant quit
            System.out.println("Do you want to quit?"); 
            char quit= scan.next().charAt(0);
            if(quit=='Y'||quit=='y'){
                wantQuit=true;
                break;}
            //prob delete this when combine with other class
        }
    }
    
    private static void hallMenu(Hall hall) {
        while (true) {
            Utils.clearScreen(80);
            System.out.println("\n=================================");
            System.out.println("|          Select Seat          |");
            System.out.println("+===============================+");
            System.out.println("|   1. View Seat Map            |");
            System.out.println("|   2. Book a Seat              |");
            System.out.println("|   3. Cancel Booking           |");
            System.out.println("|   4. Order Food               |");
            System.out.println("|   5. Go Back                  |");
            System.out.println("=================================");
            System.out.print("Enter choice: ");

            if(!scan.hasNextInt()){
                System.out.println("Invalid choice! Please enter a number."); 
                scan.nextLine();
                Utils.pause();
                continue;
            }
            
            int choice = scan.nextInt();
            scan.nextLine();
            
            
            
            switch (choice) {
                case 1:
                    hall.displaySeatMap();
                    Utils.pause();
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
                    displayFoodMenu();
                    break;
                case 5:
                    return;  
                default:
                    System.out.println("Invalid choice!");
            }// switch end
            
            


        }//while end

    }// method end

    public static void displayFoodMenu(){
        FoodOrder.loadOrderNo();
        FoodOrder f1= new FoodOrder();
        Snack s1= new Snack();
        Drink d1= new Drink();
        Combo c1= new Combo();
        
        while (true) {
                Utils.clearScreen(80);
                System.out.println("\n=================================");
                System.out.println("|         Food Menu             |");
                System.out.println("+===============================+");
                System.out.println("|   1. Snack                    |");
                System.out.println("|   2. Drinks                   |");
                System.out.println("|   3. Combo                    |");
                System.out.println("|   4. View Order               |");
                System.out.println("|   5. Cancel Order             |");
                System.out.println("|   6. Done Ordering            |");
                System.out.println("=================================");
                System.out.print("Enter choice: ");

                int choice=scan.nextInt();
                scan.nextLine(); //clean buffer

                switch (choice) {
                    case 1: 
                        Utils.clearScreen(80);
                        System.out.println("=================================");
                        System.out.println("|          Snacks Menu          |");
                        System.out.println("=================================");
                        System.out.print(s1.toString());
                        System.out.println("=================================");
                        System.out.print("Enter choice: ");

                        int snack=scan.nextInt();
                        scan.nextLine(); //clean buffer

                        s1.addSnack(snack, f1);     
                        break;
                    case 2: 
                        Utils.clearScreen(80);
                        System.out.println("=================================");
                        System.out.println("|          Drinks Menu          |");
                        System.out.println("=================================");;  
                        System.out.print(d1.toString());
                        System.out.println("=================================");
                        System.out.print("Enter choice: ");

                        int drink=scan.nextInt();
                        scan.nextLine(); //clean buffer
                        d1.addDrink(drink,f1);  
                        break;
                    case 3: 
                        Utils.clearScreen(80);
                        System.out.println("================================================");
                        System.out.println("|                 Combos Menu                  |");
                        System.out.println("================================================");
                        System.out.print(c1.toString());    
                        System.out.println("================================================");                    
                        System.out.print("Enter choice: ");

                        int combo=scan.nextInt();
                        scan.nextLine(); //clean buffer
                        c1.addCombo(combo,f1);  
                        break;

                    case 4:
                        System.out.println(f1.toString());
                        Utils.pause();
                        break;

                    case 5:
                        f1.deleteOrder();
                        break;
                    case 6: 
                            System.out.println("\n"+f1.toString());
                            f1.saveOrder();
                            System.out.println("Thank you for your order!");
                            Utils.pause();
                            return;
                    default: System.out.println("Invalid choice!"); continue;
                
                }// switch end
                

        }// while end
    }//method end

}//class end
