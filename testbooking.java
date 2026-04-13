import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testbooking {
        private static Scanner scan = new Scanner(System.in);
        private static List<Cinema> cinemaList = new ArrayList<>();
        private static List<Hall> hallList = new ArrayList<>();
        private static List<Movie> movieList = new ArrayList<>();
        private static List<Show> showList = new ArrayList<>();
    public static void main(String[] args) {
        //inititalize data

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

        Scanner scanner = new Scanner(System.in);
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;
        cinemaLoop(scanner, cinemaList, movieList, showList);
    }

    
    // 1. CINEMA LOOP
    public static void cinemaLoop(Scanner scanner, List<Cinema> cinemaList, List<Movie> movieList, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Cinema> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(cinemaList);
            } else {
                for (Cinema c : cinemaList) {
                    if (c.getCinemaName().toLowerCase().contains(currentFilter.toLowerCase()) || 
                        c.getState().toLowerCase().contains(currentFilter.toLowerCase()) || 
                        c.getCity().toLowerCase().contains(currentFilter.toLowerCase())) {
                        filteredList.add(c);
                    }
                }
            }

            Utils.displayLine();
            Cinema.displaySelectCinemaHeader();
            Cinema.displayCinemaInfoHeader();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) {
                    filteredList.get(i).displayCinemaDetails();
                } else {
                    Utils.displayEmptyRow();
                }
            }
            Utils.displayLine();

            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText); 

            // Interaction Area
            if (!isSelectingId) {
                
                // Display Error Row (if an error exists)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Two Instruction Rows (user input happens on the second one)
                System.out.println("  Enter keywords to filter cinemas (Cinema Name, State, City)"); 
                System.out.print("  (use \"quotes\" for keywords, X to exit, Enter to skip) : "); 

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; // Exit directly
                } else if (input.isEmpty()) {
                    isSelectingId = true; // Switch to selecting Cinema ID mode
                    errorMessage = ""; 
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = ""; // Valid keyword, clear error
                    isSelectingId = true;
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!"; // Missing quotes error
                }
                
            } else {
                // Display Error Row (if an invalid ID was entered)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Cinema ID input row
                System.out.print("  Enter cinema ID to choose cinema (X to refilter the cinemas) : ");
                
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false; // Go back to filtering mode
                    errorMessage = ""; 
                    
                }  else if (idInput.isEmpty()) {
                    errorMessage = "Cinema ID cannot be empty!";
                } else {
                    Cinema found = null;
                    for (Cinema c : filteredList) {
                        if (c.getCinemaId().equalsIgnoreCase(idInput)) {
                            found = c;
                            break;
                        }
                    }

                    if (found != null) {
                        errorMessage = "";
                        currentFilter = "None"; // Reset filter for the next time this menu opens
                        isSelectingId = false;
                        movieLoop(scanner, found, movieList, showList); // Enter the movie loop!
                    } else {
                        errorMessage = "Invalid ID. Please enter a valid Cinema ID.";
                    }
                }
            }
        }
    }

    //2. MOVIE LOOP
    public static void movieLoop(Scanner scanner, Cinema selectedCinema, List<Movie> movieList, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        List<Movie> availableMovies = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Show show : showList) {
            if (show.getCinema().getCinemaId().equals(selectedCinema.getCinemaId())) {
                
                LocalDateTime showDateTime = show.getStartDateTime(show.getStartDate(), show.getStartTime());

                if (!showDateTime.isBefore(now)) { 
                    Movie m = show.getMovie();
                    if (!availableMovies.contains(m)) {
                        availableMovies.add(m);
                    }
                }
            }
        }

        while (true) {
            Utils.clearScreen(20);
            System.out.flush();

            List<Movie> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(availableMovies); 
            } else {
                for (Movie m : availableMovies) {     
                    if (m.getTitle().toLowerCase().contains(currentFilter.toLowerCase()) || 
                        m.getGenre().toLowerCase().contains(currentFilter.toLowerCase())) {
                        filteredList.add(m);
                    }
                }
            }

            Utils.displayLine();

            Movie.displaySelectMovieHeader(selectedCinema.getCinemaName());
            Movie.displayMovieInfoHeader();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) {
                    filteredList.get(i).displayMovieDetails(); 
                } else {
                    Movie.displayEmptyMovieRow(); 
                }
            }
            Utils.displayLine();

            // ================= Interaction & Input Handling =================
            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText); 

            // Interaction Area
            if (!isSelectingId) {
                
                // Display Error Row (if an error exists)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Two Instruction Rows (user input happens on the second one)
                System.out.println("  Enter keywords to filter movies (Movie Title, Genre)"); 
                System.out.print("  (use \"quotes\" for keywords, X to BACK to cinema, Enter to skip) : "); 

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; // Exit directly
                } else if (input.isEmpty()) {
                    isSelectingId = true; // Switch to selecting Movie ID mode
                    errorMessage = "";
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = ""; // Valid keyword, clear error
                    isSelectingId = true; 
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!"; // Missing quotes error
                }
                
            } else {
                // Display Error Row (if an invalid ID was entered)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Movie ID input row
                System.out.print("  Enter movie ID to choose movie (X to refilter the movies) : ");
                
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false; // Go back to filtering mode
                    errorMessage = "";
                
                } else if (idInput.isEmpty()) {
                    errorMessage = "Movie ID cannot be empty!";
                } else {
                    Movie found = null;
                    for (Movie m : filteredList) {
                        if (m.getMovieId().equalsIgnoreCase(idInput)) {
                            found = m;
                            break;
                        }
                    }

                    if (found != null) {
                        errorMessage = "";
                        currentFilter = "None"; // Reset filter for the next time this menu opens
                        isSelectingId = false;
                        
                        showLoop(scanner, selectedCinema, found, showList); // Enter the show loop!
                        
                    } else {
                        errorMessage = "Invalid movie ID. Please enter a valid Movie ID.";
                    }
                }
            }
        }
    }


    public static void showLoop(Scanner scanner, Cinema selectedCinema, Movie selectedMovie, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        List<Show> availableShows = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Show show : showList) {
            if (show.getCinema().getCinemaId().equals(selectedCinema.getCinemaId()) && 
                show.getMovie().getMovieId().equals(selectedMovie.getMovieId())) {
                
                LocalDateTime showDateTime = show.getStartDateTime(show.getStartDate(), show.getStartTime());
                if (!showDateTime.isBefore(now)) { 
                    availableShows.add(show);
                }
            }
        }

        while (true) {
            Utils.clearScreen(20); 
            System.out.flush();

            List<Show> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(availableShows);
            } else {
                for (Show s : availableShows) {
                    if (s.getStartDate().contains(currentFilter) || 
                        s.getStartTime().contains(currentFilter)) {
                        filteredList.add(s);
                    }
                }
            }

            Utils.displayLine();

            Show.displaySelectShowHeader(selectedMovie.getTitle());
            Show.displayShowInfoHeader();

            for (int i = 0; i < 10; i++) {
                if (i < filteredList.size()) {
                    filteredList.get(i).displayShowDetails();
                } else {
                    Show.displayEmptyShowRow();
                }
            }
            Utils.displayLine();

            // ================= Interaction & Input Handling =================
            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.println("  Filter applied : " + filterText); 

            // Interaction Area
            if (!isSelectingId) {
                
                // Display Error Row (if an error exists)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Two Instruction Rows (user input happens on the second one)
                System.out.println("  Enter keywords to filter shows (Date '2026-07-01' or Time '15:00')"); 
                System.out.print("  (use \"quotes\" for keywords, X to BACK to movies, Enter to skip) : "); 

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; // Exit directly back to movies
                } else if (input.isEmpty()) {
                    isSelectingId = true; // Switch to selecting Show ID mode
                    errorMessage = "";
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = ""; // Valid keyword, clear error
                    isSelectingId = true; // Switch to selecting ID mode
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!"; // Missing quotes error
                }
                
            } else {
                // Display Error Row (if an invalid ID was entered)
                if (!errorMessage.isEmpty()) {
                    System.out.println("  Error: " + errorMessage);
                }
                
                // Display the Show ID input row
                System.out.print("  Enter show ID to choose time (X to refilter the shows) : ");
                
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false; // Go back to filtering mode
                    errorMessage = "";
                
                } else if (idInput.isEmpty()) {
                    errorMessage = "Show ID cannot be empty!";
                } else {
                    Show found = null;
                    for (Show s : filteredList) {
                        if (s.getShowId().equalsIgnoreCase(idInput)) {
                            found = s;
                            break;
                        }
                    }

                    if (found != null) {
                        errorMessage = "";
                        currentFilter = "None"; // Reset filter for the next time this menu opens
                        isSelectingId = false;
                        
                        // Seat&TicketLoop(scanner, selectedCinema, selectedMovie, found, ...);
                        
                    } else {
                        errorMessage = "Invalid show ID. Please enter a valid Show ID.";
                    }
                }
            }
        }
    }
}
