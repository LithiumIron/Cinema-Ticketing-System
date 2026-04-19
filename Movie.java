import java.io.*;//import classess for file i/o
import java.util.*;//import utility classes such as List, ArrayList and Scanner

public class Movie {

    //file used to store movie data permanently
    private static final String FILE_NAME = "movies.txt";
    //filed used to store next available movie ID number
    private static final String COUNTER_FILE = "movie_counter.txt";
    private static List<Movie> movieList = new ArrayList<>();
    private static int nextIdNumber = 1;   // next available ID number (1 -> M001)

    public static final String[] VALID_GENRES = {
        "Action", "Adventure", "Animation", "Comedy", "Drama",
        "Fantasy", "Horror", "Musical", "Romance", "Sci-Fi",
        "Thriller", "War"
    };

    public static final String[] VALID_STATUSES = {
        "Now Showing", "Coming Soon", "Ended"
    };

    // ==================== Data fields ===================
    private String movieId;
    private String title;
    private String genre;
    private String duration;
    private int ageRating;
    private String status;

    //no arg cons
    public Movie(){
        this("","","","", 0,"");
    }

    //full args cons used when loading movie data from file
    public Movie(String movieId, String title, String genre, String duration, int ageRating, String status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.ageRating = ageRating;
        this.status = status;
        nextIdNumber++;
    }
    
