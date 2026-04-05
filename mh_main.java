import java.util.Scanner;
import java.util.List;

public class mh_main {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        //load data from files at program start
        Movie.loadFromFile();
        Hall.loadFromFile();

        boolean exit = false;

        while (!exit) {//no exit
            Utils.clearScreen(100);
            printMainMenu();

            int choice = Utils.getIntRange(scan, 0, 2);

            switch (choice) {
                case 1:manageMovies();break;
                case 2:manageHalls();break;
                case 0:{
                    exit = true;
                    showExitScreen();
                }break;
            }
        }
        scan.close();
    }

    // ================== MAIN MENU ==================

    private static void printMainMenu() {
        System.out.println("========================================");
        System.out.println("   CINEMA MANAGEMENT SYSTEM - STAFF    ");
        System.out.println("========================================");
        System.out.println("1. Manage Movies");
        System.out.println("2. Manage Halls");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ================== MOVIE ==================

    //---Manages Movies---
    private static void manageMovies() {
        boolean back = false;

        while (!back) {
            Utils.clearScreen(20);
            printMovieMenu();

            int choice = Utils.getIntRange(scan, 0, 6);

            switch (choice) {
                case 1:addMovie();break;
                case 2:Movie.displayMoviesMenu(scan);break;
                case 3:updateMovie();break;
                case 4:deleteMovie();break;
                case 5:searchMovie();break;
                case 6:{
                    Utils.clearScreen(20);
                    Movie.displayMovieSummary();
                     Utils.pause();
                }break;
                case 0:back = true;break;
            }
        }
    }

    //---Display Movie Menu---
    private static void printMovieMenu() {
        System.out.println("--- MOVIE MANAGEMENT ---");
        System.out.println("1. Add New Movie");
        System.out.println("2. View Movies (by genre / all)");
        System.out.println("3. Update Movie");
        System.out.println("4. Delete Movie");
        System.out.println("5. Search Movie");
        System.out.println("6. View Movie Summary (by genre)");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    //---add movie---
    private static void addMovie() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Movie ---");

        System.out.print("Title: ");
        String title = scan.nextLine().trim();
        while (title.isEmpty()){
            System.out.println("Title cannot be emplty!");
            System.out.println("Title: ");
            title = scan.nextLine().trim();
        }

        Utils.clearScreen(20);
        String genre = Movie.selectGenre(scan);

        System.out.print("Duration (minutes): ");
        int duration = Utils.getIntRange(scan, 1, 500);

        System.out.print("Age Rating: ");
        int rating = Utils.getIntRange(scan, 0, 100);

        String status = Movie.selectStatus(scan);

        Movie m = new Movie(title, genre, duration, rating, status);
        Movie.addMovie(m);

        System.out.println("\nMovie Added:");
        System.out.println(m);

         Utils.pause();
    }

    //---delete movie---
    private static void deleteMovie() {
        Utils.clearScreen(20);
        Movie.displayAllMovies();

        System.out.print("Enter Movie ID to delete: ");
        String id = scan.nextLine().trim();

        System.out.print("Are you sure? (y/n): ");
        String confirm = scan.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            Movie.deleteMovie(id);
        } else {
            System.out.println("Deletion cancelled.");
        }

         Utils.pause();
    }

    //---update movie---
    private static void updateMovie() {
        System.out.print("Enter Movie ID: ");
        String id = scan.nextLine();
        Movie.updateMovie(id, scan);
    }

    //---search movie---
    private static void searchMovie() {
    	Utils.clearScreen(40);
        System.out.println("Search by movie title.\nExample: Minion, ion, mi, i");
        System.out.print("\nEnter keyword: ");
        String key = scan.nextLine();

        List<Movie> results = Movie.findMoviesByTitle(key);

        if (results.isEmpty()) {
            System.out.println("No results.");
        } else {
            for (Movie m : results) {
                System.out.println(m);
            }
        }

         Utils.pause();
    }

    // ================== HALL ==================

    //---Manages Halls---
    private static void manageHalls() {
        boolean back = false;

        while (!back) {
            Utils.clearScreen(20);
            printHallMenu();

            int choice = Utils.getIntRange(scan, 0, 5);

            switch (choice) {
                case 1:
                	addHall();break;
                case 2:
                	Utils.clearScreen(100);
                	Hall.displayHallSummary(scan);break;
                case 3:
                	Hall.deleteHallPrompt(scan);break;
                case 4:
                	Hall.displaySeatMapMenu(scan);break;
                case 5:
                	Hall.displayAllHallsDetailed(scan);break;
                case 0:
                	back = true;break;
            }
        }
    }

    //---Display Hall Menu---
    private static void printHallMenu() {
        System.out.println("--- HALL MANAGEMENT ---");
        System.out.println("1. Add New Hall");
        System.out.println("2. View Hall Summary");
        System.out.println("3. Delete Hall");
        System.out.println("4. View Hall Seat Map");
        System.out.println("5. View Hall Details");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    //---add hall---
    private static void addHall() {
        Utils.clearScreen(20);

        System.out.println("--- Select Hall Type ---");
        System.out.println("1. Small");
        System.out.println("2. Medium");
        System.out.println("3. Large");
        System.out.print("Enter choice: ");

        int type = Utils.getIntRange(scan, 1, 3);
        Hall.addHallByType(type);

         Utils.pause();
    }

    // ================== EXIT ==================
    private static void showExitScreen() {
        Utils.clearScreen(100);

        System.out.println(" /$$$$$$$                            /$$$$$$$                            /$$");
        System.out.println("| $$__  $$                          | $$__  $$                          | $$");
        System.out.println("| $$  \\ $$ /$$   /$$  /$$$$$$       | $$  \\ $$ /$$   /$$  /$$$$$$       | $$");
        System.out.println("| $$$$$$$ | $$  | $$ /$$__  $$      | $$$$$$$ | $$  | $$ /$$__  $$      | $$");
        System.out.println("| $$__  $$| $$  | $$| $$$$$$$$      | $$__  $$| $$  | $$| $$$$$$$$      |__/");
        System.out.println("| $$  \\ $$| $$  | $$| $$_____/      | $$  \\ $$| $$  | $$| $$_____/");
        System.out.println("| $$$$$$$/|  $$$$$$$|  $$$$$$$      | $$$$$$$/|  $$$$$$$|  $$$$$$$       /$$");
        System.out.println("|_______/  \\____  $$ \\_______/      |_______/  \\____  $$ \\_______/      |__/");
        System.out.println("           /$$  | $$                           /$$  | $$");
        System.out.println("          |  $$$$$$/                          |  $$$$$$/");
        System.out.println("           \\______/                            \\______/");

        System.out.println("\n------------------------------------------");
        System.out.println("   Thank you for using the system. Goodbye!");
        System.out.println("------------------------------------------");

        System.out.println("\nProcess completed.");
    }

    
}
