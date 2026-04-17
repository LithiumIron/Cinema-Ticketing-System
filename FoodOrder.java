//Object ob1= {new FoodOrder(), new Combo(),...}; can make a array and call the food i think
//look at Practical 6 Q3, its a inheritance question, can take example. 
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FoodOrder {
//data properties
    private static int orderNo=0;
    private int itemNo=0;    
    private double totalPrice=0.00;
    private Map<String, Integer> order = new HashMap<>();  
    
    public FoodOrder(){
    	this(0.0,new HashMap<>(),0);
    }

    public FoodOrder(double totalPrice,Map<String, Integer> order,int itemNo){
    	this.totalPrice=totalPrice;
    	this.order=new HashMap<>(order);
    	this.itemNo=itemNo;
        
    	
    }
    
    //getter
    public static int getOrderNo() {return orderNo;}
    public int getItemNo(){return itemNo;}
    public double getTotalPrice() {return totalPrice;}
       
    public String getOrder() {
        StringBuilder sb = new StringBuilder();
        for (String name : order.keySet()) {
            int qty = order.get(name);
            if (qty > 1) {
                sb.append("(" +name + ") x" + qty + ", ");
            } else {
                sb.append(name + ", ");
            }
        }
        
        String result = sb.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }//method end
    
    //setter
    public static void setOrderNo(int orderNo) {FoodOrder.orderNo = orderNo;}
    public void setItemNo(int itemNo){this.itemNo=itemNo;}
    public void setTotalPrice(double totalPrice) {this.totalPrice = totalPrice;}

    public void setOrder(HashMap<String, Integer> order) {     //to reset & make a new orders
        this.order.clear();
        this.order.putAll(order);
       	this.itemNo=this.order.size();
    }

    //------------other methods------------
    public void deleteOrder(){
        this.order.clear();
        this.totalPrice=0.0;
        this.itemNo=this.order.size();
        System.out.println("Order cancelled!");
    }
    public void addItem(String name, double price) {
        // Increase quantity if already exists
        if (order.containsKey(name)) {
            order.put(name, order.get(name) + 1);
        } else {
            order.put(name, 1);
        }
        totalPrice += price;
        itemNo++;
    }
    //size interface
    public int displayFoodSize(){
        Scanner scan=new Scanner(System.in);
        System.out.println("\n=================================");
        System.out.println("|       Select Item Size        |");
        System.out.println("+===============================+");
        System.out.println("|   1. Large                    |");
        System.out.println("|   2. Regular                  |");
        System.out.println("|   3. Go Back                  |");
        System.out.println("=================================");
        System.out.print("Enter choice: ");

        return scan.nextInt();

    }

    //cost size
    public double largeCost(double price){
    	return price*1.5;
    }//double end


    public void saveOrder() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("orders.txt", true))) {
            writer.println("Order No: " + orderNo);
            writer.println("Order: " + getOrder());
            writer.println("Total: RM" + totalPrice);
            writer.println("---------------------------------\n\n");
        } 
        catch (IOException e) {
            System.out.println("Saving order failed!");
        }
    }

    public static void loadOrderNo() {
        File file = new File("orders.txt");
        if (!file.exists()) {
            FoodOrder.orderNo = 1;
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lastOrder = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Order No: ")) {
                    String numStr = line.substring(10).trim();
                    lastOrder = Integer.parseInt(numStr);
                }
            }
            FoodOrder.orderNo = lastOrder + 1;
        } catch (IOException e) {
            FoodOrder.orderNo = 1;
        }
    }
    
    
    public String toString(){
        return String.format("Order No: %d\nOrder: %s\nCost: RM%.2f", 
                            orderNo, getOrder(), totalPrice);
    
    }  
    public boolean equals(Object obj){
        if(obj instanceof FoodOrder){
            FoodOrder fo= (FoodOrder)obj;
            return this.orderNo.equals(fo.orderNo);
        }
        else
            return false;
    }    
    
}//class end