    //cons used when creating a new movie
    //movie id is automatically generated
    public Movie(String title, String genre, String duration, int ageRating, String status) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.ageRating = ageRating;
        this.status = status;
        this.movieId = generateNextId();
    }

    // ========== Getter ==========
    public String getMovieId(){return movieId;}
    public String getTitle(){return title;}
    public String getGenre(){return genre;}
    public String getDuration(){return duration;}
    public int getAgeRating(){return ageRating;}
    public String getStatus(){return status;}
    
    public static List<Movie> getAllMovies(){return movieList;}

    // ========= Setter ==========
    public void setTitle(String title){this.title = title;}
    public void setGenre(String genre){this.genre = genre;}
    public void setDuration(String duration){this.duration = duration;}
    public void setAgeRating(int ageRating){this.ageRating = ageRating;}
    public void setStatus(String status){this.status = status;}

    //---to string---
    public String toString() {
        return String.format("ID: %-6s | Title: %-20s | Genre: %-12s | Duration: %6s | Rating: %2d | Status: %-12s",
                getMovieId(), getTitle(), getGenre(), getDuration(), getAgeRating(), getStatus());
    }

    //---generate next id---
    private static String generateNextId() {
        String id = String.format("M%03d", nextIdNumber);//M001,M002,M003
        saveCounter();
        //this method generates a unique movei ID using a counter and 
        //saves the updated counter value to ensure IDs remain unique
        return id;
    }
    
    //---save counter---
    private static void saveCounter() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            bw.write(String.valueOf(nextIdNumber));
        } catch (IOException e) {
            System.out.println("Error saving movie counter.");
        }
    }

    //load movie id counter from file
    private static void loadCounter() {
        File counterFile = new File(COUNTER_FILE);
        if (counterFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(counterFile))) {
                String line = br.readLine();//read 1st line from the file
                if (line != null && !line.trim().isEmpty()) {//"","    " -> empty
                    nextIdNumber = Integer.parseInt(line.trim());//trim(): remove spaces at the beginning and end of a string
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error loading movie counter.");
            }
        }

        //if counter file doesn't exist, calculate from existing movies
        int max = 0;
        for (Movie m : movieList) {
            //extract the numeric part of the movie id
            //example: M005 -> 005
            int num = Integer.parseInt(m.getMovieId().substring(1));
            if (num > max) max = num;
        }
        nextIdNumber = max + 1;
        saveCounter();
    }

    // ==================== FILE OPERATIONS ====================
    
    public static void loadFromFile() {//load movie data from file
        File file = new File(FILE_NAME);//create a object for the movie file
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {//open file and read its content
                String line;
                while ((line = br.readLine()) != null) {//read each line from the file
                    String[] parts = line.split("\\|");//split the line into parts using "|" as delimeter
                    if (parts.length == 6) {//ensure the line contains the correct number of fields
                        movieList.add(new Movie(parts[0], parts[1], parts[2],
                                parts[3],
                                Integer.parseInt(parts[4]), parts[5]));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error loading movies.");
            }
        }

        //load or initializen the movie id counter
        loadCounter();
    }

    //save all movies into file (overwrite mode)
    private static void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Movie m : movieList) {
                bw.write(m.getMovieId() + "|" + m.getTitle() + "|" + m.getGenre() + "|" +
                        m.getDuration() + "|" + m.getAgeRating() + "|" + m.getStatus());
                bw.newLine();//one line one movie
            }
        } catch (Exception e) {
            System.out.println("Error saving movies.");
        }
        saveCounter();
    }

    // =========================== METHODS ===============================

    //---add movie---
    public static void addMovie(Movie m) {
        if (findMovieById(m.getMovieId()) != null) {
            System.out.println("Movie ID " + m.getMovieId() + " already exists. Skipping add.");
            return;
        }
        movieList.add(m);
        saveToFile();
        System.out.println("\nMovie added successfully!");
    }

    //---delete movie---
    public static void deleteMovie(String id) {
        Movie m = findMovieById(id);
        if (m == null) {
            System.out.println("Movie ID not found!");
            return;
        }
        movieList.remove(m);
        saveToFile();
        System.out.println("Movie deleted successfully!");
        // Counter is NOT decreased, so IDs remain unique forever.
    }

    //---find movie by id---
    public static Movie findMovieById(String id) {
        for (Movie m : movieList) {
            if (m.getMovieId().equalsIgnoreCase(id)) return m;//compare id without case sensitivity
        }
        return null;
    }

    //---searches movies by title keyword---
    public static List<Movie> findMoviesByTitle(String keyword) {
        List<Movie> result = new ArrayList<>();
        for (Movie m : movieList) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    //---update movie status only---
    public static void updateMovieStatus(String id, String newStatus) {
        Movie movie = findMovieById(id);
        if (movie == null) {
            System.out.println("Movie ID not found!");
            return;
        }
        movie.setStatus(newStatus);
        saveToFile();
        System.out.println("\nMovie status updated successfully!");
        System.out.println(movie.toString());
    }

    // ==================== DISPLAY METHODS ====================
    
    public static void displayAllMovies() {
        printTable(movieList, "All Movies");
    }

    //---display movie summary---
    public static void displayMovieSummary() {//display number of movies for each genre
        Map<String, Integer> map = new TreeMap<>();//store genre and count in sorted order

        for (Movie m : movieList) {//count number of movies for each genre
            map.put(m.getGenre(), map.getOrDefault(m.getGenre(), 0) + 1);
        }

        System.out.println("================== MOVIE SUMMARY ==================");
        System.out.printf("%-15s | %-6s%n", "Genre", "Count");
        System.out.println("-----------------+--------");

        for (String g : map.keySet()) {//loop through all genres
            System.out.printf("%-15s | %-6d%n", g, map.get(g));
        }

        System.out.println("===================================================");
        System.out.println("\nTotal number of movies: " + movieList.size());
    }

    //---display empty movie row---
    public static void displayEmptyMovieRow() {
        System.out.printf("| %-4s | %-28s | %-19s | %-8s | %-10s |\n", 
                          "-".repeat(4), "-".repeat(28), "-".repeat(19), "-".repeat(8), "-".repeat(10));
    }
    
    // ==================== HELPER METHODS ====================
    private static List<Movie> filterByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        for (Movie m : movieList) {
            if (m.getGenre().equalsIgnoreCase(genre)) result.add(m);
        }
        return result;
    }

    public static void printTable(List<Movie> list, String title) {
        System.out.println("====================================================================================");
        System.out.println("                             " + title.toUpperCase());
        System.out.println("====================================================================================");

        if (list.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        System.out.printf("%-6s | %-20s | %-12s | %-8s | %-6s | %-12s%n",
                "ID", "Title", "Genre", "Duration", "Rating", "Status");
        System.out.println("-------+----------------------+--------------+----------+--------+--------------");

        for (Movie m : list) {
            System.out.printf("%-6s | %-20s | %-12s | %-8s | %-6d | %-12s%n",
                    m.getMovieId(),
                    Utils.truncate(m.getTitle(), 20),//shorten the title to 20 char
                    m.getGenre(),
                    m.getDuration(),
                    m.getAgeRating(),
                    m.getStatus());
        }

        System.out.println("====================================================================================");
        System.out.println("\nTotal number of " + title.toLowerCase() + ": " + list.size());
    }

    // ==================== STATIC HELPER METHODS FOR DRIVER ====================
    public static List<Movie> filterByGenreList(String genre) {
        return filterByGenre(genre);
    }

    public static Set<String> getAllGenres() {
        Set<String> set = new TreeSet<>();
        for (Movie m : movieList) set.add(m.getGenre());
        return set;
    }

    // ==================== UI DISPLAY HELPERS (no input) ====================
    public static void displaySelectMovieHeader(String cinemaName) {
        String safeCinemaName = Utils.truncate(cinemaName, 45);
        String headerText = safeCinemaName + " : Movie Selection";
        int totalSpaces = 83 - headerText.length();
        int padLeft = totalSpaces / 2;
        int padRight = totalSpaces - padLeft; 
        
        System.out.println("|" + " ".repeat(padLeft) + headerText + " ".repeat(padRight) + "|");
    }

    public static void displayMovieInfoHeader() {
        System.out.printf("| %-4s | %-28s | %-19s | %-8s | %-10s |\n", "ID", "Title", "Genre", "Duration", "Age Rating");
    }

    public void displayMovieDetails() {
        String dTitle = Utils.truncate(title, 28);
        String dGenre = Utils.truncate(genre, 19);
        String dDuration = Utils.truncate(duration, 8); 
        System.out.printf("| %-4s | %-28s | %-19s | %-8s | %-10d |\n", movieId, dTitle, dGenre, dDuration, ageRating);
    }
}
