import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Cinemadriver {

    // ================================================================
    // SHARED STATE
    // ================================================================
    private static final DataStorage storage = new DataStorage();
    private static final Scanner      scan    = new Scanner(System.in);

    private static User   currentUser   = null;
    private static int    reportCounter = 1;

    // Lists from mh_main
    private static List<Cinema> cinemaList = new ArrayList<>();
    private static List<Hall>   hallList   = new ArrayList<>();
    private static List<Movie>  movieList  = new ArrayList<>();
    private static List<Show>   showList   = new ArrayList<>();
    private static List<Reward> rewardList = new ArrayList<>();

    // ================================================================
    // ENTRY POINT
    // ================================================================
    public static void main(String[] args) {
        storage.loadAll();
        Movie.loadFromFile();
        Hall.loadFromFile();
        Seat.initializeAllHalls();
        FoodOrder.loadOrderNo();

        initCinemas();
        initMovies();
        initShows();
        initRewards();

        printBanner();

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
    // DATA INITIALISATION
    // ================================================================
    private static void initCinemas() {
        if (!cinemaList.isEmpty()) return;

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

        for (Cinema cinema : cinemaList) {
            for (char letter = 'A'; letter <= 'J'; letter++) {
                String type = (letter <= 'D') ? "Small" : (letter <= 'G' ? "Medium" : "Large");
                Hall newHall = new Hall(type);
                cinema.getHalls().add(newHall);
                hallList.add(newHall);
            }
        }
    }

    private static void initMovies() {
        if (!movieList.isEmpty()) return;

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

    private static void initShows() {
        if (!showList.isEmpty()) return;

        LocalDateTime     today     = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[]          timeSlots = {"09:00", "12:00", "15:00", "18:00", "21:00", "00:00"};

        for (Cinema cinema : cinemaList) {
            for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
                String dateString = today.plusDays(dayOffset).format(formatter);
                for (String timeSlot : timeSlots) {
                    for (int j = 0; j < 10; j++) {
                        showList.add(new Show(
                            movieList.get(j),
                            cinema,
                            cinema.getHalls().get(j),
                            timeSlot,
                            dateString));
                    }
                }
            }
        }
    }

    private static void initRewards() {
        if (!rewardList.isEmpty()) return;

        rewardList.add(new Discount("10% off on next booking", 100, 10, 10.0));
        rewardList.add(new Discount("15% off on next booking", 150, 10, 15.0));
        rewardList.add(new Discount("20% off on next booking", 200, 10, 20.0));
        rewardList.add(new Discount("25% off on next booking", 250, 10, 25.0));
        rewardList.add(new Discount("50% off on next booking", 500, 10, 50.0));
        rewardList.add(new Freebie("Free Popcorn",    60, 10, Map.of("Popcorn",    2)));
        rewardList.add(new Freebie("Free Hotdog",     35, 10, Map.of("Hotdog",     1)));
        rewardList.add(new Freebie("Free Cola",       40, 10, Map.of("Cola",       2)));
        rewardList.add(new Freebie("Free Sweet Corn", 70, 10, Map.of("Sweet Corn", 3)));
        rewardList.add(new Freebie("Free Nuggets",    30, 10, Map.of("Nuggets",    1)));
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
    private static void showCustomerMenu(Customer customer) {
        customer.displayMenu();
        int choice = readInt("  Select option: ", 0, 2);

        switch (choice) {
            case 1: viewProfile(customer);            break;
            case 2: handleDeleteOwnAccount(customer); break;
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
    // MANAGEMENT MENU (staff only - movies, cinemas, halls, shows, rewards)
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
            System.out.println("0. Back");
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
                : "Invalid postal code! Must be 5 digits.");
            System.out.print("Postal Code: ");
            postalCode = scan.nextLine().trim();
        }

        for (Cinema c : cinemaList) {
            if (c.getCinemaName().equalsIgnoreCase(name) &&
                c.getState().equalsIgnoreCase(state)     &&
                c.getCity().equalsIgnoreCase(city)       &&
                c.getPostalCode().equals(postalCode)) {
                System.out.println("Failed: This cinema already exists!");
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

        System.out.print("New Cinema Name (blank = keep): ");
        String name = scan.nextLine().trim();
        if (!name.isEmpty()) c.setCinemaName(name);

        System.out.print("New State (blank = keep): ");
        String state = scan.nextLine().trim();
        if (!state.isEmpty()) c.setState(state);

        System.out.print("New City (blank = keep): ");
        String city = scan.nextLine().trim();
        if (!city.isEmpty()) c.setCity(city);

        System.out.print("New Postal Code (blank = keep): ");
        String postalCode = scan.nextLine().trim();
        if (!postalCode.isEmpty()) {
            while (!Cinema.IsPostalCodeFormatValid(postalCode)) {
                System.out.println("Invalid postal code! Must be 5 digits.");
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

        System.out.print("Are you sure? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            cinemaList.remove(c);
            System.out.println("Cinema deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        Utils.pause();
    }

    private static void searchCinema() {
        Utils.clearScreen(20);
        System.out.print("Enter keyword: ");
        String key = scan.nextLine().trim().toLowerCase();

        List<Cinema> results = new ArrayList<>();
        for (Cinema c : cinemaList) {
            if (c.getCinemaName().toLowerCase().contains(key) ||
                c.getState().toLowerCase().contains(key)      ||
                c.getCity().toLowerCase().contains(key)       ||
                c.getPostalCode().contains(key))
                results.add(c);
        }

        if (results.isEmpty()) System.out.println("No results found.");
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
            System.out.println("6. View Movie Summary");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 6);
            switch (choice) {
                case 1: addMovie();                    break;
                case 2: Movie.displayMoviesMenu(scan); break;
                case 3: updateMovie();                 break;
                case 4: deleteMovie();                 break;
                case 5: searchMovie();                 break;
                case 6:
                    Utils.clearScreen(20);
                    Movie.displayMovieSummary();
                    Utils.pause();
                    break;
                case 0: back = true; break;
            }
        }
    }

    private static void addMovie() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Movie ---");

        System.out.print("Title: ");
        String title = scan.nextLine().trim();
        while (title.isEmpty()) {
            System.out.println("Title cannot be empty!");
            System.out.print("Title: ");
            title = scan.nextLine().trim();
        }

        String genre    = Movie.selectGenre(scan);
        System.out.print("Duration (e.g. 2h 30m): ");
        String duration = scan.nextLine().trim();
        System.out.print("Age Rating: ");
        int    rating   = Utils.getIntRange(scan, 0, 21);
        String status   = Movie.selectStatus(scan);

        Movie m = new Movie(title, genre, duration, rating, status);
        Movie.addMovie(m);
        movieList.add(m);
        System.out.println("\nMovie added:\n" + m);
        Utils.pause();
    }

    private static void updateMovie() {
        System.out.print("Enter Movie ID: ");
        String id = scan.nextLine().trim();
        Movie.updateMovie(id, scan);
    }

    private static void deleteMovie() {
        Utils.clearScreen(20);
        Movie.displayAllMovies();
        System.out.print("Enter Movie ID to delete: ");
        String id = scan.nextLine().trim();
        System.out.print("Are you sure? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) Movie.deleteMovie(id);
        else System.out.println("Deletion cancelled.");
        Utils.pause();
    }

    private static void searchMovie() {
        Utils.clearScreen(20);
        System.out.print("Enter keyword: ");
        String      key     = scan.nextLine().trim();
        List<Movie> results = Movie.findMoviesByTitle(key);
        if (results.isEmpty()) System.out.println("No results found.");
        else for (Movie m : results) System.out.println(m);
        Utils.pause();
    }

    private static Movie findMovieById(String id) {
        for (Movie m : movieList)
            if (m.getMovieId().equalsIgnoreCase(id)) return m;
        return null;
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
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 5);
            switch (choice) {
                case 1: addHall();                          break;
                case 2: Hall.displayHallSummary(scan);      break;
                case 3: Hall.deleteHallPrompt(scan);        break;
                case 4: Hall.displaySeatMapMenu(scan);      break;
                case 5: Hall.displayAllHallsDetailed(scan); break;
                case 0: back = true;                        break;
            }
        }
    }

    private static void addHall() {
        Utils.clearScreen(20);
        System.out.println("--- Select Hall Type ---");
        System.out.println("1. Small");
        System.out.println("2. Medium");
        System.out.println("3. Large");
        System.out.print("Enter choice: ");
        Hall.addHallByType(Utils.getIntRange(scan, 1, 3));
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
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");

            int choice = Utils.getIntRange(scan, 0, 4);
            switch (choice) {
                case 1: addShow();    break;
                case 2: updateShow(); break;
                case 3: deleteShow(); break;
                case 4: searchShow(); break;
                case 0: back = true;  break;
            }
        }
    }

    private static void addShow() {
        Utils.clearScreen(20);
        System.out.println("--- Add New Show ---");

        System.out.print("Enter Cinema ID: ");
        String cinemaId       = scan.nextLine().trim();
        Cinema selectedCinema = findCinemaById(cinemaId);
        while (cinemaId.isEmpty() || selectedCinema == null) {
            System.out.println(cinemaId.isEmpty() ? "Cinema ID cannot be empty!" : "Cinema not found!");
            System.out.print("Enter Cinema ID: ");
            cinemaId       = scan.nextLine().trim();
            selectedCinema = findCinemaById(cinemaId);
        }

        System.out.print("Enter Hall ID: ");
        String hallId      = scan.nextLine().trim();
        Hall   selectedHall = findHallInCinema(selectedCinema, hallId);
        while (hallId.isEmpty() || selectedHall == null) {
            System.out.println(hallId.isEmpty() ? "Hall ID cannot be empty!" : "Hall not found in this cinema!");
            System.out.print("Enter Hall ID: ");
            hallId       = scan.nextLine().trim();
            selectedHall = findHallInCinema(selectedCinema, hallId);
        }

        System.out.print("Enter Movie ID: ");
        String movieId       = scan.nextLine().trim();
        Movie  selectedMovie = findMovieById(movieId);
        while (movieId.isEmpty() || selectedMovie == null) {
            System.out.println(movieId.isEmpty() ? "Movie ID cannot be empty!" : "Movie not found!");
            System.out.print("Enter Movie ID: ");
            movieId       = scan.nextLine().trim();
            selectedMovie = findMovieById(movieId);
        }

        System.out.print("Enter Show Date (yyyy-MM-dd): ");
        String showDate = scan.nextLine().trim();
        while (showDate.isEmpty() || !Show.isDateFormatValid(showDate) || !Show.isStartDateNotInPast(showDate)) {
            System.out.println(showDate.isEmpty() ? "Date cannot be empty!"
                : !Show.isDateFormatValid(showDate) ? "Invalid format! Use yyyy-MM-dd."
                : "Date cannot be in the past!");
            System.out.print("Enter Show Date (yyyy-MM-dd): ");
            showDate = scan.nextLine().trim();
        }

        System.out.print("Enter Show Time (HH:mm): ");
        String showTime = scan.nextLine().trim();
        while (showTime.isEmpty() || !Show.isTimeFormatValid(showTime) || !Show.isStartTimeInFuture(showDate, showTime)) {
            System.out.println(showTime.isEmpty() ? "Time cannot be empty!"
                : !Show.isTimeFormatValid(showTime) ? "Invalid format! Use HH:mm."
                : "Show time must be in the future!");
            System.out.print("Enter Show Time (HH:mm): ");
            showTime = scan.nextLine().trim();
        }

        for (Show s : showList) {
            if (s.getCinema().getCinemaId().equalsIgnoreCase(cinemaId) &&
                s.getHall().getHallId().equalsIgnoreCase(hallId)       &&
                s.getStartDate().equals(showDate)                      &&
                s.getStartTime().equals(showTime)) {
                System.out.println("Failed: A show already exists at this cinema, hall, date and time!");
                Utils.pause();
                return;
            }
        }

        Show newShow = new Show(selectedMovie, selectedCinema, selectedHall, showTime, showDate);
        showList.add(newShow);
        System.out.println("\nShow added successfully:\n" + newShow);
        Utils.pause();
    }

    private static void updateShow() {
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to update: ");
        String id = scan.nextLine().trim();

        Show s = findShowById(id);
        if (s == null) { System.out.println("Show not found."); Utils.pause(); return; }

        System.out.println("\nCurrent details:\n" + s);

        System.out.print("New Cinema ID (blank = keep): ");
        String cinemaId = scan.nextLine().trim();
        if (!cinemaId.isEmpty()) {
            Cinema c = findCinemaById(cinemaId);
            if (c != null) s.setCinema(c);
            else System.out.println("Cinema not found. Keeping current.");
        }

        System.out.print("New Hall ID (blank = keep): ");
        String hallId = scan.nextLine().trim();
        if (!hallId.isEmpty()) {
            Hall h = findHallInCinema(s.getCinema(), hallId);
            if (h != null) s.setHall(h);
            else System.out.println("Hall not found. Keeping current.");
        }

        System.out.print("New Movie ID (blank = keep): ");
        String movieId = scan.nextLine().trim();
        if (!movieId.isEmpty()) {
            Movie m = findMovieById(movieId);
            if (m != null) s.setMovie(m);
            else System.out.println("Movie not found. Keeping current.");
        }

        System.out.print("New Show Date yyyy-MM-dd (blank = keep): ");
        String showDate = scan.nextLine().trim();
        if (!showDate.isEmpty()) {
            if (Show.isDateFormatValid(showDate) && Show.isStartDateNotInPast(showDate))
                s.setStartDate(showDate);
            else System.out.println("Invalid date. Keeping current.");
        }

        System.out.print("New Show Time HH:mm (blank = keep): ");
        String showTime = scan.nextLine().trim();
        if (!showTime.isEmpty()) {
            if (Show.isTimeFormatValid(showTime) && Show.isStartTimeInFuture(s.getStartDate(), showTime))
                s.setStartTime(showTime);
            else System.out.println("Invalid time. Keeping current.");
        }

        System.out.println("\nShow updated successfully:\n" + s);
        Utils.pause();
    }

    private static void deleteShow() {
        Utils.clearScreen(20);
        System.out.print("Enter Show ID to delete: ");
        String id = scan.nextLine().trim();

        Show s = findShowById(id);
        if (s == null) { System.out.println("Show not found."); Utils.pause(); return; }

        System.out.print("Are you sure? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            showList.remove(s);
            System.out.println("Show deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        Utils.pause();
    }

    private static void searchShow() {
        Utils.clearScreen(20);

        System.out.print("Enter Movie ID (e.g. M001): ");
        String movieId = scan.nextLine().trim();
        while (!movieId.matches("(?i)^M\\d{3}$")) {
            System.out.println("Invalid format. Use M001.");
            System.out.print("Enter Movie ID: ");
            movieId = scan.nextLine().trim();
        }

        System.out.print("Enter Cinema ID (e.g. C001): ");
        String cinemaId = scan.nextLine().trim();
        while (!cinemaId.matches("(?i)^C\\d{3}$")) {
            System.out.println("Invalid format. Use C001.");
            System.out.print("Enter Cinema ID: ");
            cinemaId = scan.nextLine().trim();
        }

        System.out.print("Enter Date (yyyy-MM-dd): ");
        String date = scan.nextLine().trim();
        while (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Invalid format. Use yyyy-MM-dd.");
            System.out.print("Enter Date: ");
            date = scan.nextLine().trim();
        }

        List<Show> results = new ArrayList<>();
        for (Show s : showList) {
            if (s.getMovie().getMovieId().equalsIgnoreCase(movieId)     &&
                s.getCinema().getCinemaId().equalsIgnoreCase(cinemaId)  &&
                s.getStartDate().equals(date))
                results.add(s);
        }

        if (results.isEmpty()) System.out.println("No shows found.");
        else for (Show s : results) System.out.println(s + "\n--------------------------");
        Utils.pause();
    }

    private static Show findShowById(String id) {
        for (Show s : showList)
            if (s.getShowId().equalsIgnoreCase(id)) return s;
        return null;
    }

    private static Hall findHallInCinema(Cinema cinema, String hallId) {
        if (cinema == null) return null;
        for (Hall h : cinema.getHalls())
            if (h.getHallId().equalsIgnoreCase(hallId)) return h;
        return null;
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
            System.out.println("0. Back");
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
        System.out.println("1. Discount");
        System.out.println("2. Freebie");
        System.out.print("Enter choice: ");
        int type = Utils.getIntRange(scan, 1, 2);

        System.out.print("Reward Name: ");
        String name = scan.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            System.out.print("Reward Name: ");
            name = scan.nextLine().trim();
        }

        System.out.print("Reward Points required: ");
        int points = scan.nextInt();
        while (points <= 0) { System.out.print("Must be positive: "); points = scan.nextInt(); }

        System.out.print("Reward Quantity: ");
        int quantity = scan.nextInt();
        while (quantity <= 0) { System.out.print("Must be positive: "); quantity = scan.nextInt(); }

        if (type == 1) {
            System.out.print("Discount Percentage: ");
            double pct = scan.nextDouble();
            while (pct <= 0 || pct >= 100) { System.out.print("Must be 1-99: "); pct = scan.nextDouble(); }
            scan.nextLine();
            Reward r = new Discount(name, points, quantity, pct);
            rewardList.add(r);
            System.out.println("\nDiscount reward added:\n" + r);
        } else {
            scan.nextLine();
            HashMap<String, Integer> freebieMap = new HashMap<>();
            boolean cont = true;
            while (cont) {
                System.out.print("Freebie item name: ");
                String item = scan.nextLine().trim();
                while (item.isEmpty()) { System.out.print("Cannot be empty: "); item = scan.nextLine().trim(); }
                System.out.print("Quantity for " + item + ": ");
                int qty = scan.nextInt();
                while (qty <= 0) { System.out.print("Must be positive: "); qty = scan.nextInt(); }
                scan.nextLine();
                freebieMap.put(item, qty);
                System.out.print("Add more items? (y/n): ");
                cont = scan.nextLine().trim().equalsIgnoreCase("y");
            }
            Reward r = new Freebie(name, points, quantity, freebieMap);
            rewardList.add(r);
            System.out.println("\nFreebie reward added:\n" + r);
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

        System.out.print("New Name (blank = keep): ");
        String name = scan.nextLine().trim();
        if (!name.isEmpty()) r.setRewardDesc(name);

        System.out.print("New Points (blank = keep): ");
        String pts = scan.nextLine().trim();
        if (!pts.isEmpty()) {
            try {
                int p = Integer.parseInt(pts);
                if (p > 0) r.setRewardPts(p);
                else System.out.println("Must be positive. Keeping current.");
            } catch (NumberFormatException e) { System.out.println("Invalid. Keeping current."); }
        }

        System.out.print("New Quantity (blank = keep): ");
        String qty = scan.nextLine().trim();
        if (!qty.isEmpty()) {
            try {
                int q = Integer.parseInt(qty);
                if (q > 0) r.setRewardQty(q);
                else System.out.println("Must be positive. Keeping current.");
            } catch (NumberFormatException e) { System.out.println("Invalid. Keeping current."); }
        }

        if (r.getRewardType().equalsIgnoreCase("Discount")) {
            System.out.print("New Discount % (blank = keep): ");
            String pct = scan.nextLine().trim();
            if (!pct.isEmpty()) {
                try {
                    double p = Double.parseDouble(pct);
                    if (p > 0 && p < 100) ((Discount) r).setPercentage(p);
                    else System.out.println("Must be 1-99. Keeping current.");
                } catch (NumberFormatException e) { System.out.println("Invalid. Keeping current."); }
            }
        } else if (r.getRewardType().equalsIgnoreCase("Freebie")) {
            Freebie fr = (Freebie) r;
            Map<String, Integer> map = new HashMap<>(fr.getFreebieItems());
            System.out.print("Freebie item to update (blank = skip): ");
            String item = scan.nextLine().trim();
            if (!item.isEmpty() && map.containsKey(item)) {
                System.out.print("New quantity for " + item + ": ");
                String q = scan.nextLine().trim();
                try {
                    int newQty = Integer.parseInt(q);
                    if (newQty > 0) { map.put(item, newQty); fr.setFreebieItems(map); }
                } catch (NumberFormatException e) { System.out.println("Invalid. Keeping current."); }
            }
        }

        System.out.println("\nReward updated:\n" + r);
        Utils.pause();
    }

    private static void deleteReward() {
        Utils.clearScreen(20);
        System.out.print("Enter Reward ID to delete: ");
        String id = scan.nextLine().trim();

        Reward r = findRewardById(id);
        if (r == null) { System.out.println("Reward not found."); Utils.pause(); return; }

        System.out.print("Are you sure? (y/n): ");
        if (scan.nextLine().trim().equalsIgnoreCase("y")) {
            r.setRewardStatus("Delisted");
            System.out.println("Reward delisted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
        Utils.pause();
    }

    private static void searchReward() {
        Utils.clearScreen(20);
        System.out.print("Enter keyword: ");
        String key = scan.nextLine().trim().toLowerCase();

        List<Reward> results = new ArrayList<>();
        for (Reward r : rewardList)
            if (r.getRewardDesc().toLowerCase().contains(key)) results.add(r);

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
    // BANNER
    // ================================================================
    private static void printBanner() {
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |   CINEMA TICKETING & CONCESSION SYSTEM   |");
        System.out.println("  +------------------------------------------+");
    }
}
