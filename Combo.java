import java.util.HashMap;
import java.util.Map;

public class Combo extends FoodOrder {
	private String[] comboID = {"C01", "C02", "C03", "C04", "C05"};
	private Map<String, String> combo = new HashMap<>();
    private Map<String, Double> comboCost = new HashMap<>(); 

	public Combo(){
		this(0.0,new HashMap<>(),0);
	}

    public Combo(double totalPrice, Map<String, Integer> order, int itemNo) {
    	super(totalPrice, order, itemNo);
		initializeMenu();
    }
    
    public void initializeMenu() {
        combo.put("C01", "Popcorn + Sprite (Regular)");
        combo.put("C02", "Hotdog x2 + Cola (Regular)");
        combo.put("C03", "French Fries + 100 Plus (Regular)");
        combo.put("C04", "Sweet Corn + Sprite (Regular)");
        combo.put("C05", "Nuggets + Pepsi (Regular)");

        comboCost.put("C01", 13.00);
        comboCost.put("C02", 12.00);
        comboCost.put("C03", 6.00);
        comboCost.put("C04", 9.50);
        comboCost.put("C05", 8.00);
    }

    //getter
    public String getComboAndCost() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String i : comboID) {
            sb.append(String.format("%d. %-34s| RM%.2f\n", 
                     count, combo.get(i), comboCost.get(i)));
            count++;
        }
        return sb.toString();
    }
	public Map<String, Double> getComboCost() {return comboCost;}
    public String[] getComboID() {return comboID;}
    public Map<String, String> getCombo() {return combo;}

    
	//setter
    public void setComboCost(Map<String, Double> comboCost) {this.comboCost = comboCost;}
    public void setComboID(String[] comboID) {this.comboID = comboID;}
    public void setCombo(Map<String, String> combo) {this.combo = combo;}

    public void addCombo(int choice, FoodOrder order) {
        if (choice >= 1 && choice <= comboID.length) {
            String id = comboID[choice - 1];  // Convert 1 to "C01", 2 to "C02"
            
            order.addItem(combo.get(id), comboCost.get(id));
            
        }
    }
    
   
    public String toString(){return getComboAndCost();}

    public boolean equals(Object obj) {
        if ((obj instanceof Combo)) {
            Combo c=(Combo)obj;
            return this.comboID.equals(c.comboID);
        }
        else 
            return false;
        
    }  
}
