import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
    
public class Snack extends FoodOrder {
	private String[] snackID = {"S01", "S02", "S03", "S04", "S05"};
	private Map<String, String> snack = new HashMap<>();
    private Map<String, Double> snackCost = new HashMap<>();

	public Snack(){
		this(0.0,new HashMap<>(),0);
	}

    public Snack(double totalPrice, Map<String, Integer> order, int itemNo) {
    	super(totalPrice, order, itemNo);
		initializeMenu();
    }


    private void initializeMenu() {
        snack.put("S01", "Popcorn");
        snack.put("S02", "Hotdog");
        snack.put("S03", "French Fries");
        snack.put("S04", "Sweet Corn");
        snack.put("S05", "Nuggets");
        
        snackCost.put("S01", 8.00);
        snackCost.put("S02", 5.00);
        snackCost.put("S03", 4.00);
        snackCost.put("S04", 4.00);
        snackCost.put("S05", 6.00);
    }

    //getter
    public String getSnackAndCost() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String i : snackID) {
            sb.append(String.format("%d. %-21s| RM%.2f\n", 
                     count, snack.get(i), snackCost.get(i)));
            count++;
        }
        return sb.toString();
    }
    public double getSnackCost(int i) {
        if (i >= 0 && i < snackID.length) {
            String id = snackID[i];
            return snackCost.get(id);
        }
        return 0.0;
    }
    public String[] getSnackID() {return snackID;}
    public Map<String, String> getSnack() {return snack;}

    
	//setter
    public void setSnackCost(Map<String, Double> snackCost) {this.snackCost = snackCost;}
    public void setSnackID(String[] snackID) {this.snackID = snackID;}
    public void setSnack(Map<String, String> snack) {this.snack = snack;}

    public void addSnack(int choice, FoodOrder order) {
        if (choice >= 1 && choice <= snackID.length) {
            String id = snackID[choice - 1];  // Convert 1 to "S01", 2 to "S02"
            String name = snack.get(id);
            double price = snackCost.get(id);
            
            int size = order.displayFoodSize();  // Reusable!
            
            if (size == 1) {
                price = order.largeCost(price);
                order.addItem(name + " (Large)", price);
            } 
            else if (size == 2) {
                order.addItem(name + " (Regular)", price);
            }
        }
    }

    public String toString(){return getSnackAndCost();}

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Snack)) return false;
        return Arrays.equals(snackID, ((Snack) obj).snackID) &&
               snack.equals(((Snack) obj).snack) &&
               snackCost.equals(((Snack) obj).snackCost);
    }    


}
