import java.util.HashMap;
import java.util.Map;

public class Drink extends FoodOrder {
	private String[] drinkID = {"D01", "D02", "D03", "D04", "D05"};
	private Map<String, String> drink = new HashMap<>();
    private Map<String, Double> drinkCost = new HashMap<>();

	public Drink(){
		this(0.0,new HashMap<>(),0);
	}

    public Drink(double totalPrice, Map<String, Integer> order, int itemNo) {
    	super(totalPrice, order, itemNo);
		initializeMenu();	
    }
    
   private void initializeMenu() {
        drink.put("D01", "Sprite");
        drink.put("D02", "Cola");
        drink.put("D03", "100 Plus");
        drink.put("D04", "Water");
        drink.put("D05", "Pepsi");

        drinkCost.put("D01", 3.00);
        drinkCost.put("D02", 3.00);
        drinkCost.put("D03", 3.00);
        drinkCost.put("D04", 1.50);
        drinkCost.put("D05", 4.00);
    }

    //getter
    public String getDrinkAndCost() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String i : drinkID) {
            sb.append(String.format("%d. %-21s| RM%.2f\n", 
                     count, drink.get(i), drinkCost.get(i)));
            count++;
        }
        return sb.toString();
    }
	public Map<String, Double> getDrinkCost() {return drinkCost;}
    public String[] getDrinkID() {return drinkID;}
    public Map<String, String> getDrink() {return drink;}

    
	//setter
    public void setDrinkCost(Map<String, Double> drinkCost) {this.drinkCost = drinkCost;}
    public void setDrinkID(String[] drinkID) {this.drinkID = drinkID;}
    public void setDrink(Map<String, String> drink) {this.drink = drink;}

    public boolean addDrink(int choice, FoodOrder order) {
        if (choice >= 1 && choice <= drinkID.length) {
            String id = drinkID[choice - 1];  // Convert 1 to "S01", 2 to "S02"
            String name = drink.get(id);
            double price = drinkCost.get(id);
            
            int size = order.displayFoodSize();  // Reusable!
            
            if (size == 1) {
                price = order.largeCost(price);
                order.addItem(name + " (Large)", price);
                return true;
            } 
            else if (size == 2) {
                order.addItem(name + " (Regular)", price);
                return true;
            }
            else
            	return false;
        }
        else return false;  
    }

    public String toString(){return getDrinkAndCost();}

    public boolean equals(Object obj) {
        if ((obj instanceof Drink)) {
            Drink d=(Drink)obj;
            return this.drinkID.equals(d.drinkID);
        }
        else 
            return false;
        
    }  
}
