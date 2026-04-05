import java.util.Scanner;
public class TestFood {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FoodOrder.loadOrderNo();
        FoodOrder f1= new FoodOrder();
        Snack s1= new Snack();
        Drink d1= new Drink();
        Combo c1= new Combo();
        

        //testrun only
        while (true) {
        
                System.out.println("\n=================================");
                System.out.println("          Food Menu");
                System.out.println("=================================");
                System.out.println("1. Snack");
                System.out.println("2. Drinks");
                System.out.println("3. Combo");
                System.out.println("4. Exit");
                System.out.println("=================================");
                System.out.print("Enter choice: ");

                int choice=input.nextInt();
                input.nextLine(); //clean buffer

                switch (choice) {
                    case 1: 
                        System.out.println("===================");
                        System.out.println("Snacks Menu");
                        System.out.println("===================");
                        System.out.print(s1.toString());
                        System.out.print("Enter choice: ");

                        int snack=input.nextInt();
                        input.nextLine(); //clean buffer
                        s1.addSnack(snack, f1);     
                        break;
                    case 2: 
                        System.out.println("===================");
                        System.out.println("Drinks Menu");
                        System.out.println("===================");  
                        System.out.print(d1.toString());
                        System.out.print("Enter choice: ");

                        int drink=input.nextInt();
                        input.nextLine(); //clean buffer
                        d1.addDrink(drink,f1);  
                        break;
                    case 3: 
                        System.out.println("===================");
                        System.out.println("Combos Menu");
                        System.out.println("===================");
                        System.out.print(c1.toString());                        
                        System.out.print("Enter choice: ");

                        int combo=input.nextInt();
                        input.nextLine(); //clean buffer
                        c1.addCombo(combo,f1);  
                        break;
                    case 4: 
                            System.out.println(f1.toString());
                            f1.saveOrder();
                            System.out.println("Thank you for your order!");
                            return;
                    default: System.out.println("Invalid choice!"); continue;
                
                }// switch end
                

        }// while end
    }//main end
}//class end
