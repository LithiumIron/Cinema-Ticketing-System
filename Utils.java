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

    public static void displayEmptyRow(int... widths) {
        StringBuilder format = new StringBuilder("|");
        Object[] separators = new Object[widths.length];

        for (int i = 0; i < widths.length; i++) {
            format.append(" %-").append(widths[i]).append("s |");
            separators[i] = "-".repeat(widths[i]);
        }
        format.append("\n");

        System.out.printf(format.toString(), separators);
        
    }

    
    public static int getIntRangeWithExit (Scanner sc, int min, int max, String exitKey){ //mh for driverprogram addhall() 19/4/26 2：33pm
		while (true) {
			System.out.printf("Enter choice (%d-%d, or %s to cancel): ", min, max, exitKey);
			String input = sc.nextLine().trim();
			if (input.equalsIgnoreCase(exitKey)){
				return -1;
			}
			try {
				int val = Integer.parseInt(input);
				if (val >= min && val <= max){
					return val;
				}
			}catch (NumberFormatException ignored){}
			System.out.println("Invalid input! Please enter a number between " + min + " and " + max + ".");
		}
	}
}
