import java.util.HashMap;
import java.util.Map;

public class Freebie extends Reward{
    //data attributes
    private Map<String, Integer> freebieItems;
    private static int sequence = 0;

    //no-arg constructor
    public Freebie() {
        this("", 0, 0, "", new HashMap<>());
    }

    //constructor with args
    public Freebie(String rewardDesc, int redeemPts, int rewardQty, String useCond, Map<String, Integer> freebieItems) {
        super(generateNewCode(), rewardDesc, "Freebie", redeemPts, rewardQty, useCond);
        this.freebieItems = freebieItems;
    }

    //getter
    public Map<String, Integer> getFreebieItems() {
        return freebieItems;
    }

    public static int getSequence(){
        return sequence;
    }

    //setter
    public void setFreebieItems(Map<String, Integer> freebieItems){
        this.freebieItems = freebieItems;
    }

    //other methods
    @Override
    public String toString(){
        return super.toString() + 
            "\nFreebie Items : " + freebieItems;
    }

    public static boolean isStringParamNotNullorEmpty(String str) {
        return str != null && !str.trim().isEmpty();    //for desc, status, cond
    }

    public static boolean isValuePositive(int num){
        return num > 0;
    }

    public boolean IsItemExist(String itemName) {
        for (String key : this.freebieItems.keySet()) {
            if (key.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public int getFullQuantity(String itemName){
        return this.freebieItems .get(itemName);
    }

    public boolean IsItemQtySufficient(String itemName, int qtyToRemove) {
        int availableQty = getFullQuantity(itemName);
        return availableQty >= qtyToRemove;
    }

    public void addFreebieItems(Map<String, Integer> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            return;
        }
        newItems.forEach((itemName, addQty) -> {
            this.freebieItems.merge(itemName, addQty, Integer::sum);
        });
    }

    public void removeFreebieItems(Map<String, Integer> itemsToRemove) {
        if (itemsToRemove == null || itemsToRemove.isEmpty()) {
            return;
        }

        itemsToRemove.forEach((itemName, removeQty) -> {
            int currentQty = this.freebieItems.get(itemName);
            int remainingQty = currentQty - removeQty;

            if (remainingQty <= 0) {
                this.freebieItems.remove(itemName);
            } else {
                this.freebieItems.put(itemName, remainingQty);
            }
        });
    }

    private static String generateNewCode() {
        sequence++;
        return String.format("F%05d", sequence);
    }
}
