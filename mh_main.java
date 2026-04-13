import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class mh_main {
    private static Scanner scan = new Scanner(System.in);
    private static List<Cinema> cinemaList = new ArrayList<>();
    private static List<Hall> hallList = new ArrayList<>();
    private static List<Movie> movieList = new ArrayList<>();
    private static List<Show> showList = new ArrayList<>();
    private static List<Reward> rewardList = new ArrayList<>();

    public static void main(String[] args) {

        cinemaList.add(new Cinema("DGV Mid Valley Southkey", "Johor", "Johor Bahru", "81100"));
        cinemaList.add(new Cinema("DGV Aman Central", "Kedah", "Alor Setar", "05150"));
        cinemaList.add(new Cinema("DGV AEON Mall Kota Bharu", "Kelantan", "Kota Bharu", "15050"));
        cinemaList.add(new Cinema("DGV AEON Bandaraya Melaka", "Melaka", "Melaka", "75450"));
        cinemaList.add(new Cinema("DGV Palm Mall", "Negeri Sembilan", "Seremban", "70200"));
        cinemaList.add(new Cinema("DGV East Coast Mall", "Pahang", "Kuantan", "25000"));
        cinemaList.add(new Cinema("DGV Gurney Plaza", "Pulau Pinang", "George Town", "10250"));
        cinemaList.add(new Cinema("DGV Ipoh Parade", "Perak", "Ipoh", "30450"));
        cinemaList.add(new Cinema("DGV Kangar Jaya Mall", "Perlis", "Kangar", "01000"));
        cinemaList.add(new Cinema("DGV Suria Sabah", "Sabah", "Kota Kinabalu", "88000"));
        cinemaList.add(new Cinema("DGV CityONE Megamall", "Sarawak", "Kuching", "93350"));
        cinemaList.add(new Cinema("DGV Setia City Mall", "Selangor", "Shah Alam", "40170"));
        cinemaList.add(new Cinema("DGV Paya Bunga Square", "Terengganu", "Kuala Terengganu", "20100"));
        cinemaList.add(new Cinema("DGV Mid Valley Megamall", "Kuala Lumpur", "Kuala Lumpur", "59200"));
        cinemaList.add(new Cinema("DGV IOI City Mall", "Putrajaya", "Putrajaya", "62502"));
        cinemaList.add(new Cinema("DGV Financial Park", "Labuan", "Labuan", "87000"));
        cinemaList.add(new Cinema("DGV Sunway Pyramid", "Selangor", "Petaling Jaya", "47500"));
        cinemaList.add(new Cinema("DGV Pavilion KL", "Kuala Lumpur", "Kuala Lumpur", "55100"));
        cinemaList.add(new Cinema("DGV Queensbay Mall", "Pulau Pinang", "Bayan Lepas", "11900"));
        cinemaList.add(new Cinema("DGV Paradigm Mall JB", "Johor", "Johor Bahru", "81200"));
        cinemaList.add(new Cinema("DGV 1 Utama Shopping Centre", "Selangor", "Petaling Jaya", "47800"));
        cinemaList.add(new Cinema("DGV KSL City Mall", "Johor", "Johor Bahru", "80250"));
        cinemaList.add(new Cinema("DGV AEON Mall Tebrau City", "Johor", "Johor Bahru", "81100"));
        cinemaList.add(new Cinema("DGV MyTOWN Shopping Centre", "Kuala Lumpur", "Kuala Lumpur", "55100"));
        cinemaList.add(new Cinema("DGV Sunway Carnival Mall", "Pulau Pinang", "Seberang Jaya", "13700"));
        cinemaList.add(new Cinema("DGV AEON Mall Bukit Tinggi", "Selangor", "Klang", "41200"));
        cinemaList.add(new Cinema("DGV Vivacity Megamall", "Sarawak", "Kuching", "93350"));
        cinemaList.add(new Cinema("DGV Imago Shopping Mall", "Sabah", "Kota Kinabalu", "88100"));
        cinemaList.add(new Cinema("DGV The Spring", "Sarawak", "Kuching", "93300"));
        cinemaList.add(new Cinema("DGV Toppen Shopping Centre", "Johor", "Johor Bahru", "81100"));

        for (Cinema cinema : cinemaList) {
            for (char letter = 'A'; letter <= 'J'; letter++) {
            String type = (letter <= 'D') ? "Small" : (letter <= 'G' ? "Medium" : "Large");
        
            Hall newHall = new Hall(type);
            
            cinema.getHalls().add(newHall);
            hallList.add(newHall); 
            }
        }

        movieList.add(new Movie("M001", "Inception", "Sci-Fi", "2h 28m", 13, "Now Showing"));
        movieList.add(new Movie("M002", "The Conjuring", "Horror", "1h 52m", 18, "Now Showing"));
        movieList.add(new Movie("M003", "Toy Story 4", "Animation", "1h 40m", 0, "Now Showing"));
        movieList.add(new Movie("M004", "The Dark Knight", "Action", "2h 32m", 13, "Now Showing"));
        movieList.add(new Movie("M005", "Avengers: Endgame", "Action", "3h 01m", 13, "Now Showing"));
        movieList.add(new Movie("M006", "Gladiator", "Action", "2h 35m", 18, "Now Showing"));
        movieList.add(new Movie("M007", "Despicable Me 4", "Animation", "1h 34m", 0, "Now Showing"));
        movieList.add(new Movie("M008", "The Hangover", "Comedy", "1h 40m", 18, "Now Showing"));
        movieList.add(new Movie("M009", "Interstellar", "Sci-Fi", "2h 49m", 13, "Now Showing"));
        movieList.add(new Movie("M010", "Spider-Man: Across the Spider-Verse", "Animation", "2h 20m", 0, "Now Showing"));

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        String[] timeSlots = {"09:00", "12:00", "15:00", "18:00", "21:00", "00:00"}; 

        for (Cinema cinema : cinemaList) {
            for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
                LocalDateTime targetDate = today.plusDays(dayOffset); 
                String dateString = targetDate.format(formatter); 
            
                for (String timeSlot : timeSlots) {
                    for (int j = 0; j < 10; j++) {
                        Hall currentHall = cinema.getHalls().get(j);
                        Movie currentMovie = movieList.get(j);
                    
                        Show newShow = new Show(currentMovie, cinema, currentHall, timeSlot, dateString);
                        showList.add(newShow);
                    }
                }
            }
        }

        rewardList.add(new Discount("10% off on next booking", 100, 10, 10.0));
        rewardList.add(new Discount("15% off on next booking", 150, 10, 15.0));
        rewardList.add(new Discount("20% off on next booking", 200, 10, 20.0));
        rewardList.add(new Discount("25% off on next booking", 250, 10, 25.0));
        rewardList.add(new Discount("50% off on next booking", 500, 10, 50.0));
        rewardList.add(new Freebie("Free Popcorn", 60, 10, Map.of("Popcorn", 2)));
        rewardList.add(new Freebie("Free Hotdog", 35, 10, Map.of("Hotdog", 1)));
        rewardList.add(new Freebie("Free Cola", 40, 10, Map.of("Cola", 2)));
        rewardList.add(new Freebie("Free Sweet Corn", 70, 10, Map.of("Sweet Corn", 3)));
        rewardList.add(new Freebie("Free Nuggets", 30, 10, Map.of("Nuggets", 1)));

        //load data from files at program start
        Movie.loadFromFile();
        Hall.loadFromFile();
        //Reward.loadFromFile(); // Assuming you have a loadFromFile method in the Reward class

        boolean exit = false;

        while (!exit) {//no exit
            Utils.clearScreen(100);
            printMainMenu();

            int choice = Utils.getIntRange(scan, 0, 5);

            switch (choice) {
                case 1:manageMovies();break;
                case 2:manageCinemas();break;
                case 3:manageHalls();break;
                case 4:manageShows();break; 
                case 5:manageRewards();break;
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
        System.out.println("2. Manage Cinemas");
        System.out.println("3. Manage Halls");
        System.out.println("4. Manage Show");
        System.out.println("5. Manage Rewards");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ================== CINEMA ==================
    //---Manages Cinemas---
    private static void manageCinemas() {
        boolean back = false;

        while (!back) {
            Utils.clearScreen(20);
            printCinemaMenu();

            int choice = Utils.getIntRange(scan, 0, 5);

            switch (choice) {
                case 1: addCinema(scan); break;
                case 2: updateCinema(scan); break;
                case 3: deleteCinema(scan); break;
                case 4: searchCinema(scan); break;
                case 0: back = true; break;
            }
        }
    }

    //---Display Cinema Menu---
    private static void printCinemaMenu() {
        System.out.println("--- CINEMA MANAGEMENT ---");
        System.out.println("1. Add New Cinema");
        System.out.println("2. Update Cinema");
        System.out.println("3. Delete Cinema");
        System.out.println("4. Search Cinema");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    public static void addCinema(Scanner scan) {
        Utils.clearScreen(20);
        System.out.println("--- Add New Cinema ---");

        System.out.print("Cinema Name: ");
        String name = scan.nextLine().trim();
        while (name.isEmpty()){
            System.out.println("Cinema name cannot be empty!");
            System.out.print("Cinema Name: ");
            name = scan.nextLine().trim();
        }

        System.out.print("State: ");
        String state = scan.nextLine().trim();
        while (state.isEmpty()){
            System.out.println("State cannot be empty!");
            System.out.print("State: ");
            state = scan.nextLine().trim();
        }

        System.out.print("City: ");
        String city = scan.nextLine().trim();
        while (city.isEmpty()){
            System.out.println("City cannot be empty!");
            System.out.print("City: ");
            city = scan.nextLine().trim();
        }

        System.out.print("Postal Code: ");
        String postalCode = scan.nextLine().trim();
        while (!Cinema.IsPostalCodeFormatValid(postalCode) || postalCode.isEmpty()){
            if (postalCode.isEmpty()) {
                System.out.println("Postal code cannot be empty!");
            } else {
                System.out.println("Invalid postal code format! Postal code must be 5 digits.");
            }
            System.out.print("Postal Code: ");
            postalCode = scan.nextLine().trim();
        }

        boolean cinemaExists = false;
        for (Cinema existingCinema : cinemaList) {
            if (existingCinema.getCinemaName().equalsIgnoreCase(name) && 
                existingCinema.getState().equalsIgnoreCase(state) &&
                existingCinema.getCity().equalsIgnoreCase(city) &&
                existingCinema.getPostalCode().equals(postalCode)) {
                System.out.println("Failed to add: This cinema already exists in the system!");
                cinemaExists = true;
                break;
            }
        }
        if (!cinemaExists) {
            Cinema newCinema = new Cinema(name, state, city, postalCode);
            cinemaList.add(newCinema);
            System.out.println("\nCinema added successfully:");
            System.out.println(newCinema);
        }
        
        Utils.pause();
    }

    public static void updateCinema(Scanner scan) {
        Utils.clearScreen(20);
        System.out.print("Enter Cinema ID to update: ");
        String id = scan.nextLine().trim();

        Cinema cinemaToUpdate = null;
        for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaId().equalsIgnoreCase(id)) {
                cinemaToUpdate = cinema;
                break;
            }
        }

        if (cinemaToUpdate == null) {
            System.out.println("Cinema not found.");
            Utils.pause();
            return;
        }

        System.out.println("\nCurrent details:");
        System.out.println(cinemaToUpdate);

        System.out.print("\nNew Cinema Name (leave blank to keep current): ");
        String name = scan.nextLine().trim();
        if (!name.isEmpty()) {
            cinemaToUpdate.setCinemaName(name);
        }

        System.out.print("New State (leave blank to keep current): ");
        String state = scan.nextLine().trim();
        if (!state.isEmpty()) {
            cinemaToUpdate.setState(state);
        }

        System.out.print("New City (leave blank to keep current): ");
        String city = scan.nextLine().trim();
        if (!city.isEmpty()) {
            cinemaToUpdate.setCity(city);
        }

        System.out.print("New Postal Code (leave blank to keep current): ");
        String postalCode = scan.nextLine().trim();
        if (!postalCode.isEmpty()) {
            while (!Cinema.IsPostalCodeFormatValid(postalCode)){
                System.out.println("Invalid postal code format! Postal code must be 5 digits.");
                System.out.print("New Postal Code: ");
                postalCode = scan.nextLine().trim();
            }
            cinemaToUpdate.setPostalCode(postalCode);
        }

        System.out.println("\nCinema updated successfully:");
        System.out.println(cinemaToUpdate);

         Utils.pause();
    }

    public static void deleteCinema(Scanner scan) {
        Utils.clearScreen(20);
        System.out.print("Enter Cinema ID to delete: ");
        String id = scan.nextLine().trim();

        Cinema cinemaToDelete = null;
        for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaId().equalsIgnoreCase(id)) {
                cinemaToDelete = cinema;
                break;
            }
        }

        if (cinemaToDelete == null) {
            System.out.println("Cinema not found.");
            Utils.pause();
            return;
        }

        System.out.print("Are you sure you want to delete this cinema? (y/n): ");
        String confirm = scan.nextLine().trim();

        if (confirm.equalsIgnoreCase("y")) {
            cinemaList.remove(cinemaToDelete);
            System.out.println("Cinema deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
         Utils.pause();
    }

    public static void searchCinema(Scanner scan) {
        Utils.clearScreen(30);
        System.out.println("Search by cinema name, state, city, or postal code.");
        System.out.print("Enter keyword: ");
        String key = scan.nextLine().trim();

        List<Cinema> results = new ArrayList<>();
        for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaName().toLowerCase().contains(key.toLowerCase()) ||
                cinema.getState().toLowerCase().contains(key.toLowerCase()) ||
                cinema.getCity().toLowerCase().contains(key.toLowerCase()) ||
                cinema.getPostalCode().contains(key)) {
                results.add(cinema);
            }
        }


        if (results.isEmpty()) {
            System.out.println("\nNo results found.");
        } else {
            System.out.println("\nSearch Results:");
            for (Cinema cinema : results) {
                System.out.println(cinema + "\n--------------------------");
            }
        }
        Utils.pause();
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
            System.out.println("Title cannot be empty!");
            System.out.println("Title: ");
            title = scan.nextLine().trim();
        }

        Utils.clearScreen(20);
        String genre = Movie.selectGenre(scan);

        System.out.print("Duration (minutes): ");
        String duration = scan.nextLine().trim();

        System.out.print("Age Rating: ");
        int rating = Utils.getIntRange(scan, 0, 21);

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

    //====================== SHOW ==================
    //---Manages Shows---
    private static void manageShows() {
        boolean back = false;

        while (!back) {
            Utils.clearScreen(20);
            printShowMenu();

            int choice = Utils.getIntRange(scan, 0, 5);

            switch (choice) {
                case 1:addShow();break;
                case 2:updateShow(scan);break;
                case 3:deleteShow(scan);break;
                case 4:searchShow(scan);break;
                case 0:back = true;break;
            }
        }
    }

    public static void printShowMenu() {
        System.out.println("--- SHOW MANAGEMENT ---");
        System.out.println("1. Add New Show");
        System.out.println("2. Update Show");
        System.out.println("3. Delete Show");
        System.out.println("4. Search Show");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addShow() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Show ---");

        System.out.print("Enter Cinema ID: ");
        String cinemaId = scan.nextLine().trim();
        boolean cinemaExists = false;
        for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaId().equalsIgnoreCase(cinemaId)) {
                cinemaExists = true;
                break;
            }
        }
        while (cinemaId.isEmpty() || !cinemaExists) {
            if (cinemaId.isEmpty()) {
                System.out.println("Cinema ID cannot be empty!");
            } else {
                System.out.println("Cinema not found!");
            }
            System.out.print("Enter Cinema ID: ");
            cinemaId = scan.nextLine().trim();
        }

        System.out.print("Enter Hall ID: ");
        String hallId = scan.nextLine().trim();
        boolean hallExists = false;
        if (hallId.isEmpty()) {
            System.out.println("Hall ID cannot be empty!");
             Utils.pause();
            return;
        }
        for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaId().equalsIgnoreCase(cinemaId)) {
                for (Hall hall : cinema.getHalls()) {
                    if (hall.getHallId().equalsIgnoreCase(hallId)) {
                        hallExists = true;
                        break;
                    }
                }
                break;
            }
        }
        while (hallId.isEmpty() || !hallExists) {
            if (hallId.isEmpty()) {
                System.out.println("Hall ID cannot be empty!");
            } else {
                System.out.println("Hall not found in the specified cinema!");
            }
            System.out.print("Enter Hall ID: ");
            hallId = scan.nextLine().trim();
            for (Cinema cinema : cinemaList) {
            if (cinema.getCinemaId().equalsIgnoreCase(cinemaId)) {
                for (Hall hall : cinema.getHalls()) {
                    if (hall.getHallId().equalsIgnoreCase(hallId)) {
                        hallExists = true;
                        break;
                        }
                    }
                break;
                }
            }
        }

        System.out.print("Enter Movie ID: ");
        String movieId = scan.nextLine().trim();
        boolean movieExists = false;
        for (Movie movie : movieList) {
            if (movie.getMovieId().equalsIgnoreCase(movieId)) {
                movieExists = true;
                break;
            }
        }
        while (movieId.isEmpty() || !movieExists) {
            if (movieId.isEmpty()) {
                System.out.println("Movie ID cannot be empty!");
            } else {
                System.out.println("Movie not found!");
            }
            System.out.print("Enter Movie ID: ");
            movieId = scan.nextLine().trim();
        }

        System.out.print("Enter Show Date (yyyy-MM-dd): ");
        String showDate = scan.nextLine().trim();
        while (showDate.isEmpty() || !Show.isDateFormatValid(showDate) || !Show.isStartDateNotInPast(showDate)) {
            if (showDate.isEmpty()) {
                System.out.println("Show date cannot be empty!");
            } else if (!Show.isDateFormatValid(showDate)) {
                System.out.println("Invalid date format! Please enter in yyyy-MM-dd format.");
            } else {
                System.out.println("Show date cannot be in the past!");
            }
            System.out.print("Enter Show Date (yyyy-MM-dd): ");
            showDate = scan.nextLine().trim();
        }

        System.out.print("Enter Show Time (HH:mm): ");
        String showTime = scan.nextLine().trim();
        while (showTime.isEmpty() || !Show.isTimeFormatValid(showTime) || !Show.isStartTimeInFuture(showDate, showTime)) {
            if (showTime.isEmpty()) {
                System.out.println("Show start time cannot be empty!");
            } else if (!Show.isTimeFormatValid(showTime)) {
                System.out.println("Invalid time format! Please enter in HH:mm format.");
            } else {
                System.out.println("Show start time must be in the future!");
            }
            System.out.print("Enter Show Start Time (HH:mm): ");
            showTime = scan.nextLine().trim();
        }

        boolean showExists = false;
        for (Show show : showList) {
            if (show.getCinema().getCinemaId().equalsIgnoreCase(cinemaId) &&
                show.getHall().getHallId().equalsIgnoreCase(hallId) &&
                show.getStartDate().equals(showDate) &&
                show.getStartTime().equals(showTime)) {
                System.out.println("Failed to add: A show is already scheduled in the same cinema, hall, date, and time!");
                showExists = true;
                break;      
                }
        }
        
        if (!showExists) {
            Cinema selectedCinema = null;
            Hall selectedHall = null;
             for (Cinema cinema : cinemaList) {
                if (cinema.getCinemaId().equalsIgnoreCase(cinemaId)) {
                    selectedCinema = cinema;
                    for (Hall hall : cinema.getHalls()) {
                        if (hall.getHallId().equalsIgnoreCase(hallId)) {
                            selectedHall = hall;
                            break;
                        }
                    }
                    break;
                }
            }
            Movie selectedMovie = null;
            for (Movie movie : movieList) {
                if (movie.getMovieId().equalsIgnoreCase(movieId)) {
                    selectedMovie = movie;
                    break;
                }
            }
            Show newShow = new Show(selectedMovie, selectedCinema, selectedHall, showTime, showDate);
            showList.add(newShow);
            System.out.println("\nShow added successfully:");
            System.out.println(newShow);
        }
        Utils.pause();
    }

    public static void updateShow(Scanner scan){
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to update: ");
        String id = scan.nextLine().trim();

        Show showToUpdate = null;
        for (Show show : showList) {
            if (show.getShowId().equalsIgnoreCase(id)) {
                showToUpdate = show;
                break;
            }
        }
        if (showToUpdate == null) {
            System.out.println("Show not found.");
            Utils.pause();
            return;
        }

        System.out.println("\nCurrent details:");
        System.out.println(showToUpdate);
        System.out.print("\nNew Cinema (Enter the new cinema ID to change) (leave blank to keep current): ");
        String cinemaID = scan.nextLine().trim();
        if (!cinemaID.isEmpty()) {
            for (Cinema cinema : cinemaList) {
                if (cinema.getCinemaId().equalsIgnoreCase(cinemaID)) {
                    showToUpdate.setCinema(cinema);
                    break;
                }
            }
        }
        
        System.out.print("\nNew Hall (Enter the new hall ID to change) (leave blank to keep current): ");
        String hallID = scan.nextLine().trim();
        
        Cinema currentCinema = showToUpdate.getCinema(); 
        
        while (!hallID.isEmpty()) {
            boolean hallFound = false;
            
            for (Hall hall : currentCinema.getHalls()) {
                if (hall.getHallId().equalsIgnoreCase(hallID)) {
                    showToUpdate.setHall(hall);
                    hallFound = true;
                    break; 
                }
            }
            
            if (!hallFound) {
                System.out.println("Error: Hall ID '" + hallID + "' does not exist in " + currentCinema.getCinemaName() + ".");
                System.out.print("Please enter a valid Hall ID (or leave blank to keep current): ");
                hallID = scan.nextLine().trim(); 
            }
        }

        System.out.print("\nNew Movie (Enter the new movie ID to change) (leave blank to keep current): ");
        String movieID = scan.nextLine().trim();
        if (!movieID.isEmpty()) {
            for (Movie movie : movieList) {
                if (movie.getMovieId().equalsIgnoreCase(movieID)) {
                    showToUpdate.setMovie(movie);
                    break;
                }
            }
        }

        System.out.print("\nNew Show Date (yyyy-MM-dd) (leave blank to keep current): ");
        String showDate = scan.nextLine().trim();
        if (!showDate.isEmpty()) {
            while (!Show.isDateFormatValid(showDate) || !Show.isStartDateNotInPast(showDate)) {
                if (!Show.isDateFormatValid(showDate)) {
                    System.out.println("Invalid date format! Please enter in yyyy-MM-dd format.");
                } else {
                    System.out.println("Show date cannot be in the past!");
                }
                System.out.print("New Show Date (yyyy-MM-dd) (leave blank to keep current): ");
                showDate = scan.nextLine().trim();
                if (showDate.isEmpty()) {
                    break;
                }
            }
            if (!showDate.isEmpty()) {
                showToUpdate.setStartDate(showDate);
            }
        }

        System.out.print("\nNew Show Time (HH:mm) (leave blank to keep current): ");
        String showTime = scan.nextLine().trim();
        if (!showTime.isEmpty()) {
            while (!Show.isTimeFormatValid(showTime) || !Show.isStartTimeInFuture(showToUpdate.getStartDate(), showTime)) {
                if (!Show.isTimeFormatValid(showTime)) {
                    System.out.println("Invalid time format! Please enter in HH:mm format.");
                } else {
                    System.out.println("Show start time must be in the future!");
                }
                System.out.println("\nNew Show Time (HH:mm) (leave blank to keep current): ");
                showTime = scan.nextLine().trim();
                if (showTime.isEmpty()) {
                    break;
                }
            }
            if (!showTime.isEmpty()) {
                showToUpdate.setStartTime(showTime);
            }
        }
        
        System.out.println("\nShow updated successfully:");
        System.out.println(showToUpdate);

        Utils.pause();
    }

    public static void deleteShow(Scanner scan) {
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to delete: ");
        String id = scan.nextLine().trim();

        Show showToDelete = null;
        for (Show show : showList) {
            if (show.getShowId().equalsIgnoreCase(id)) {
                showToDelete = show;
                break;
            }
        }

        if (showToDelete == null) {
            System.out.println("Show not found.");
            Utils.pause();
            return;
        }

        System.out.print("Are you sure you want to delete this show? (y/n): ");
        String confirm = scan.nextLine().trim();

        if (confirm.equalsIgnoreCase("y")) {
            showList.remove(showToDelete);
            System.out.println("Show deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
         Utils.pause();
    }

    public static void searchShow(Scanner scan) {
        Utils.clearScreen(20);
        String movieId = "";
        String cinemaId = "";
        String date = "";

        while (true) {
            System.out.print("Enter Movie ID to search (e.g., M001): ");
            movieId = scan.nextLine().trim();

            if (movieId.isEmpty()) {
                System.out.println("Please enter a Movie ID.");
            } else if (!movieId.matches("(?i)^M\\d{3}$")) {
                System.out.println("Please enter a valid Movie ID format (e.g., M001).");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter Cinema ID to search (e.g., C001): ");
            cinemaId = scan.nextLine().trim();

            if (cinemaId.isEmpty()) {
                System.out.println("Please enter a Cinema ID.");
            } else if (!cinemaId.matches("(?i)^C\\d{3}$")) {
                System.out.println("Please enter a valid Cinema ID format (e.g., C001).");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter Date to search (YYYY-MM-DD): ");
            date = scan.nextLine().trim();

            if (date.isEmpty()) {
                System.out.println("Please enter a Date.");
            } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                System.out.println("Please enter a valid Date format (YYYY-MM-DD).");
            } else {
                break;
            }
        }

        List<Show> results = new ArrayList<>();
        for (Show show : showList) {
            if (show.getMovie().getMovieId().equalsIgnoreCase(movieId) &&
                show.getCinema().getCinemaId().equalsIgnoreCase(cinemaId) &&
                show.getStartDate().equals(date)) {
                results.add(show);
            }
        }

        if (results.isEmpty()) {
            System.out.println("\nNo shows found for Movie: " + movieId.toUpperCase() + " at Cinema: " + cinemaId.toUpperCase() + " on " + date);
        } else {
            System.out.println("\nSearch results:");
            for (Show show : results) {
                System.out.println(show + "\n--------------------------");
            }
        }
    
        Utils.pause();
    }

    private static void manageRewards() {
        boolean back = false;

        while (!back) {
            Utils.clearScreen(20);
            printRewardMenu();

            int choice = Utils.getIntRange(scan, 0, 4);

            switch (choice) {
                case 1:addReward();break;
                case 2:updateReward();break;
                case 3:deleteReward();break;
                case 4:searchReward();break;
                case 0:back = true;break;
            }
        }
    }

    private static void printRewardMenu() {
        System.out.println("--- REWARD MANAGEMENT ---");
        System.out.println("1. Add New Reward");
        System.out.println("2. Update Reward");
        System.out.println("3. Delete Reward");
        System.out.println("4. Search Reward");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    public static void addReward() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Reward ---");

        System.out.println("Select Reward Type:");
        System.out.println("1. Discount");
        System.out.println("2. Freebie");
        System.out.print("Enter choice: ");
        int type = Utils.getIntRange(scan, 1, 2);

    if (type == 1) {
        System.out.println("\nReward Type: Discount");

        System.out.print("Reward Name: ");
        String name = scan.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Reward name cannot be empty!");
            System.out.print("Reward Name: ");
            name = scan.nextLine().trim();
        }

        System.out.print("Reward Points: ");
        int points = scan.nextInt();
        while (points <= 0) {
            System.out.println("Reward points cannot be zero or negative!");
            System.out.print("Reward Points: ");
            points = scan.nextInt();
        }

        System.out.print("Reward Quantity: ");
        int quantity = scan.nextInt();
        while (quantity <= 0) {
            System.out.println("Reward quantity cannot be zero or negative!");
            System.out.print("Reward Quantity: ");
            quantity = scan.nextInt();
        }

        System.out.print("Discount Percentage: ");
        double discountPercentage = scan.nextDouble();
        while (discountPercentage <= 0 || discountPercentage >= 100) {
            System.out.println("Discount percentage must be between 0 and 100!");
            System.out.print("Discount Percentage: ");
            discountPercentage = scan.nextDouble();
        }
        scan.nextLine(); 

        Reward newReward = new Discount(name, points, quantity, discountPercentage);
        rewardList.add(newReward);
        System.out.println("\nReward added successfully!");
        System.out.println(newReward);

    } else if (type == 2) {
        System.out.println("\nReward Type: Freebie");

        System.out.print("Reward Name: ");
        String name = scan.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Reward name cannot be empty!");
            System.out.print("Reward Name: ");
            name = scan.nextLine().trim();
        }

        System.out.print("Reward Points: ");
        int points = scan.nextInt();
        while (points <= 0) {
            System.out.println("Reward points cannot be zero or negative!");
            System.out.print("Reward Points: ");
            points = scan.nextInt();
        }

        System.out.print("Reward Quantity: ");
        int quantity = scan.nextInt();
        while (quantity <= 0) {
            System.out.println("Reward quantity cannot be zero or negative!");
            System.out.print("Reward Quantity: ");
            quantity = scan.nextInt();
        }
        scan.nextLine(); 

        HashMap<String, Integer> freebieMap = new HashMap<>();
        boolean continueAdding = true;

        while (continueAdding) {
            System.out.print("Enter freebie item (e.g., popcorn): ");
            String freebieItem = scan.nextLine().trim();
            while (freebieItem.isEmpty()) {
                System.out.println("Freebie item cannot be empty!");
                System.out.print("Enter freebie item: ");
                freebieItem = scan.nextLine().trim();
            }

            System.out.print("Reward Quantity for " + freebieItem + ": ");
            int freebieQty = scan.nextInt();
            while (freebieQty <= 0) {
                System.out.println("Reward quantity cannot be zero or negative!");
                System.out.print("Reward Quantity for " + freebieItem + ": ");
                freebieQty = scan.nextInt();
            }
            scan.nextLine(); 

            freebieMap.put(freebieItem, freebieQty);

            System.out.print("Do you want to continue adding more freebie items? (y/n): ");
            String response = scan.nextLine();
            if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
                continueAdding = false;
            }
        }
        
        Reward newReward = new Freebie(name, points, quantity, freebieMap);
        rewardList.add(newReward);
        System.out.println("\nReward added successfully!");
        System.out.println(newReward);
    }
    Utils.pause();
}

