import java.util.Scanner;

public class Utils {
    public static Scanner sc=new Scanner(System.in);
    //clear screen
    public static void clearScreen(int lines) {
        for (int i = 0; i < lines; i++) System.out.println();
    }
  
    //pause
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    //check Int input
    public static int getInt(Scanner sc) {
        while (!sc.hasNextInt()) {//if invalid then retry until valid input
            System.out.print("Please enter a number: ");
            sc.next();
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    //chekc Int input range
    public static int getIntRange(Scanner sc, int min, int max) {
        while (true) {
            int v = getInt(sc);
            if (v >= min && v <= max) return v;
            System.out.printf("Please enter a number between %d and %d: ", min, max);
        }
    }

    //truncate
    public static String truncate(String s, int len) {//shorten string if it is too loong
        if (s.length() <= len) return s;
        return s.substring(0, len - 3) + "...";//truncate string and add"..." if it exceeds length
    }

    public static void displayLine() {
        System.out.println("-".repeat(85));
    }

    public static void displayInnerLine() {
        System.out.println("|" + "-".repeat(83) + "|");
    }

    public static void displayEmptyRow() {
        System.out.printf("| %-4s | %-30s | %-15s | %-23s |\n", 
                          "-".repeat(4), 
                          "-".repeat(30), 
                          "-".repeat(15), 
                          "-".repeat(23));
    }
}
