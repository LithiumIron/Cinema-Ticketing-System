import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Cinemadriver {

    // ================================================================
    // SHARED STATE
    // ================================================================
    private static final DataStorage storage = new DataStorage();
    private static final Scanner      scan    = new Scanner(System.in);

    private static User   currentUser   = null;
    private static int    reportCounter = 1;

    private static final List<Cinema>      cinemaList          = new ArrayList<>();
    private static final List<Movie>       movieList           = Movie.getAllMovies();
    private static final List<Show>        showList            = new ArrayList<>();
    private static final List<Ticket>      ticketList          = new ArrayList<>();
    private static final List<Reward>      rewardList          = new ArrayList<>();
    private static final List<Redemption>  redemptionList      = new ArrayList<>();
    private static final Map<String, List<String>> customerRedemptions = new HashMap<>();

    // ================================================================
    // ENTRY POINT
    // ================================================================
    public static void main(String[] args) {

        // Initialize seat halls
        Seat.initializeAllHalls();
        
        // Load file-based data FIRST before any init that depends on them
        Movie.loadFromFile();
        //Hall.loadFromFile(); --removed: halls are created with cinemas each run
        
        initializeCinemas();
        initializeHallsforCinemas();
        initializeMovie();
        initializeTicket();
        initializeShow();
        initializeReward();

        printBanner();
        storage.loadAll();

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showMainMenu();
            } else if (currentUser instanceof Customer) {
                showCustomerMenu((Customer) currentUser);
            } else if (currentUser instanceof Staff) {
                showStaffMenu((Staff) currentUser);
            }
        }

        System.out.println("\n  Thank you for using the Cinema System. Goodbye!\n");
    }

    // ================================================================
    // MAIN MENU
    // ================================================================
    private static boolean showMainMenu() {
        System.out.println("\n  +------------------------------+");
        System.out.println("  |         MAIN MENU            |");
        System.out.println("  |------------------------------|");
        System.out.println("  |  [1] Login                   |");
        System.out.println("  |  [2] Register as Customer    |");
        System.out.println("  |  [3] Register as Staff       |");
        System.out.println("  |  [0] Exit                    |");
        System.out.println("  +------------------------------+");

        int choice = readInt("  Select option: ", 0, 3);
        switch (choice) {
            case 1: handleLogin();            break;
            case 2: handleRegisterCustomer(); break;
            case 3: handleRegisterStaff();    break;
            case 0: return false;
        }
        return true;
    }

    // ================================================================
    // LOGIN
    // ================================================================
    private static void handleLogin() {
        System.out.println("\n  --- Login ---");
        try {
            System.out.print("  Username : ");
            String username = scan.nextLine().trim();

            System.out.print("  Password : ");
            String password = scan.nextLine().trim();

            if (username.isEmpty() || password.isEmpty())
                throw new IllegalArgumentException("Username and password cannot be empty.");

            User user = storage.loginLookup(username, password);
            if (user == null)
                throw new IllegalArgumentException("Invalid username or password.");

            currentUser = user;
            System.out.println("\n  Login successful! Welcome, " + user.getDisplayName());

        } catch (IllegalArgumentException e) {
            System.out.println("  Login failed - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error during login: " + e.getMessage());
        }
    }

    // ================================================================
    // REGISTER - CUSTOMER
    // ================================================================
    private static void handleRegisterCustomer() {
        System.out.println("\n  --- Register New Customer ---");
        try {
            System.out.print("  Choose username : ");
            String username = scan.nextLine().trim();

            if (username.isEmpty())
                throw new IllegalArgumentException("Username cannot be empty.");
            if (storage.findByUsername(username) != null)
                throw new IllegalArgumentException("Username already taken. Please choose another.");

            System.out.print("  Password (min 6 chars) : ");
            String password = scan.nextLine().trim();

            if (password.length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters.");

            String   customerID  = storage.generateCustomerID();
            Customer newCustomer = new Customer(customerID, username, password);
            newCustomer.setRewardPts(100);
            initializeRedemption(newCustomer);

            if (storage.addCustomer(newCustomer)) {
                System.out.println("\n  Registration successful!");
                System.out.println("  Your Customer ID : " + customerID);
                System.out.println("  You can now log in with your username and password.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("  Registration error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }

    // ================================================================
    // REGISTER - STAFF
    // ================================================================
    private static void handleRegisterStaff() {
        System.out.println("\n  --- Register New Staff ---");
        try {
            System.out.print("  Full name       : ");
            String staffName = scan.nextLine().trim();

            if (staffName.isEmpty())
                throw new IllegalArgumentException("Staff name cannot be empty.");

            System.out.print("  Choose username : ");
            String username = scan.nextLine().trim();

            if (username.isEmpty())
                throw new IllegalArgumentException("Username cannot be empty.");
            if (storage.findByUsername(username) != null)
                throw new IllegalArgumentException("Username already taken. Please choose another.");

            System.out.print("  Password (min 6 chars) : ");
            String password = scan.nextLine().trim();

            if (password.length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters.");

            String staffID  = storage.generateStaffID();
            Staff  newStaff = new Staff(staffID, staffName, username, password);

            if (storage.addStaff(newStaff)) {
                System.out.println("\n  Staff registration successful!");
                System.out.println("  Your Staff ID : " + staffID);
                System.out.println("  You can now log in with your username and password.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("  Registration error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }

    // ================================================================
    // CUSTOMER MENU
    // ================================================================

    private static void displayMenu() {
        System.out.println("\n  ================================");
        System.out.println("  |       CUSTOMER MENU          |");
        System.out.println("  +===============================");
        System.out.println("  |  [1] View My Profile         |");
        System.out.println("  |  [2] Delete My Account       |");
        System.out.println("  |  [3] Booking                 |");
        System.out.println("  |  [4] Redeem Reward           |");
        System.out.println("  |  [0] Logout                  |");
        System.out.println("  ================================");
    }

    private static void showCustomerMenu(Customer customer) {
        customer.displayMenu();
        int choice = readInt("  Select option: ", 0, 4);

        switch (choice) {
            case 1: viewProfile(customer); break;
            case 2: handleDeleteOwnAccount(customer); break;
            case 3: cinemaLoop(customer, scan); break;
            case 4: redeemReward(customer, scan); break;
            case 0:
                customer.logout();
                currentUser = null;
                break;
        }
    }

    private static void viewProfile(User user) {
        System.out.println("\n  --- My Profile ---");
        System.out.println("  " + user);
    }

    private static void handleDeleteOwnAccount(Customer customer) {
        System.out.println("\n  --- Delete Account ---");
        System.out.println("  WARNING: This will permanently delete your account.");
        System.out.print("  Type YES to confirm: ");
        String confirm = scan.nextLine().trim();

        if (confirm.equals("YES")) {
            storage.deleteCustomer(customer.getCustomerID());
            System.out.println("  Your account has been deleted.");
            currentUser = null;
        } else {
            System.out.println("  Account deletion cancelled.");
        }
    }

    // ================================================================
    // STAFF MENU
    // ================================================================
    private static void showStaffMenu(Staff staff) {
        staff.displayMenu();
        int choice = readInt("  Select option: ", 0, 6);

        switch (choice) {
            case 1: viewProfile(staff);           break;
            case 2: handleUpdateStaffName(staff); break;
            case 3: handleViewAllUsers();         break;
            case 4: showReportMenu();             break;
            case 5: handleDeleteCustomer();       break;
            case 6: showManagementMenu();         break;
            case 0:
                staff.logout();
                currentUser = null;
                break;
        }
    }

    private static void handleUpdateStaffName(Staff staff) {
        System.out.println("\n  --- Update Name ---");
        try {
            System.out.print("  New full name: ");
            String newName = scan.nextLine().trim();

            if (newName.isEmpty())
                throw new IllegalArgumentException("Name cannot be empty.");

            staff.setStaffName(newName);
            storage.updateStaffName(staff.getStaffID(), newName);
            System.out.println("  Name updated to: " + newName);

        } catch (IllegalArgumentException e) {
            System.out.println("  Error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }

    private static void handleViewAllUsers() {
        storage.displayAllCustomers();
        storage.displayAllStaff();
    }

    private static void handleDeleteCustomer() {
        System.out.println("\n  --- Delete Customer ---");
        storage.displayAllCustomers();

        if (storage.getAllCustomers().isEmpty()) return;

        try {
            System.out.print("  Enter Customer ID to delete: ");
            String id = scan.nextLine().trim();

            if (id.isEmpty())
                throw new IllegalArgumentException("Customer ID cannot be empty.");

            Customer target = storage.findCustomerByID(id);
            if (target == null)
                throw new IllegalArgumentException("Customer not found: " + id);

            System.out.println("  Found: " + target);
            System.out.print("  Type YES to confirm deletion: ");
            String confirm = scan.nextLine().trim();

            if (confirm.equals("YES")) {
                storage.deleteCustomer(id);
                System.out.println("  Customer deleted successfully.");
            } else {
                System.out.println("  Deletion cancelled.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("  Error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error: " + e.getMessage());
        }
    }

    // ================================================================
    // REPORT MENU
    // ================================================================
    private static void showReportMenu() {
        System.out.println("\n  --- Reports ---");
        System.out.println("  [1] Customer Summary");
        System.out.println("  [2] Staff Summary");
        System.out.println("  [3] Top Reward Points");
        System.out.println("  [4] System Overview");
        System.out.println("  [5] Booking Summary");
        System.out.println("  [6] Peak Hours");
        System.out.println("  [0] Back");

        int choice = readInt("  Select report: ", 0, 6);
        if (choice == 0) return;

        String reportID = "RPT" + String.format("%03d", reportCounter++);

        try {
            switch (choice) {
                case 1: new Report(reportID, "CUSTOMER_SUMMARY").generateCustomerSummaryReport(storage); break;
                case 2: new Report(reportID, "STAFF_SUMMARY").generateStaffSummaryReport(storage);       break;
                case 3: new Report(reportID, "TOP_REWARDS").generateTopRewardReport(storage);            break;
                case 4: new Report(reportID, "SYSTEM_OVERVIEW").generateSystemOverviewReport(storage);   break;
                case 5: new Report(reportID, "BOOKING_SUMMARY").generateBookingSummaryReport(storage);   break;
                case 6: new Report(reportID, "PEAK_HOURS").generatePeakHourReport(storage);              break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("  Report error - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  Unexpected error generating report: " + e.getMessage());
        }
    }

    // ================================================================
    // STAFF MANAGEMENT MENU (from mh_main)
    // ================================================================
    private static void showManagementMenu() {
        boolean back = false;
        while (!back) {
            Utils.clearScreen(20);
            System.out.println("========================================");
            System.out.println("   CINEMA MANAGEMENT SYSTEM - STAFF    ");
            System.out.println("========================================");
            System.out.println("1. Manage Movies");
            System.out.println("2. Manage Cinemas");
            System.out.println("3. Manage Halls");
            System.out.println("4. Manage Shows");
            System.out.println("5. Manage Rewards");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 5);
            switch (choice) {
                case 1: manageMovies();  break;
                case 2: manageCinemas(); break;
                case 3: manageHalls();   break;
                case 4: manageShows();   break;
                case 5: manageRewards(); break;
                case 0: back = true;     break;
            }
        }
    }

    // ================================================================
    // CINEMA MANAGEMENT
    // ================================================================
    private static void manageCinemas() {
        boolean back = false;
        while (!back) {
            Utils.clearScreen(20);
            System.out.println("--- CINEMA MANAGEMENT ---");
            System.out.println("1. Add New Cinema");
            System.out.println("2. Update Cinema");
            System.out.println("3. Delete Cinema");
            System.out.println("4. Search Cinema");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 4);
            switch (choice) {
                case 1: addCinema();    break;
                case 2: updateCinema(); break;
                case 3: deleteCinema(); break;
                case 4: searchCinema(); break;
                case 0: back = true;    break;
            }
        }
    }

    private static void addCinema() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Cinema ---");

        System.out.print("Cinema Name: ");
        String name = scan.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Cinema name cannot be empty!");
            System.out.print("Cinema Name: ");
            name = scan.nextLine().trim();
        }

        System.out.print("State: ");
        String state = scan.nextLine().trim();
        while (state.isEmpty()) {
            System.out.println("State cannot be empty!");
            System.out.print("State: ");
            state = scan.nextLine().trim();
        }

        System.out.print("City: ");
        String city = scan.nextLine().trim();
        while (city.isEmpty()) {
            System.out.println("City cannot be empty!");
            System.out.print("City: ");
            city = scan.nextLine().trim();
        }

        System.out.print("Postal Code: ");
        String postalCode = scan.nextLine().trim();
        while (postalCode.isEmpty() || !Cinema.IsPostalCodeFormatValid(postalCode)) {
            System.out.println(postalCode.isEmpty()
                ? "Postal code cannot be empty!"
                : "Invalid postal code format! Postal code must be 5 digits.");
            System.out.print("Postal Code: ");
            postalCode = scan.nextLine().trim();
        }

        for (Cinema c : cinemaList) {
            if (c.getCinemaName().equalsIgnoreCase(name) &&
                c.getState().equalsIgnoreCase(state)     &&
                c.getCity().equalsIgnoreCase(city)       &&
                c.getPostalCode().equals(postalCode)) {
                System.out.println("Failed to add: This cinema already exists in the system!");
                Utils.pause();
                return;
            }
        }

        Cinema newCinema = new Cinema(name, state, city, postalCode);
        cinemaList.add(newCinema);
        System.out.println("\nCinema added successfully:\n" + newCinema);
        Utils.pause();
    }

    private static void updateCinema() {
        Utils.clearScreen(20);
        System.out.print("Enter Cinema ID to update: ");
        String id = scan.nextLine().trim();

        Cinema c = findCinemaById(id);
        if (c == null) { System.out.println("Cinema not found."); Utils.pause(); return; }

        System.out.println("\nCurrent details:\n" + c);

        System.out.print("\nNew Cinema Name (leave blank to keep current): ");
        String name = scan.nextLine().trim();
        if (!name.isEmpty()) c.setCinemaName(name);

        System.out.print("New State (leave blank to keep current): ");
        String state = scan.nextLine().trim();
        if (!state.isEmpty()) c.setState(state);

        System.out.print("New City (leave blank to keep current): ");
        String city = scan.nextLine().trim();
        if (!city.isEmpty()) c.setCity(city);

        System.out.print("New Postal Code (leave blank to keep current): ");
        String postalCode = scan.nextLine().trim();
        if (!postalCode.isEmpty()) {
            while (!Cinema.IsPostalCodeFormatValid(postalCode)) {
                System.out.println("Invalid postal code format! Postal code must be 5 digits.");
                System.out.print("New Postal Code: ");
                postalCode = scan.nextLine().trim();
            }
            c.setPostalCode(postalCode);
        }

        System.out.println("\nCinema updated successfully:\n" + c);
        Utils.pause();
    }

    private static void deleteCinema() {
        Utils.clearScreen(20);
        System.out.print("Enter Cinema ID to delete: ");
        String id = scan.nextLine().trim();

        Cinema c = findCinemaById(id);
        if (c == null) { System.out.println("Cinema not found."); Utils.pause(); return; }

        System.out.print("Are you sure you want to delete this cinema? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            final String deletedId = c.getCinemaId();
            cinemaList.remove(c);
            // Remove all shows for this cinema so customers no longer see it
            showList.removeIf(s -> s.getCinema().getCinemaId().equalsIgnoreCase(deletedId));
            System.out.println("Cinema deleted successfully. Related shows also removed.");
        }
        else 
            System.out.println("Deletion cancelled.");
        Utils.pause();
    }

    private static void searchCinema() {
        Utils.clearScreen(20);
        System.out.println("Search by cinema name, state, city, or postal code.");
        System.out.print("Enter keyword: ");
        String key = scan.nextLine().trim();

        List<Cinema> results = new ArrayList<>();
        for (Cinema c : cinemaList) {
            if (c.getCinemaName().toLowerCase().contains(key.toLowerCase()) ||
                c.getState().toLowerCase().contains(key.toLowerCase())      ||
                c.getCity().toLowerCase().contains(key.toLowerCase())       ||
                c.getPostalCode().contains(key))
                results.add(c);
        }

        if (results.isEmpty()) System.out.println("\nNo results found.");
        else for (Cinema c : results) System.out.println(c + "\n--------------------------");
        Utils.pause();
    }

    private static Cinema findCinemaById(String id) {
        for (Cinema c : cinemaList)
            if (c.getCinemaId().equalsIgnoreCase(id)) return c;
        return null;
    }

    // ================================================================
    // MOVIE MANAGEMENT
    // ================================================================
	private static void manageMovies() {
	    boolean back = false;
	    while (!back) {
	        Utils.clearScreen(20);
	        System.out.println("--- MOVIE MANAGEMENT ---");
	        System.out.println("1. Add New Movie");
	        System.out.println("2. View Movies (by genre / all)");
	        System.out.println("3. Update Movie");
	        System.out.println("4. Delete Movie");
	        System.out.println("5. Search Movie");
	        System.out.println("6. View Movie Summary (by genre)");
	        System.out.println("0. Back to Main Menu");
	        System.out.print("\nEnter your choice (0-6): ");
	
	        int choice = Utils.getIntRange(scan, 0, 6);
	        switch (choice) {
	            case 1: addMovie();   			break;
	            case 2: viewMoviesMenu();       break;
	            case 3: updateMovie();			break;
	            case 4: deleteMovie();			break;
	            case 5: searchMovie();			break;
	            case 6: showMovieSummary();     break;
	            case 0: back = true;            break;
	        }
	    }
	}
	
	// ---------- Add Movie ----------
	private static void addMovie() {
	    Utils.clearScreen(20);
	    System.out.println("--- Add New Movie ---");
	
	    String title = Utils.promptStringWithCancel(scan, "Title", "X");
	    if (title == null) {
	        System.out.println("Add movie cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Integer genreIdx = Utils.promptSelectionWithCancel(scan, "Select Genre", Movie.VALID_GENRES, "X");
	    if (genreIdx == null) {
	        System.out.println("Add movie cancelled.");
	        Utils.pause();
	        return;
	    }
	    String genre = Movie.VALID_GENRES[genreIdx - 1];
	
	    String duration = Utils.promptStringWithCancel(scan, "Duration (e.g., 2h 30m)", "X");
	    if (duration == null) {
	        System.out.println("Add movie cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Integer rating = Utils.promptIntWithCancel(scan, "Age Rating", 0, 21, "X");
	    if (rating == null) {
	        System.out.println("Add movie cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Integer statusIdx = Utils.promptSelectionWithCancel(scan, "Select Status", Movie.VALID_STATUSES, "X");
	    if (statusIdx == null) {
	        System.out.println("Add movie cancelled.");
	        Utils.pause();
	        return;
	    }
	    String status = Movie.VALID_STATUSES[statusIdx - 1];
	
	    Movie newMovie = new Movie(title, genre, duration, rating, status);
	    Movie.addMovie(newMovie);
	    System.out.println("\n[+] Movie added successfully!");
	    System.out.println(newMovie);
	    System.out.println("\n[Note] To make this movie visible to customers, add a show for it.");
	    Utils.pause();
	}
	
	// ---------- View Movies Menu (by genre / all) ----------
	private static void viewMoviesMenu() {
	    while (true) {
	        Set<String> genres = Movie.getAllGenres();
	        List<String> genreList = new ArrayList<>(genres);
	
	        Utils.clearScreen(20);
	        System.out.println("========================================");
	        System.out.println("         DISPLAY MOVIES MENU           ");
	        System.out.println("========================================");
	        int i = 1;
	        for (String g : genreList) {
	            System.out.println(i++ + ". " + g);
	        }
	        System.out.println(i + ". All Movies");
	        System.out.println("0. Back");
	        System.out.print("Enter your choice (0-" + i + "): ");
	
	        int choice = Utils.getIntRange(scan, 0, i);
	        if (choice == 0) return;
	
	        List<Movie> list;
	        String title;
	        if (choice == i) {
	            list = Movie.getAllMovies();
	            title = "All Movies";
	        } else {
	            String selectedGenre = genreList.get(choice - 1);
	            list = Movie.filterByGenreList(selectedGenre);
	            title = selectedGenre + " Movies";
	        }
	
	        Utils.clearScreen(80);
	        Movie.printTable(list, title);
	        Utils.pause();
	    }
	}
	
	// ---------- Update Movie ----------
	private static void updateMovie() {
	    System.out.print("Enter Movie ID to update (or X to cancel): ");
	    String id = scan.nextLine().trim();
	    if (id.equalsIgnoreCase("X")) {
	        System.out.println("Update cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Movie movie = Movie.findMovieById(id);
	    if (movie == null) {
	        System.out.println("[!] Movie ID not found.");
	        Utils.pause();
	        return;
	    }
	
	    Utils.clearScreen(20);
	    System.out.println("Current movie info:");
	    System.out.println("---------------------------------");
	    System.out.printf("|ID      : %-21s|\n", movie.getMovieId());
	    System.out.printf("|Title   : %-21s|\n", movie.getTitle());
	    System.out.printf("|Genre   : %-21s|\n", movie.getGenre());
	    System.out.printf("|Duration: %-21s|\n", movie.getDuration());
	    System.out.printf("|Rating  : %-21d|\n", movie.getAgeRating());
	    System.out.printf("|Status  : %-21s|\n", movie.getStatus());
	    System.out.println("---------------------------------");
	
	    Integer statusIdx = Utils.promptSelectionWithCancel(scan, "Select New Status", Movie.VALID_STATUSES, "X");
	    if (statusIdx == null) {
	        System.out.println("Update cancelled.");
	        Utils.pause();
	        return;
	    }
	    String newStatus = Movie.VALID_STATUSES[statusIdx - 1];
	    Movie.updateMovieStatus(id, newStatus);
	    Utils.pause();
	}
	
	// ---------- Delete Movie ----------
	private static void deleteMovie() {
	    Movie.displayAllMovies();
	    System.out.print("Enter Movie ID to delete (or X to cancel): ");
	    String id = scan.nextLine().trim();
	    if (id.equalsIgnoreCase("X")) {
	        System.out.println("Deletion cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Movie movie = Movie.findMovieById(id);
	    if (movie == null) {
	        System.out.println("[!] Movie ID not found.");
	        Utils.pause();
	        return;
	    }
	
	    System.out.print("Are you sure you want to delete \"" + movie.getTitle() + "\"? (Y/N): ");
	    String confirm = scan.nextLine().trim();
	    if (confirm.equalsIgnoreCase("Y")) {
	        Movie.deleteMovie(id);
	        // Remove related shows
	        showList.removeIf(s -> s.getMovie().getMovieId().equalsIgnoreCase(id));
	        System.out.println("Movie deleted successfully. Related shows also removed.");
	    } else {
	        System.out.println("Deletion cancelled.");
	    }
	    Utils.pause();
	}
	
	// ---------- Search Movie ----------
	private static void searchMovie() {
	    Utils.clearScreen(40);
	    System.out.println("Search movie by title keyword.");
	    System.out.println("Examples: \"minion\", \"man\", \"ion\"");
	    System.out.print("Enter keyword (or X to cancel): ");
	    String keyword = scan.nextLine().trim();
	    if (keyword.equalsIgnoreCase("X") || keyword.isEmpty()) {
	        System.out.println("Search cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    List<Movie> results = Movie.findMoviesByTitle(keyword);
	    if (results.isEmpty()) {
	        System.out.println("[!] No movies found matching \"" + keyword + "\".");
	    } else {
	        System.out.println("\nFound " + results.size() + " movie(s):");
	        Movie.printTable(results, "Search Results");
	    }
	    Utils.pause();
	}
	
	// ---------- Show Movie Summary ----------
	private static void showMovieSummary() {
	    Utils.clearScreen(20);
	    Movie.displayMovieSummary();
	    Utils.pause();
	}

    // ================================================================
    // HALL MANAGEMENT
    // ================================================================
	private static void manageHalls() {
	    boolean back = false;
	    while (!back) {
	        Utils.clearScreen(20);
	        System.out.println("--- HALL MANAGEMENT ---");
	        System.out.println("1. Add New Hall");
	        System.out.println("2. View Hall Summary");
	        System.out.println("3. Delete Hall");
	        System.out.println("4. View Hall Seat Map");
	        System.out.println("5. View Hall Details");
	        System.out.println("0. Back to Main Menu");
	        System.out.print("Enter your choice: ");
	
	        int choice = Utils.getIntRange(scan, 0, 5);
	        switch (choice) {
	            case 1: addHall();        			 break;
	            case 2: viewHallSummary();           break;
	            case 3: deleteHallInteractive();     break;
	            case 4: viewHallSeatMapMenu();       break;
	            case 5: viewHallDetails();           break;
	            case 0: back = true;                 break;
	        }
	    }
	}


	private static void addHall() {
	    Utils.clearScreen(20);
	    System.out.println("--- Select Hall Type ---");
	    System.out.println("1. Small");
	    System.out.println("2. Medium");
	    System.out.println("3. Large");
	
	    Integer typeChoice = Utils.promptIntWithCancel(scan, "Enter choice", 1, 3, "X");
	    if (typeChoice == null) {
	        System.out.println("Add hall cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    System.out.print("Enter Cinema ID to add this hall to (or X to cancel): ");
	    String cinemaId = scan.nextLine().trim();
	    if (cinemaId.equalsIgnoreCase("X")) {
	        System.out.println("Add hall cancelled.");
	        Utils.pause();
	        return;
	    }
	
	    Cinema targetCinema = findCinemaById(cinemaId);
	    if (targetCinema == null) {
	        System.out.println("[!] Cinema not found. Hall not added.");
	        Utils.pause();
	        return;
	    }
	
	    String type = typeChoice == 1 ? "Small" : (typeChoice == 2 ? "Medium" : "Large");
	    Hall newHall = new Hall(type);
	    targetCinema.getHalls().add(newHall);
	    Hall.registerHall(newHall);
	
	    System.out.println("\n[✓] Hall added successfully to " + targetCinema.getCinemaName());
	    System.out.println("Hall ID: " + newHall.getHallId() + " | Type: " + newHall.getType());
	    Utils.pause();
	}
	
	// ---------- View Hall Summary ----------
	private static void viewHallSummary() {
	    Utils.clearScreen(20);
	    System.out.println("\n========= HALL SUMMARY =========");
	    System.out.println(Hall.getHallSummaryData());
	    System.out.println("================================");
	    Utils.pause();
	}
	
	// ---------- Delete Hall ----------
	private static void deleteHallInteractive() {
	    Hall.displayAllHalls();
	    while (true) {
	        System.out.print("Enter Hall ID to delete (or X to cancel): ");
	        String id = scan.nextLine().trim();
	        if (id.equalsIgnoreCase("X")) {
	            System.out.println("Deletion cancelled.");
	            break;
	        }
	        Hall h = Hall.findHallById(id);
	        if (h == null) {
	            System.out.println("  [!] Hall ID not found! Please try again.");
	            continue;
	        }
	        System.out.print("Are you sure? (y/n): ");
	        String confirm = scan.nextLine().trim();
	        if (confirm.equalsIgnoreCase("y")) {
	            boolean deleted = Hall.deleteHallById(id);
	            if (deleted) System.out.println("Hall deleted successfully!");
	            else System.out.println("Deletion failed.");
	        } else {
	            System.out.println("Deletion cancelled.");
	        }
	        break;
	    }
	    Utils.pause();
	}
	
	// ---------- View Hall Seat Map ----------
	private static void viewHallSeatMapMenu() {
	    while (true) {
	        Utils.clearScreen(80);
	        System.out.println("======= VIEW HALL SEAT MAP =======");
	        System.out.println("1. Small Hall");
	        System.out.println("2. Medium Hall");
	        System.out.println("3. Large Hall");
	        System.out.println("0. Back");
	        System.out.print("Enter choice: ");
	        int c = Utils.getIntRange(scan, 0, 3);
	        if (c == 0) break;
	        Utils.clearScreen(80);
	        Hall.printHallSeatMapByType(c);
	        Utils.pause();
	    }
	}
	
	// ---------- View Hall Details ----------
	private static void viewHallDetails() {
	    Utils.clearScreen(80);
	    Hall.displayAllHallsDetailed();
	    Utils.pause();
	}
	
	
	
    // ================================================================
    // SHOW MANAGEMENT
    // ================================================================
    private static void manageShows() {
        boolean back = false;
        while (!back) {
            Utils.clearScreen(20);
            System.out.println("--- SHOW MANAGEMENT ---");
            System.out.println("1. Add New Show");
            System.out.println("2. Update Show");
            System.out.println("3. Delete Show");
            System.out.println("4. Search Show");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 4);
            switch (choice) {
                case 1: addShow();         break;
                case 2: updateShow();      break;
                case 3: deleteShow();      break;
                case 4: searchShow();      break;
                case 0: back = true;       break;
            }
        }
    }

    private static void addShow() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Show ---");

        System.out.print("Enter Cinema ID: ");
        String cinemaId   = scan.nextLine().trim();
        boolean cinemaExists = false;
        for (Cinema c : cinemaList)
            if (c.getCinemaId().equalsIgnoreCase(cinemaId)) { cinemaExists = true; break; }
        while (cinemaId.isEmpty() || !cinemaExists) {
            System.out.println(cinemaId.isEmpty() ? "Cinema ID cannot be empty!" : "Cinema not found!");
            System.out.print("Enter Cinema ID: ");
            cinemaId = scan.nextLine().trim();
            cinemaExists = false;
            for (Cinema c : cinemaList)
                if (c.getCinemaId().equalsIgnoreCase(cinemaId)) { cinemaExists = true; break; }
        }

        System.out.print("Enter Hall ID: ");
        String hallId    = scan.nextLine().trim();
        boolean hallExists = false;
        for (Cinema c : cinemaList)
            if (c.getCinemaId().equalsIgnoreCase(cinemaId))
                for (Hall h : c.getHalls())
                    if (h.getHallId().equalsIgnoreCase(hallId)) { hallExists = true; break; }
        while (hallId.isEmpty() || !hallExists) {
            System.out.println(hallId.isEmpty() ? "Hall ID cannot be empty!" : "Hall not found in the specified cinema!");
            System.out.print("Enter Hall ID: ");
            hallId = scan.nextLine().trim();
            hallExists = false;
            for (Cinema c : cinemaList)
                if (c.getCinemaId().equalsIgnoreCase(cinemaId))
                    for (Hall h : c.getHalls())
                        if (h.getHallId().equalsIgnoreCase(hallId)) { hallExists = true; break; }
        }

        System.out.print("Enter Movie ID: ");
        String movieId   = scan.nextLine().trim();
        boolean movieExists = false;
        for (Movie m : movieList)
            if (m.getMovieId().equalsIgnoreCase(movieId)) { movieExists = true; break; }
        while (movieId.isEmpty() || !movieExists) {
            System.out.println(movieId.isEmpty() ? "Movie ID cannot be empty!" : "Movie not found!");
            System.out.print("Enter Movie ID: ");
            movieId = scan.nextLine().trim();
            movieExists = false;
            for (Movie m : movieList)
                if (m.getMovieId().equalsIgnoreCase(movieId)) { movieExists = true; break; }
        }

        System.out.print("Enter Show Date (yyyy-MM-dd): ");
        String showDate = scan.nextLine().trim();
        while (showDate.isEmpty() || !Show.isDateFormatValid(showDate) || !Show.isStartDateNotInPast(showDate)) {
            System.out.println(showDate.isEmpty() ? "Show date cannot be empty!"
                : !Show.isDateFormatValid(showDate) ? "Invalid date format! Please enter in yyyy-MM-dd format."
                : "Show date cannot be in the past!");
            System.out.print("Enter Show Date (yyyy-MM-dd): ");
            showDate = scan.nextLine().trim();
        }

        System.out.print("Enter Show Time (HH:mm): ");
        String showTime = scan.nextLine().trim();
        while (showTime.isEmpty() || !Show.isTimeFormatValid(showTime) || !Show.isStartTimeInFuture(showDate, showTime)) {
            System.out.println(showTime.isEmpty() ? "Show start time cannot be empty!"
                : !Show.isTimeFormatValid(showTime) ? "Invalid time format! Please enter in HH:mm format."
                : "Show start time must be in the future!");
            System.out.print("Enter Show Start Time (HH:mm): ");
            showTime = scan.nextLine().trim();
        }

        for (Show s : showList) {
            if (s.getCinema().getCinemaId().equalsIgnoreCase(cinemaId) &&
                s.getHall().getHallId().equalsIgnoreCase(hallId)       &&
                s.getStartDate().equals(showDate)                      &&
                s.getStartTime().equals(showTime)) {
                System.out.println("Failed to add: A show is already scheduled at this cinema, hall, date and time!");
                Utils.pause();
                return;
            }
        }

        Cinema selectedCinema = null;
        Hall   selectedHall   = null;
        Movie  selectedMovie  = null;
        for (Cinema c : cinemaList)
            if (c.getCinemaId().equalsIgnoreCase(cinemaId)) {
                selectedCinema = c;
                for (Hall h : c.getHalls())
                    if (h.getHallId().equalsIgnoreCase(hallId)) { selectedHall = h; break; }
                break;
            }
        for (Movie m : movieList)
            if (m.getMovieId().equalsIgnoreCase(movieId)) { selectedMovie = m; break; }

        Show newShow = new Show(selectedMovie, selectedCinema, selectedHall, showTime, showDate);
        showList.add(newShow);
        System.out.println("\nShow added successfully:\n" + newShow);
        Utils.pause();
    }

    private static void updateShow() {
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to update: ");
        String id = scan.nextLine().trim();

        Show showToUpdate = null;
        for (Show s : showList)
            if (s.getShowId().equalsIgnoreCase(id)) { showToUpdate = s; break; }

        if (showToUpdate == null) { System.out.println("Show not found."); Utils.pause(); return; }

        System.out.println("\nCurrent details:\n" + showToUpdate);

        System.out.print("\nNew Cinema (cinema ID, blank to keep): ");
        String cinemaID = scan.nextLine().trim();
        if (!cinemaID.isEmpty())
            for (Cinema c : cinemaList)
                if (c.getCinemaId().equalsIgnoreCase(cinemaID)) { showToUpdate.setCinema(c); break; }

        System.out.print("\nNew Hall (hall ID, blank to keep): ");
        String hallID = scan.nextLine().trim();
        Cinema currentCinema = showToUpdate.getCinema();
        while (!hallID.isEmpty()) {
            boolean found = false;
            for (Hall h : currentCinema.getHalls())
                if (h.getHallId().equalsIgnoreCase(hallID)) { showToUpdate.setHall(h); found = true; break; }
            if (!found) {
                System.out.println("Hall ID not found in " + currentCinema.getCinemaName() + ". Try again: ");
                hallID = scan.nextLine().trim();
            } else break;
        }

        System.out.print("\nNew Movie (movie ID, blank to keep): ");
        String movieID = scan.nextLine().trim();
        if (!movieID.isEmpty())
            for (Movie m : movieList)
                if (m.getMovieId().equalsIgnoreCase(movieID)) { showToUpdate.setMovie(m); break; }

        System.out.print("\nNew Show Date (yyyy-MM-dd, blank to keep): ");
        String showDate = scan.nextLine().trim();
        if (!showDate.isEmpty()) {
            while (!Show.isDateFormatValid(showDate) || !Show.isStartDateNotInPast(showDate)) {
                System.out.println(!Show.isDateFormatValid(showDate)
                    ? "Invalid date format!" : "Date cannot be in the past!");
                System.out.print("New Show Date (yyyy-MM-dd, blank to keep): ");
                showDate = scan.nextLine().trim();
                if (showDate.isEmpty()) break;
            }
            if (!showDate.isEmpty()) showToUpdate.setStartDate(showDate);
        }

        System.out.print("\nNew Show Time (HH:mm, blank to keep): ");
        String showTime = scan.nextLine().trim();
        if (!showTime.isEmpty()) {
            while (!Show.isTimeFormatValid(showTime) || !Show.isStartTimeInFuture(showToUpdate.getStartDate(), showTime)) {
                System.out.println(!Show.isTimeFormatValid(showTime)
                    ? "Invalid time format!" : "Show start time must be in the future!");
                System.out.print("New Show Time (HH:mm, blank to keep): ");
                showTime = scan.nextLine().trim();
                if (showTime.isEmpty()) break;
            }
            if (!showTime.isEmpty()) showToUpdate.setStartTime(showTime);
        }

        System.out.println("\nShow updated successfully:\n" + showToUpdate);
        Utils.pause();
    }

    private static void deleteShow() {
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to delete: ");
        String id = scan.nextLine().trim();

        Show showToDelete = null;
        for (Show s : showList)
            if (s.getShowId().equalsIgnoreCase(id)) { showToDelete = s; break; }

        if (showToDelete == null) { System.out.println("Show not found."); Utils.pause(); return; }

        System.out.print("Are you sure you want to delete this show? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            showList.remove(showToDelete);
            System.out.println("Show deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        Utils.pause();
    }

    private static void searchShow() {
        Utils.clearScreen(20);
        String movieId = "", cinemaId = "", date = "";

        while (true) {
            System.out.print("Enter Movie ID to search (e.g., M001): ");
            movieId = scan.nextLine().trim();
            if (movieId.isEmpty()) System.out.println("Please enter a Movie ID.");
            else if (!movieId.matches("(?i)^M\\d{3}$")) System.out.println("Invalid format (e.g., M001).");
            else break;
        }
        while (true) {
            System.out.print("Enter Cinema ID to search (e.g., C001): ");
            cinemaId = scan.nextLine().trim();
            if (cinemaId.isEmpty()) System.out.println("Please enter a Cinema ID.");
            else if (!cinemaId.matches("(?i)^C\\d{3}$")) System.out.println("Invalid format (e.g., C001).");
            else break;
        }
        while (true) {
            System.out.print("Enter Date to search (YYYY-MM-DD): ");
            date = scan.nextLine().trim();
            if (date.isEmpty()) System.out.println("Please enter a Date.");
            else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) System.out.println("Invalid format (YYYY-MM-DD).");
            else break;
        }

        List<Show> results = new ArrayList<>();
        for (Show s : showList)
            if (s.getMovie().getMovieId().equalsIgnoreCase(movieId) &&
                s.getCinema().getCinemaId().equalsIgnoreCase(cinemaId) &&
                s.getStartDate().equals(date))
                results.add(s);

        if (results.isEmpty())
            System.out.println("\nNo shows found for Movie: " + movieId.toUpperCase() +
                " at Cinema: " + cinemaId.toUpperCase() + " on " + date);
        else for (Show s : results) System.out.println(s + "\n--------------------------");
        Utils.pause();
    }

    // ================================================================
    // REWARD MANAGEMENT
    // ================================================================
    private static void manageRewards() {
        boolean back = false;
        while (!back) {
            Utils.clearScreen(20);
            System.out.println("--- REWARD MANAGEMENT ---");
            System.out.println("1. Add New Reward");
            System.out.println("2. Update Reward");
            System.out.println("3. Delete Reward");
            System.out.println("4. Search Reward");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 4);
            switch (choice) {
                case 1: addReward();    break;
                case 2: updateReward(); break;
                case 3: deleteReward(); break;
                case 4: searchReward(); break;
                case 0: back = true;    break;
            }
        }
    }

    private static void addReward() {
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
            while (points <= 0) { System.out.print("Must be positive: "); points = scan.nextInt(); }
            System.out.print("Reward Quantity: ");
            int quantity = scan.nextInt();
            while (quantity <= 0) { System.out.print("Must be positive: "); quantity = scan.nextInt(); }
            System.out.print("Discount Percentage: ");
            double pct = scan.nextDouble();
            while (pct <= 0 || pct >= 100) { System.out.print("Must be 1-99: "); pct = scan.nextDouble(); }
            scan.nextLine();
            Reward r = new Discount(name, points, quantity, pct);
            rewardList.add(r);
            System.out.println("\nReward added successfully!\n" + r);

        } else {
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
            while (points <= 0) { System.out.print("Must be positive: "); points = scan.nextInt(); }
            System.out.print("Reward Quantity: ");
            int quantity = scan.nextInt();
            while (quantity <= 0) { System.out.print("Must be positive: "); quantity = scan.nextInt(); }
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
                while (freebieQty <= 0) { System.out.print("Must be positive: "); freebieQty = scan.nextInt(); }
                scan.nextLine();
                freebieMap.put(freebieItem, freebieQty);
                System.out.print("Do you want to continue adding more freebie items? (y/n): ");
                continueAdding = scan.nextLine().trim().equalsIgnoreCase("y");
            }
            Reward r = new Freebie(name, points, quantity, freebieMap);
            rewardList.add(r);
            System.out.println("\nReward added successfully!\n" + r);
        }
        Utils.pause();
    }

    private static void updateReward() {
        Utils.clearScreen(20);
        System.out.print("Enter Reward ID to update: ");
        String id = scan.nextLine().trim();

        Reward r = findRewardById(id);
        if (r == null) { System.out.println("Reward not found."); Utils.pause(); return; }

        System.out.println("\nCurrent details:\n" + r);

        System.out.print("\nNew Reward Name (leave blank to keep current): ");
        String name = scan.nextLine().trim();
        if (!name.isEmpty()) r.setRewardDesc(name);

        System.out.print("New Reward Points (leave blank to keep current): ");
        String ptsInput = scan.nextLine().trim();
        if (!ptsInput.isEmpty()) {
            try {
                int pts = Integer.parseInt(ptsInput);
                if (pts > 0) r.setRewardPts(pts);
                else System.out.println("Reward points must be positive! Keeping current.");
            } catch (NumberFormatException e) { System.out.println("Invalid input! Skipping points update."); }
        }

        System.out.print("New Reward Quantity (leave blank to keep current): ");
        String qtyInput = scan.nextLine().trim();
        if (!qtyInput.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyInput);
                if (qty > 0) r.setRewardQty(qty);
                else System.out.println("Reward quantity must be positive! Keeping current.");
            } catch (NumberFormatException e) { System.out.println("Invalid input! Skipping quantity update."); }
        }

        if (r.getRewardType().equalsIgnoreCase("Discount")) {
            Discount dr = (Discount) r;
            System.out.print("New Discount Percentage (leave blank to keep current): ");
            String discInput = scan.nextLine().trim();
            if (!discInput.isEmpty()) {
                try {
                    double pct = Double.parseDouble(discInput);
                    if (pct > 0 && pct < 100) dr.setPercentage(pct);
                    else System.out.println("Discount must be between 0-100! Keeping current.");
                } catch (NumberFormatException e) { System.out.println("Invalid input! Skipping discount update."); }
            }
        } else if (r.getRewardType().equalsIgnoreCase("Freebie")) {
            Freebie fr = (Freebie) r;
            Map<String, Integer> freebieMap = new HashMap<>(fr.getFreebieItems());
            while (true) {
                System.out.print("Enter freebie item to update (or press Enter/type 'done' to skip): ");
                String freebieItem = scan.nextLine().trim();
                if (freebieItem.isEmpty() || freebieItem.equalsIgnoreCase("done")) break;
                if (!freebieMap.containsKey(freebieItem)) {
                    System.out.println("Freebie item not found! Please enter a valid item.");
                    continue;
                }
                System.out.print("New Reward Quantity for " + freebieItem + " (or press Enter to skip): ");
                String qtyStr = scan.nextLine().trim();
                if (!qtyStr.isEmpty()) {
                    try {
                        int qty = Integer.parseInt(qtyStr);
                        if (qty > 0) freebieMap.put(freebieItem, qty);
                        else System.out.println("Quantity must be positive! Keeping current.");
                    } catch (NumberFormatException e) { System.out.println("Invalid number! Skipping item quantity update."); }
                }
            }
            fr.setFreebieItems(freebieMap);
        }

        System.out.println("\nReward updated successfully:\n" + r);
        Utils.pause();
    }

    private static void deleteReward() {
        Utils.clearScreen(20);
        System.out.print("Enter Reward ID to delete: ");
        String id = scan.nextLine().trim();

        Reward r = findRewardById(id);
        if (r == null) { System.out.println("Reward not found."); Utils.pause(); return; }

        System.out.print("Are you sure you want to delete this reward? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            r.setRewardStatus("Delisted");
            System.out.println("Reward deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        Utils.pause();
    }

    private static void searchReward() {
        Utils.clearScreen(20);
        System.out.println("Search by reward name.");
        System.out.print("Enter keyword: ");
        String key = scan.nextLine().trim();

        List<Reward> results = new ArrayList<>();
        for (Reward r : rewardList)
            if (r.getRewardDesc().toLowerCase().contains(key.toLowerCase()))
                results.add(r);

        if (results.isEmpty()) System.out.println("No rewards found.");
        else for (Reward r : results) System.out.println(r + "\n--------------------------");
        Utils.pause();
    }

    private static Reward findRewardById(String id) {
        for (Reward r : rewardList)
            if (r.getRewardID().equalsIgnoreCase(id)) return r;
        return null;
    }

    // ================================================================
    // INITIALIZE DATA
    // ================================================================
    private static void initializeCinemas(){
        cinemaList.add(new Cinema("DGV Mid Valley Southkey",       "Johor",           "Johor Bahru",      "81100"));
        cinemaList.add(new Cinema("DGV Aman Central",              "Kedah",           "Alor Setar",       "05150"));
        cinemaList.add(new Cinema("DGV AEON Mall Kota Bharu",      "Kelantan",        "Kota Bharu",       "15050"));
        cinemaList.add(new Cinema("DGV AEON Bandaraya Melaka",     "Melaka",          "Melaka",           "75450"));
        cinemaList.add(new Cinema("DGV Palm Mall",                 "Negeri Sembilan", "Seremban",         "70200"));
        cinemaList.add(new Cinema("DGV East Coast Mall",           "Pahang",          "Kuantan",          "25000"));
        cinemaList.add(new Cinema("DGV Gurney Plaza",              "Pulau Pinang",    "George Town",      "10250"));
        cinemaList.add(new Cinema("DGV Ipoh Parade",               "Perak",           "Ipoh",             "30450"));
        cinemaList.add(new Cinema("DGV Kangar Jaya Mall",          "Perlis",          "Kangar",           "01000"));
        cinemaList.add(new Cinema("DGV Suria Sabah",               "Sabah",           "Kota Kinabalu",    "88000"));
        cinemaList.add(new Cinema("DGV CityONE Megamall",          "Sarawak",         "Kuching",          "93350"));
        cinemaList.add(new Cinema("DGV Setia City Mall",           "Selangor",        "Shah Alam",        "40170"));
        cinemaList.add(new Cinema("DGV Paya Bunga Square",         "Terengganu",      "Kuala Terengganu", "20100"));
        cinemaList.add(new Cinema("DGV Mid Valley Megamall",       "Kuala Lumpur",    "Kuala Lumpur",     "59200"));
        cinemaList.add(new Cinema("DGV IOI City Mall",             "Putrajaya",       "Putrajaya",        "62502"));
        cinemaList.add(new Cinema("DGV Financial Park",            "Labuan",          "Labuan",           "87000"));
        cinemaList.add(new Cinema("DGV Sunway Pyramid",            "Selangor",        "Petaling Jaya",    "47500"));
        cinemaList.add(new Cinema("DGV Pavilion KL",               "Kuala Lumpur",    "Kuala Lumpur",     "55100"));
        cinemaList.add(new Cinema("DGV Queensbay Mall",            "Pulau Pinang",    "Bayan Lepas",      "11900"));
        cinemaList.add(new Cinema("DGV Paradigm Mall JB",          "Johor",           "Johor Bahru",      "81200"));
        cinemaList.add(new Cinema("DGV 1 Utama Shopping Centre",   "Selangor",        "Petaling Jaya",    "47800"));
        cinemaList.add(new Cinema("DGV KSL City Mall",             "Johor",           "Johor Bahru",      "80250"));
        cinemaList.add(new Cinema("DGV AEON Mall Tebrau City",     "Johor",           "Johor Bahru",      "81100"));
        cinemaList.add(new Cinema("DGV MyTOWN Shopping Centre",    "Kuala Lumpur",    "Kuala Lumpur",     "55100"));
        cinemaList.add(new Cinema("DGV Sunway Carnival Mall",      "Pulau Pinang",    "Seberang Jaya",    "13700"));
        cinemaList.add(new Cinema("DGV AEON Mall Bukit Tinggi",    "Selangor",        "Klang",            "41200"));
        cinemaList.add(new Cinema("DGV Vivacity Megamall",         "Sarawak",         "Kuching",          "93350"));
        cinemaList.add(new Cinema("DGV Imago Shopping Mall",       "Sabah",           "Kota Kinabalu",    "88100"));
        cinemaList.add(new Cinema("DGV The Spring",                "Sarawak",         "Kuching",          "93300"));
        cinemaList.add(new Cinema("DGV Toppen Shopping Centre",    "Johor",           "Johor Bahru",      "81100"));

    }

    private static void initializeHallsforCinemas(){
        for (Cinema cinema : cinemaList) {
            for (int i = 1 ; i <= 10 ; i++) {
                String type = (i <= 4) ? "Small" : (i <= 7 ? "Medium" : "Large");
                Hall newHall = new Hall(type);
                cinema.getHalls().add(newHall);
                Hall.registerHall(newHall); // register into Hall's list so staff can see it
            }
        }
    }

    private static void initializeMovie(){
        if (movieList.isEmpty()) {
            movieList.add(new Movie("M001", "Inception",                           "Sci-Fi",    "2h 28m", 13, "Now Showing"));
            movieList.add(new Movie("M002", "The Conjuring",                       "Horror",    "1h 52m", 18, "Now Showing"));
            movieList.add(new Movie("M003", "Toy Story 4",                         "Animation", "1h 40m",  0, "Now Showing"));
            movieList.add(new Movie("M004", "The Dark Knight",                     "Action",    "2h 32m", 13, "Now Showing"));
            movieList.add(new Movie("M005", "Avengers: Endgame",                   "Action",    "3h 01m", 13, "Now Showing"));
            movieList.add(new Movie("M006", "Gladiator",                           "Action",    "2h 35m", 18, "Now Showing"));
            movieList.add(new Movie("M007", "Despicable Me 4",                     "Animation", "1h 34m",  0, "Now Showing"));
            movieList.add(new Movie("M008", "The Hangover",                        "Comedy",    "1h 40m", 18, "Now Showing"));
            movieList.add(new Movie("M009", "Interstellar",                        "Sci-Fi",    "2h 49m", 13, "Now Showing"));
            movieList.add(new Movie("M010", "Spider-Man: Across the Spider-Verse", "Animation", "2h 20m",  0, "Now Showing"));
        }
    }

    private static void initializeShow(){
    	   if (movieList.size() < 10){
    	   	System.out.println(" [Warning] Not enough movies to initialise shows.");
    	   	return;
    	   }
        LocalDateTime     today     = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[]          timeSlots = {"09:00", "12:00", "15:00", "18:00", "21:00", "00:00"};

        for (Cinema cinema : cinemaList) {
            for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
                String dateString = today.plusDays(dayOffset).format(formatter);
                for (String timeSlot : timeSlots) {
                    for (int j = 0; j < 10; j++) {
                        showList.add(new Show(movieList.get(j), cinema,
                                cinema.getHalls().get(j), timeSlot, dateString));
                    }
                }
            }
        }
    }

    private static void initializeTicket(){
        ticketList.add(new Ticket("Child",  15));
        ticketList.add(new Ticket("Adult",  28));
        ticketList.add(new Ticket("Senior", 12));
    }

    private static void initializeReward(){
        rewardList.add(new Discount("5% off on next booking", 50, 10, 5.0));
        rewardList.add(new Discount("10% off on next booking", 100, 10, 10.0));
        rewardList.add(new Discount("15% off on next booking", 150, 10, 15.0));
        rewardList.add(new Discount("20% off on next booking", 200, 10, 20.0));
        rewardList.add(new Discount("25% off on next booking", 250, 10, 25.0));
        rewardList.add(new Discount("30% off on next booking", 300, 10, 30.0));
        rewardList.add(new Discount("35% off on next booking", 350, 10, 35.0));
        rewardList.add(new Discount("45% off on next booking", 450, 10, 45.0));
        rewardList.add(new Discount("50% off on next booking", 500, 10, 50.0));
        rewardList.add(new Freebie("Free Popcorn", 60, 10, Map.of("Popcorn", 2)));
        rewardList.add(new Freebie("Free Hotdog", 35, 10, Map.of("Hotdog", 1)));
        rewardList.add(new Freebie("Free Cola", 40, 10, Map.of("Cola", 2)));
        rewardList.add(new Freebie("Free Sweet Corn", 70, 10, Map.of("Sweet Corn", 3)));
        rewardList.add(new Freebie("Free Nuggets", 30, 10, Map.of("Nuggets", 1)));
    }

    private static void initializeRedemption(Customer customer){
        Redemption r1 = new Redemption(rewardList.get(0));
    	Redemption r2 = new Redemption(rewardList.get(1));
    	Redemption r3 = new Redemption(rewardList.get(10));
    	Redemption r4 = new Redemption(rewardList.get(11));

    	redemptionList.addAll(Arrays.asList(r1, r2, r3, r4));

    	customerRedemptions.computeIfAbsent(customer.getCustomerID(), k -> new ArrayList<>())
        .addAll(Arrays.asList(
            r1.getRedemptionID(),  // ? ??? ID
            r2.getRedemptionID(),
            r3.getRedemptionID(),
            r4.getRedemptionID()
        ));
    }


    // ================================================================
    // CINEMA BOOKING LOOP (Customer - browse & book)
    // ================================================================
    private static void cinemaLoop(Customer customer, Scanner scanner) {
        String  currentFilter = "None";
        String  errorMessage  = "";
        boolean isSelectingId = false;

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Cinema> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(cinemaList);
            } else {
                for (Cinema c : cinemaList)
                    if (c.getCinemaName().toLowerCase().contains(currentFilter.toLowerCase()) ||
                        c.getState().toLowerCase().contains(currentFilter.toLowerCase())      ||
                        c.getCity().toLowerCase().contains(currentFilter.toLowerCase()))
                        filteredList.add(c);
            }

            Utils.displayLine();
            Cinema.displaySelectCinemaHeader();
            Utils.displayInnerLine();
            Cinema.displayCinemaInfoHeader();
            Utils.displayInnerLine();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) filteredList.get(i).displayCinemaDetails();
                else Utils.displayEmptyRow(4, 30, 15, 23);
            }
            Utils.displayLine();

            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText);

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.println("  Enter keywords to filter cinemas (Cinema Name, State, City)");
                System.out.print("  (use \"quotes\" for keywords, X to exit, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) return;
                else if (input.isEmpty()) { isSelectingId = true; errorMessage = ""; }
                else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage  = "";
                    isSelectingId = true;
                } else errorMessage = "Please use \"quotes\" for keywords!";

            } else {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.print("  Enter cinema ID to choose cinema (X to refilter the cinemas) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) { isSelectingId = false; errorMessage = ""; }
                else if (idInput.isEmpty()) { errorMessage = "Cinema ID cannot be empty!"; }
                else {
                    Cinema selectedCinema = null;
                    for (Cinema c : filteredList)
                        if (c.getCinemaId().equalsIgnoreCase(idInput)) { selectedCinema = c; break; }

                    if (selectedCinema != null) {
                        errorMessage  = "";
                        currentFilter = "None";
                        isSelectingId = false;
                        movieLoop(customer, scanner, selectedCinema);
                    } else errorMessage = "Invalid ID. Please enter a valid Cinema ID.";
                }
            }
        }
    }

    // ================================================================
    // MOVIE LOOP
    // ================================================================
    private static void movieLoop(Customer customer, Scanner scanner, Cinema selectedCinema) {
        String  currentFilter = "None";
        String  errorMessage  = "";
        boolean isSelectingId = false;

        List<Movie> availableMovies = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Show show : showList) {
            if (show.getCinema().getCinemaId().equals(selectedCinema.getCinemaId())) {
                LocalDateTime showDateTime = show.getStartDateTime(show.getStartDate(), show.getStartTime());
                if (!showDateTime.isBefore(now)) {
                    Movie m = show.getMovie();
                    if (!availableMovies.contains(m)) availableMovies.add(m);
                }
            }
        }

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Movie> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) filteredList.addAll(availableMovies);
            else for (Movie m : availableMovies)
                if (m.getTitle().toLowerCase().contains(currentFilter.toLowerCase()) ||
                    m.getGenre().toLowerCase().contains(currentFilter.toLowerCase()))
                    filteredList.add(m);

            Utils.displayLine();
            Movie.displaySelectMovieHeader(selectedCinema.getCinemaName());
            Utils.displayLine();
            Movie.displayMovieInfoHeader();
            Utils.displayLine();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) filteredList.get(i).displayMovieDetails();
                else Utils.displayEmptyRow(4, 28, 19, 8, 10);
            }
            Utils.displayLine();

            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText);

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.println("  Enter keywords to filter movies (Movie Title, Genre)");
                System.out.print("  (use \"quotes\" for keywords, X to BACK to cinema, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) return;
                else if (input.isEmpty()) { isSelectingId = true; errorMessage = ""; }
                else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage  = "";
                    isSelectingId = true;
                } else errorMessage = "Please use \"quotes\" for keywords!";

            } else {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.print("  Enter movie ID to choose movie (X to refilter the movies) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) { isSelectingId = false; errorMessage = ""; }
                else if (idInput.isEmpty()) { errorMessage = "Movie ID cannot be empty!"; }
                else {
                    Movie selectedMovie = null;
                    for (Movie m : filteredList)
                        if (m.getMovieId().equalsIgnoreCase(idInput)) { selectedMovie = m; break; }

                    if (selectedMovie != null) {
                        errorMessage  = "";
                        currentFilter = "None";
                        isSelectingId = false;
                        showLoop(customer, scanner, selectedCinema, selectedMovie);
                    } else errorMessage = "Invalid movie ID. Please enter a valid Movie ID.";
                }
            }
        }
    }

    // ================================================================
    // SHOW LOOP
    // ================================================================
    private static void showLoop(Customer customer, Scanner scanner, Cinema selectedCinema, Movie selectedMovie) {
        String  currentFilter = "None";
        String  errorMessage  = "";
        boolean isSelectingId = false;

        List<Show> availableShows = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Show show : showList) {
            if (show.getCinema().getCinemaId().equals(selectedCinema.getCinemaId()) &&
                show.getMovie().getMovieId().equals(selectedMovie.getMovieId())) {
                LocalDateTime showDateTime = show.getStartDateTime(show.getStartDate(), show.getStartTime());
                if (!showDateTime.isBefore(now)) availableShows.add(show);
            }
        }

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Show> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) filteredList.addAll(availableShows);
            else for (Show s : availableShows)
                if (s.getStartDate().contains(currentFilter) || s.getStartTime().contains(currentFilter))
                    filteredList.add(s);

            Utils.displayLine();
            Show.displaySelectShowHeader(selectedMovie.getTitle());
            Utils.displayInnerLine();
            Show.displayShowInfoHeader();
            Utils.displayInnerLine();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) filteredList.get(i).displayShowDetails();
                else Utils.displayEmptyRow(6, 20, 15, 31);
            }
            Utils.displayLine();

            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText);

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.println("  Enter keywords to filter shows (Date '2026-07-01' or Time '15:00')");
                System.out.print("  (use \"quotes\" for keywords, X to BACK to movies, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) return;
                else if (input.isEmpty()) { isSelectingId = true; errorMessage = ""; }
                else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage  = "";
                    isSelectingId = true;
                } else errorMessage = "Please use \"quotes\" for keywords!";

            } else {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.print("  Enter show ID to choose time (X to refilter the shows) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) { isSelectingId = false; errorMessage = ""; }
                else if (idInput.isEmpty()) { errorMessage = "Show ID cannot be empty!"; }
                else {
                    Show selectedShow = null;
                    for (Show s : filteredList)
                        if (s.getShowId().equalsIgnoreCase(idInput)) { selectedShow = s; break; }

                    if (selectedShow != null) {
                        errorMessage  = "";
                        currentFilter = "None";
                        isSelectingId = false;
                        SeatandTicketLoop(customer, scanner, selectedShow);
                    } else errorMessage = "Invalid show ID. Please enter a valid Show ID.";
                }
            }
        }
    }

    // ================================================================
    // SEAT AND TICKET LOOP
    // ================================================================
    private static void SeatandTicketLoop(Customer customer, Scanner scanner, Show selectedShow) {
        String errorMessage = "";
        Hall   showHall     = selectedShow.getHall();
        int    hallNo       = showHall.getSeatHallNumber();
        List<String> bookedSeats = new ArrayList<>();

        while (true) {
            showHall.displaySeatMap();
            System.out.println();
            Utils.displayLine();
            System.out.println("  Movie: " + selectedShow.getMovie().getTitle());
            System.out.println("  Seats selected this time: " +
                (bookedSeats.isEmpty() ? "None" : String.join(", ", bookedSeats)));
            Utils.displayLine();

            if (!errorMessage.isEmpty()) { System.out.println("  Error: " + errorMessage); errorMessage = ""; }

            System.out.println("  Enter Seat ID to book (e.g., A1, B2)");
            System.out.print("  (Enter 'D' when DONE selecting, 'X' to Go Back) : ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("X")) {
                for (String seat : bookedSeats) Seat.cancelBooking(hallNo, seat);
                return;
            } else if (input.equals("D")) {
                if (bookedSeats.isEmpty()) errorMessage = "You must select at least one seat before proceeding!";
                else break;
            } else if (input.isEmpty()) {
                errorMessage = "Seat ID cannot be empty!";
            } else {
                boolean isValidInput=Seat.bookSeat(hallNo,input);
                if (isValidInput==true) 
                    bookedSeats.add(input);

            }
        }

        // Ticket type assignment
        System.out.println("\n  Seats confirmed: " + String.join(", ", bookedSeats));
        System.out.println("  Proceeding to ticket selection...\n");
        Utils.displayLine();

        List<Ticket> tList = new ArrayList<>();
        tList.add(new Ticket("Child",  15));
        tList.add(new Ticket("Adult",  28));
        tList.add(new Ticket("Senior", 12));

        System.out.println("  --- TICKET MENU ---");
        for (Ticket t : tList)
            System.out.println("  * " + t.getTicketType() + " (RM " + t.getTicketAmt() + ")");
        Utils.displayLine();

        int totalSeats         = bookedSeats.size();
        int selectedCount      = 0;
        Map<String, Integer> userPurchasedTickets = new HashMap<>();

        while (selectedCount < totalSeats) {
            int seatsLeft = totalSeats - selectedCount;
            System.out.println("\n  Remaining tickets to assign: " + seatsLeft);
            System.out.print("  Enter Ticket Type (e.g., Adult, Child, Senior): ");
            String typeInput = scanner.nextLine().trim();

            Ticket matched = null;
            for (Ticket t : tList)
                if (t.getTicketType().equalsIgnoreCase(typeInput)) { matched = t; break; }

            if (matched == null) { System.out.println("  Error: Ticket type not found!"); continue; }

            System.out.print("  Enter quantity for " + matched.getTicketType() + ": ");
            String qtyInput = scanner.nextLine().trim();
            int qty = 0;
            try {
                qty = Integer.parseInt(qtyInput);
                if (qty < 0) { System.out.println(" Error: Quantity cannot be negative!"); continue; }
                if (qty == 0) continue;
            } catch (NumberFormatException e) { System.out.println(" Error: Please enter a valid number!"); continue; }

            if (selectedCount + qty > totalSeats) {
                System.out.println(" Error: You only have " + seatsLeft + " seats left! You entered " + qty + ".");
            } else {
                selectedCount += qty;
                userPurchasedTickets.put(matched.getTicketType(),
                    userPurchasedTickets.getOrDefault(matched.getTicketType(), 0) + qty);
                System.out.println(" Success! Added " + qty + " " + matched.getTicketType() + " ticket(s).");
            }
        }

        System.out.println("\n  ========================================");
        System.out.println("  All tickets selected successfully!");
        for (Map.Entry<String, Integer> e : userPurchasedTickets.entrySet())
            System.out.println("  - " + e.getKey() + ": " + e.getValue() + " ticket(s)");
        System.out.println("  ========================================");
        Utils.pause();
        Utils.clearScreen(20);
        System.out.println("  Proceeding to Food & Beverage selection...\n");
        FoodOrderingLoop(customer, scanner, selectedShow, bookedSeats, userPurchasedTickets);
    }

    // ================================================================
    // FOOD ORDERING LOOP
    // ================================================================
    private static void FoodOrderingLoop(Customer customer, Scanner scanner, Show selectedShow,
            List<String> bookedSeats, Map<String, Integer> purchasedTickets) {
        FoodOrder[] menus       = {new Snack(), new Drink(), new Combo()};
        FoodOrder   customerOrder = new FoodOrder();
        String      errorMessage  = "";
        int         hallNo        = selectedShow.getHall().getSeatHallNumber();

        while (true) {
            Utils.clearScreen(20);
            System.out.println("  ========================================");
            System.out.println("  |        FOOD & BEVERAGE MENU          |");
            System.out.println("  ========================================");
            System.out.println("  1. Snacks");
            System.out.println("  2. Drinks");
            System.out.println("  3. Combos");
            Utils.displayLine();
            System.out.println("  Current Cart: " + (customerOrder.getItemNo() > 0 ? customerOrder.getOrder() : "Empty"));
            System.out.printf("  Total Food Cost: RM %.2f\n", customerOrder.getTotalPrice());
            Utils.displayLine();

            if (!errorMessage.isEmpty()) { System.out.println("  [!] Error: " + errorMessage); errorMessage = ""; }

            System.out.println("  Select a category (1-3)");
            System.out.print("  (Enter 'D' to proceed to Payment, 'X' to re-select Seats/Tickets) : ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isEmpty()) { errorMessage = "Input cannot be empty!"; continue; }

            if (input.equals("X")) {
                for (String seat : bookedSeats) Seat.cancelBooking(hallNo, seat);
                System.out.println("  [*] Previous seats cancelled. Returning to seat selection...");
                Utils.pause();
                SeatandTicketLoop(customer, scanner, selectedShow);
                return;
            } else if (input.equals("D")) {
                break;
            } else if (input.equals("1")) {
                System.out.println("\n  --- SNACKS ---");
                System.out.print(menus[0]);
                System.out.print("  Select Snack ID (1-5) or 0 to go back: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= 5) { ((Snack) menus[0]).addSnack(choice, customerOrder); System.out.println("  [*] Snack added to cart!"); Utils.pause(); }
                    else if (choice != 0) errorMessage = "Invalid ID! Please enter 1 to 5.";
                } catch (NumberFormatException e) { errorMessage = "Invalid input! Please enter numbers only."; }
            } else if (input.equals("2")) {
                System.out.println("\n  --- DRINKS ---");
                System.out.print(menus[1]);
                System.out.print("  Select Drink ID (1-5) or 0 to go back: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= 5) { ((Drink) menus[1]).addDrink(choice, customerOrder); System.out.println("  [*] Drink added to cart!"); Utils.pause(); }
                    else if (choice != 0) errorMessage = "Invalid ID! Please enter 1 to 5.";
                } catch (NumberFormatException e) { errorMessage = "Invalid input! Please enter numbers only."; }
            } else if (input.equals("3")) {
                System.out.println("\n  --- COMBOS ---");
                System.out.print(menus[2]);
                System.out.print("  Select Combo ID (1-5) or 0 to go back: ");
                try {
                    int choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= 5) { ((Combo) menus[2]).addCombo(choice, customerOrder); System.out.println("  [*] Combo added to cart!"); Utils.pause(); }
                    else if (choice != 0) errorMessage = "Invalid ID! Please enter 1 to 5.";
                } catch (NumberFormatException e) { errorMessage = "Invalid input! Please enter numbers only."; }
            } else {
                errorMessage = "Invalid choice! Please enter 1, 2, 3, D, or X.";
            }
        }

        System.out.println("\n  [*] Proceeding to Payment...");
        Utils.pause();
        PaymentLoop(customer, scanner, selectedShow, bookedSeats, purchasedTickets, customerOrder);
    }

    // ================================================================
    // PAYMENT LOOP
    // ================================================================
    private static void PaymentLoop(Customer customer, Scanner scanner, Show selectedShow,
        List<String> bookedSeats, Map<String, Integer> purchasedTickets,
        FoodOrder customerOrder) {

        Utils.clearScreen(20);
        System.out.print(Payment.paymentHeader());

        List<Ticket> tList = new ArrayList<>();
        if (purchasedTickets != null && !purchasedTickets.isEmpty()) {
            for (Map.Entry<String, Integer> entry : purchasedTickets.entrySet()) {
                String typeName = entry.getKey();
                int    quantity = entry.getValue();
                double realPrice = Ticket.getTicketPriceByType(typeName);
                for (int i = 0; i < quantity; i++) tList.add(new Ticket(typeName, realPrice));
            }
        }

        Ticket[]  ticketArray   = tList.toArray(new Ticket[0]);
        double ticketTotal = Ticket.calculateTicketTotal(ticketArray);
        double foodTotal = customerOrder.getTotalPrice();
        double discountAmt = 0.0;
        String appliedCode = "";
        String currentCustomerID = customer.getCustomerID();

        System.out.println("  --- INITIAL BILLING ---");
        System.out.print(Payment.toString(ticketTotal, foodTotal, discountAmt, appliedCode, rewardList));
        
        // Reward redemption
        while (true) {
            System.out.println("\n  Do you have a reward code to use?");
            System.out.print("  Enter Reward Code (e.g., F001, D001) or press ENTER to skip: ");
            String codeInput = scanner.nextLine().trim().toUpperCase();
            
            if (codeInput.isEmpty()) { System.out.println("  [i] Skipping rewards..."); break; }
            
            if (!codeInput.matches("[FD]\\d{3}")) {
                System.out.println(" Error: Invalid reward code format! (Must be Fxxx or Dxxx)");
                continue;
            }
            
            Reward targetReward = null;
            List<String> ownedRDIDs = customerRedemptions.get(currentCustomerID);
            String matchedRDID = null;

            if (ownedRDIDs == null) {
                System.out.println(" Error: You do not have any rewards in your account!");
            } else {
                for (Redemption redemp : redemptionList) {
                    if (ownedRDIDs.contains(redemp.getRedemptionID()) &&
                        redemp.getReward().getRewardID().equals(codeInput)) {
                        targetReward = redemp.getReward();
                        matchedRDID  = redemp.getRedemptionID();
                        break;
                    }
                }
            }
            
            if (targetReward == null) { System.out.println("  [!] Error: Code incorrect or not owned by you!"); continue; }

            double subTotal    = Payment.calculateSubTotal(ticketTotal, foodTotal);
            double newDiscount = Payment.applyDiscount(codeInput, subTotal, rewardList);
            
            if (newDiscount > 0) {
                discountAmt  = newDiscount;
                appliedCode  = codeInput;

                final String finalMatchedID = matchedRDID;
                if (customerRedemptions.containsKey(currentCustomerID)) {
                    customerRedemptions.get(currentCustomerID).remove(finalMatchedID);
                    if (customerRedemptions.get(currentCustomerID).isEmpty())
                        customerRedemptions.remove(currentCustomerID);
                }
                redemptionList.removeIf(r -> r.getRedemptionID().equals(finalMatchedID));

                System.out.println("  [v] Reward applied successfully!");
                System.out.println("\n  --- UPDATED BILLING ---");
                System.out.print(Payment.toString(ticketTotal, foodTotal, discountAmt, appliedCode, rewardList));
                Utils.pause();
                break;
            } else {
                System.out.println("  [!] Reward code is not a discount or invalid.");
            }
        }
        double subTotal    = Payment.calculateSubTotal(ticketTotal, foodTotal);
        double tax         = Payment.calculateTax(subTotal);

        // Payment method selection
        PaymentMethod finalMethod = null;
        while (true) {
            System.out.print(PaymentMethod.paymentMethodHeader());
            System.out.print(new PaymentMethod().toString());
            String payChoice = scanner.nextLine().trim();

            if (payChoice.equals("1")) {
                CreditCardPayment cc = new CreditCardPayment();
                cc.setMethodType(1);
                System.out.print(CreditCardPayment.creditCardHeader());
                while (true) {
                    System.out.print(" Enter Card Holder Name: ");
                    cc.setCardHolder(scanner.nextLine().trim().toUpperCase());
                    String error = cc.validateCardHolder();
                    if (error.isEmpty()) break;
                    System.out.println(" [!] " + error);
                }
                while (true) {
                    System.out.print(" Enter Card Number (xxxx xxxx xxxx xxxx): ");
                    cc.setCardNumber(scanner.nextLine().trim());
                    String error = cc.validateCardNum();
                    if (error.isEmpty()) break;
                    System.out.println(" [!] " + error);
                }
                
		    while (true) {
                try {
                    System.out.print(" Enter Expire Month (MM): ");
                    cc.setExpireMonth(Integer.parseInt(scanner.nextLine().trim()));
                    String error = cc.validateMonth();
                    if (error.isEmpty()) break;
                    System.out.println(" [!] " + error);
            } catch (NumberFormatException e) {
        System.out.println("  Invalid month - please enter a whole number.");
    }
}
        while (true) {
            try {
                System.out.print(" Enter Expire Year (YY): ");
                cc.setExpireYear(Integer.parseInt(scanner.nextLine().trim()));
                String error = cc.validateYear();
                if (error.isEmpty()) break;
                System.out.println(" [!] " + error);
    } catch (NumberFormatException e) {
        System.out.println("  Invalid year - please enter a whole number.");
    }
}

// Validate expiry AFTER both month and year are entered
        String expireError = cc.validateExpire();
        if (expireError.equals("Card has expired.")) {
        System.out.println(" [!] " + expireError + " Please enter a valid card.");
    // Re-ask month and year by continuing the outer credit card loop
        continue;
}
            
             	while (true) {
             		try {
             			System.out.print(" Enter CVC: ");
                		cc.setCardCvc(Integer.parseInt(scanner.nextLine().trim()));
                		String error = cc.validateCardCvc();
                		if (error.isEmpty()) break;
                		System.out.println(" [!] " + error);
             		} catch (NumberFormatException e) {
             			System.out.println("		Invalid cvc		- please enter a whole number.");
             		}
             	}
                finalMethod = cc;
                break;

            } else if (payChoice.equals("2")) {
                EWalletPayment wallet = new EWalletPayment();
                wallet.setMethodType(2);
                System.out.print(EWalletPayment.eWalletHeader());
                while (true) {
                	try {
                		System.out.print(wallet.eWalletType());
                    	int type = Integer.parseInt(scanner.nextLine().trim());
                    	if (wallet.validateEWalletType(type).equals("\n")) { wallet.setWalletType(type); break; }
                    	System.out.print(wallet.validateEWalletType(type));
                	} catch (NumberFormatException e) {
                		System.out.println("	Invalid type	- please enter a whole number.");
                	}   
                }
                while (true) {
                    System.out.print(" Enter Wallet Owner Name: ");
                    wallet.setWalletOwner(scanner.nextLine().trim().toUpperCase());
                    String error = wallet.validateWalletOwner();
                    if (error.isEmpty()) break;
                    System.out.println(" [!] " + error);
                }
                while (true) {
                    System.out.print(" Enter Wallet Account ID (Phone No): ");
                    wallet.setWalletAccID(scanner.nextLine().trim());
    
                    String error = wallet.validateWalletAccID();
                    if (error == null || error.isEmpty()) {
                        break; 
                    }
                    System.out.println(" [!] " + error);
                    }
                finalMethod = wallet;
                break;
            } else {
                System.out.println(" [!] Invalid choice, please select 1 or 2.");
            }
        }

        // Final confirmation
        while (true) {
            System.out.print(Payment.confirmationMsg());
            String confirm = scanner.nextLine().trim().toUpperCase();

            if (confirm.equals("Y")) {
                System.out.println("\n  " + Payment.confirmMsg());
                Booking currentBooking  = new Booking(selectedShow, customerOrder, ticketArray);
                Payment currentPayment  = new Payment(currentBooking, subTotal, tax, discountAmt);
                currentPayment.earnRewardPts(customer);
                storage.addBooking(customer.getCustomerID(), currentBooking, currentPayment);
                storage.updateCustomerPoints(customer.getCustomerID(), customer.getRewardPts());
                Utils.pause();
                showCustomerMenu(customer);
                break;
            } else if (confirm.equals("N")) {
                System.out.println("\n  " + Payment.cancelledMsg());
                int hallNo = selectedShow.getHall().getSeatHallNumber();
                for (String seat : bookedSeats) Seat.cancelBooking(hallNo, seat);
                Utils.pause();
                break;
            } else {
                System.out.println("  " + Payment.invalidConfirmMsg());
            }
        }
    }
    // ================================================================
    // HELPER - validated integer input
    // ================================================================
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scan.nextLine().trim());
                if (val < min || val > max)
                    throw new IllegalArgumentException(
                            "Please enter a number between " + min + " and " + max + ".");
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input - please enter a whole number.");
            } catch (IllegalArgumentException e) {
                System.out.println("  " + e.getMessage());
            }
        }
    }

    // ================================================================
    // REDEEM REWARD
    // ================================================================
    private static void redeemReward(Customer customer, Scanner scanner) {
        String  currentFilter  = "None";
        String  errorMessage   = "";
        boolean isSelectingId  = false;
        boolean isSelectingQty = false;   
        Reward  selectedReward = null;    

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Reward> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(rewardList);
            } else {
                for (Reward r : rewardList)
                    if (r.getRewardDesc().toLowerCase().contains(currentFilter.toLowerCase()))
                        filteredList.add(r);
            }

            Utils.displayLine();
            Reward.displayRewardRedemptionHeader();
            Utils.displayInnerLine();
            Reward.displayRewardInfoHeader();
            Utils.displayInnerLine();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) filteredList.get(i).displayRewardDetails();
                else Utils.displayEmptyRow(4, 34, 11, 12, 8); 
            }
            Utils.displayLine();

            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText);
            System.out.println("  Your Points    : " + customer.getRewardPts()); 

            if (!isSelectingId && !isSelectingQty) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.println("  Enter keywords to filter rewards (Reward Description)");
                System.out.print("  (use \"quotes\" for keywords, X to exit, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) return;
                else if (input.isEmpty()) { isSelectingId = true; errorMessage = ""; }
                else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage  = "";
                    isSelectingId = true;
                } else errorMessage = "Please use \"quotes\" for keywords!";

            } else if (isSelectingId && !isSelectingQty) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.print("  Enter reward ID to choose reward (X to refilter the rewards) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) { isSelectingId = false; errorMessage = ""; }
                else if (idInput.isEmpty()) { errorMessage = "Reward ID cannot be empty!"; }
                else {
                    selectedReward = null;
                    for (Reward r : filteredList)
                        if (r.getRewardID().equalsIgnoreCase(idInput)) { selectedReward = r; break; }

                    if (selectedReward != null) {
                        isSelectingQty = true;
                        errorMessage   = "";
                    } else errorMessage = "Invalid ID. Please enter a valid Reward ID.";
                }

            } else if (isSelectingQty) {
                if (!errorMessage.isEmpty()) System.out.println("  Error: " + errorMessage);
                System.out.println("  Selected       : [" + selectedReward.getRewardDesc() + "] (Stock: " + selectedReward.getRewardQty() + ")");
                System.out.print("  Enter quantity to redeem (X to reselect ID) : ");
                String qtyInput = scanner.nextLine().trim();

                if (qtyInput.equalsIgnoreCase("X")) {
                    isSelectingQty = false; 
                    selectedReward = null;
                    errorMessage   = "";
                } else if (qtyInput.isEmpty()) {
                    errorMessage = "Quantity cannot be empty!";
                } else {
                    try {
                        int qty = Integer.parseInt(qtyInput);

                        if (qty <= 0) {
                            errorMessage = "Quantity must be greater than 0!";
                        } else if (qty > selectedReward.getRewardQty()) {
                            errorMessage = "Insufficient stock! Only " + selectedReward.getRewardQty() + " left.";
                        } else {
                            Redemption firstRedemption = new Redemption(selectedReward);

                            int totalRedeemPts = firstRedemption.calculateTotalRedeemPts(qty);

                            if (totalRedeemPts > customer.getRewardPts()) {
                                errorMessage = "Insufficient points! You need more" + totalRedeemPts + " points.";
                            } else {
                                customer.setRewardPts(customer.getRewardPts() - totalRedeemPts);
                                selectedReward.setRewardQty(selectedReward.getRewardQty() - qty);

                                List<String> newIDs = new ArrayList<>();

                                redemptionList.add(firstRedemption);
                                newIDs.add(firstRedemption.getRedemptionID()); 
                                customerRedemptions.computeIfAbsent(customer.getCustomerID(), k -> new ArrayList<>()).add(firstRedemption.getRedemptionID());

                                for (int i = 1; i < qty; i++) {
                                    Redemption newRedemption = new Redemption(selectedReward);
                                    redemptionList.add(newRedemption);
                                    newIDs.add(newRedemption.getRedemptionID());
                                    customerRedemptions.computeIfAbsent(customer.getCustomerID(), k -> new ArrayList<>()).add(newRedemption.getRedemptionID());
                                }

                                System.out.println("\n  SUCCESS: You have redeemed " + qty + "x [" + selectedReward.getRewardDesc() + "]!");
                                Utils.pause();
                                showCustomerMenu(customer);

                                errorMessage   = "";
                                currentFilter  = "None";
                                isSelectingId  = false;
                                isSelectingQty = false;
                                selectedReward = null;


                            }
                        }
                    } catch (NumberFormatException e) {
                        errorMessage = "Please enter a valid number for quantity!";
                    }
                }
            }
        }
    }

    // ================================================================
    // BANNER
    // ================================================================
    private static void printBanner() {
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |   CINEMA TICKETING & CONCESSION SYSTEM   |");
        System.out.println("  +------------------------------------------+");
    }
}