public static void updateReward() {
    Utils.clearScreen(20);
    System.out.print("Enter Reward ID to update: ");
    String id = scan.nextLine().trim();

    Reward rewardToUpdate = null;
    for (Reward reward : rewardList) {
        if (reward.getRewardID().equalsIgnoreCase(id)) {
            rewardToUpdate = reward;
            break;
        }
    }

    if (rewardToUpdate == null) {
        System.out.println("Reward not found.");
        Utils.pause();
        return;
    }

    System.out.println("\nCurrent details:");
    System.out.println(rewardToUpdate);

    System.out.print("\nNew Reward Name (leave blank to keep current): ");
    String name = scan.nextLine().trim();
    if (!name.isEmpty()) {
        rewardToUpdate.setRewardDesc(name);
    }

    System.out.print("New Reward Points (leave blank to keep current): ");
    String pointsInput = scan.nextLine().trim();
    if (!pointsInput.isEmpty()) {
        try {
            int points = Integer.parseInt(pointsInput);
            if (points > 0) {
                rewardToUpdate.setRewardPts(points);
            } else {
                System.out.println("Reward points must be positive! Keeping current.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Skipping points update.");
        }
    }

    System.out.print("New Reward Quantity (leave blank to keep current): ");
    String quantityInput = scan.nextLine().trim();
    if (!quantityInput.isEmpty()) {
        try {
            int quantity = Integer.parseInt(quantityInput);
            if (quantity > 0) {
                rewardToUpdate.setRewardQty(quantity);
            } else {
                System.out.println("Reward quantity must be positive! Keeping current.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Skipping quantity update.");
        }
    }

    if (rewardToUpdate.getRewardType().equalsIgnoreCase("Discount")) {
        Discount discountReward = (Discount) rewardToUpdate;
        System.out.print("New Discount Percentage (leave blank to keep current): ");
        String discountInput = scan.nextLine().trim();
        if (!discountInput.isEmpty()) {
            try {
                double discountPercentage = Double.parseDouble(discountInput);
                if (discountPercentage > 0 && discountPercentage < 100) {
                    discountReward.setPercentage(discountPercentage);
                } else {
                    System.out.println("Discount must be between 0-100! Keeping current.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Skipping discount update.");
            }
        }
    } else if (rewardToUpdate.getRewardType().equalsIgnoreCase("Freebie")) {
        Freebie freebieReward = (Freebie) rewardToUpdate;
        Map<String, Integer> freebieMap = new HashMap<>(freebieReward.getFreebieItems());
        while (true) {
            System.out.print("Enter freebie item to update (or press Enter/type 'done' to skip): ");
            String freebieItem = scan.nextLine().trim();

            if (freebieItem.isEmpty() || freebieItem.equalsIgnoreCase("done")) {
                break;
            }

            if (!freebieMap.containsKey(freebieItem)) {
                System.out.println("Freebie item not found! Please enter a valid item.");
                continue;
            }

            System.out.print("New Reward Quantity for " + freebieItem + " (or press Enter to skip): ");
            String qtyInput = scan.nextLine().trim();

            if (!qtyInput.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(qtyInput);
                    if (quantity > 0) {
                        freebieMap.put(freebieItem, quantity);
                    } else {
                        System.out.println("Quantity must be positive! Keeping current.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number! Skipping item quantity update.");
                }
            }
        }
        freebieReward.setFreebieItems(freebieMap);
    }

    System.out.println("\nReward updated successfully:");
    System.out.println(rewardToUpdate);
    Utils.pause();
}

public static void deleteReward() {
    Utils.clearScreen(20);
    System.out.print("Enter Reward ID to delete: ");
    String id = scan.nextLine().trim();

    Reward rewardToDelete = null;
    for (Reward reward : rewardList) {
        if (reward.getRewardID().equalsIgnoreCase(id)) {
            rewardToDelete = reward;
            break;
        }
    }   
    if (rewardToDelete == null) {
        System.out.println("Reward not found.");
        Utils.pause();
        return;
    }       
    System.out.print("Are you sure you want to delete this reward? (y/n): ");
    String confirm = scan.nextLine().trim();
    if (confirm.equalsIgnoreCase("y")) {
        rewardList.remove(rewardToDelete);
        System.out.println("Reward deleted successfully.");
    } else {
        System.out.println("Deletion cancelled.");
    }
     Utils.pause();
}

public static void searchReward() {
    Utils.clearScreen(20);
    System.out.println("Search by reward name.");
    System.out.print("Enter keyword: ");
    String key = scan.nextLine().trim();

    List<Reward> results = new ArrayList<>();
    for (Reward reward : rewardList) {
        if (reward.getRewardDesc().toLowerCase().contains(key.toLowerCase())) {
            results.add(reward);
        }
    }

    if (results.isEmpty()) {
        System.out.println("No rewards found.");
    } else {
        System.out.println("Search results:");
        for (Reward reward : results) {
            System.out.println(reward + "\n--------------------------");
        }
    }

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
        System.out.println("| $$  \\ $$| $$  | $$| $$_____/      | $$  \\ $$| $$  | $$| $$_____/      | $$");
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
