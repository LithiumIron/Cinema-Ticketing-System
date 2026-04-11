import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testbooking {
    public static void main(String[] args) {
        //inititalize data
        List<Cinema> cinemas = new ArrayList<>();
        List<Movie> movieList = new ArrayList<>();
        List<Show> showList = new ArrayList<>();

        cinemas.add(new Cinema("DGV Mid Valley Southkey", "Johor", "Johor Bahru", "81100"));
        cinemas.add(new Cinema("DGV Aman Central", "Kedah", "Alor Setar", "05150"));
        cinemas.add(new Cinema("DGV AEON Mall Kota Bharu", "Kelantan", "Kota Bharu", "15050"));
        cinemas.add(new Cinema("DGV AEON Bandaraya Melaka", "Melaka", "Melaka", "75450"));
        cinemas.add(new Cinema("DGV Palm Mall", "Negeri Sembilan", "Seremban", "70200"));
        cinemas.add(new Cinema("DGV East Coast Mall", "Pahang", "Kuantan", "25000"));
        cinemas.add(new Cinema("DGV Gurney Plaza", "Pulau Pinang", "George Town", "10250"));
        cinemas.add(new Cinema("DGV Ipoh Parade", "Perak", "Ipoh", "30450"));
        cinemas.add(new Cinema("DGV Kangar Jaya Mall", "Perlis", "Kangar", "01000"));
        cinemas.add(new Cinema("DGV Suria Sabah", "Sabah", "Kota Kinabalu", "88000"));
        cinemas.add(new Cinema("DGV CityONE Megamall", "Sarawak", "Kuching", "93350"));
        cinemas.add(new Cinema("DGV Setia City Mall", "Selangor", "Shah Alam", "40170"));
        cinemas.add(new Cinema("DGV Paya Bunga Square", "Terengganu", "Kuala Terengganu", "20100"));
        cinemas.add(new Cinema("DGV Mid Valley Megamall", "Kuala Lumpur", "Kuala Lumpur", "59200"));
        cinemas.add(new Cinema("DGV IOI City Mall", "Putrajaya", "Putrajaya", "62502"));
        cinemas.add(new Cinema("DGV Financial Park", "Labuan", "Labuan", "87000"));
        cinemas.add(new Cinema("DGV Sunway Pyramid", "Selangor", "Petaling Jaya", "47500"));
        cinemas.add(new Cinema("DGV Pavilion KL", "Kuala Lumpur", "Kuala Lumpur", "55100"));
        cinemas.add(new Cinema("DGV Queensbay Mall", "Pulau Pinang", "Bayan Lepas", "11900"));
        cinemas.add(new Cinema("DGV Paradigm Mall JB", "Johor", "Johor Bahru", "81200"));
        cinemas.add(new Cinema("DGV 1 Utama Shopping Centre", "Selangor", "Petaling Jaya", "47800"));
        cinemas.add(new Cinema("DGV KSL City Mall", "Johor", "Johor Bahru", "80250"));
        cinemas.add(new Cinema("DGV AEON Mall Tebrau City", "Johor", "Johor Bahru", "81100"));
        cinemas.add(new Cinema("DGV MyTOWN Shopping Centre", "Kuala Lumpur", "Kuala Lumpur", "55100"));
        cinemas.add(new Cinema("DGV Sunway Carnival Mall", "Pulau Pinang", "Seberang Jaya", "13700"));
        cinemas.add(new Cinema("DGV AEON Mall Bukit Tinggi", "Selangor", "Klang", "41200"));
        cinemas.add(new Cinema("DGV Vivacity Megamall", "Sarawak", "Kuching", "93350"));
        cinemas.add(new Cinema("DGV Imago Shopping Mall", "Sabah", "Kota Kinabalu", "88100"));
        cinemas.add(new Cinema("DGV The Spring", "Sarawak", "Kuching", "93300"));
        cinemas.add(new Cinema("DGV Toppen Shopping Centre", "Johor", "Johor Bahru", "81100"));

        for (Cinema cinema : cinemas) {
            for (char letter = 'A'; letter <= 'J'; letter++) {
                String name = "Hall " + letter;
                String type = (letter <= 'D') ? "Small" : (letter <= 'G' ? "Medium" : "Large");
                Hall newHall = new Hall(name,type);
                cinema.getHalls().add(newHall);
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
    
        for (Cinema cinema : cinemas) {
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
        cinemaLoop(scanner, cinemas, movieList, showList);
    }

        // =======================================================================
    // 1. CINEMA LOOP: 负责选择电影院
    // =======================================================================
    public static void cinemaLoop(Scanner scanner, List<Cinema> cinemas, List<Movie> movieList, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            List<Cinema> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(cinemas);
            } else {
                for (Cinema c : cinemas) {
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

            // Interaction Area
            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.printf("| Filter applied : %-64s |\n", filterText); 
            System.out.printf("| %-81s |\n", "Enter keywords to filter cinemas (Cinema Name, State, City)"); 
            System.out.printf("| %-81s |\n", "(use \"quotes\" for keywords, X to exit, Enter to skip) :"); 

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-74s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
                System.out.printf("| %-81s |\n", ""); 
            } else {
                System.out.printf("| %-81s |\n", "Enter cinema ID to choose cinema (X to refilter the cinemas) :");
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-74s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
            }
            Utils.displayLine();

            // Input Handling
            if (!isSelectingId) {
                System.out.print("\033[4A\r| (use \"quotes\" for keywords, X to exit, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; // 结束程序
                } else if (input.isEmpty()) {
                    isSelectingId = true;
                    errorMessage = "";
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() > 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = "";
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!";
                }
            } else {
                System.out.print("\033[3A\r| Enter cinema ID to choose cinema (X to refilter the cinemas) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false;
                    errorMessage = "";
                } else {
                    Cinema found = null;
                    for (Cinema c : filteredList) {
                        if (c.getCinemaId().equalsIgnoreCase(idInput)) {
                            found = c;
                            break;
                        }
                    }

                    if (found != null) {
                        // 🌟 无缝跳转！不显示 success，直接重置状态并开启 Movie Loop
                        currentFilter = "None";
                        isSelectingId = false;
                        errorMessage = "";
                        
                        movieLoop(scanner, found, movieList, showList);
                        
                        // 当 movieLoop 结束(按X)后，会回到这里继续循环画 Cinema 页面
                    } else {
                        errorMessage = "Invalid cinema ID. Please enter a valid Cinema ID.";
                    }
                }
            }
        }
    }

    // =======================================================================
    // 2. MOVIE LOOP: 负责在选定的影院中选择电影
    // =======================================================================
    public static void movieLoop(Scanner scanner, Cinema selectedCinema, List<Movie> movieList, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        List<Movie> availableMovies = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Show show : showList) {
            // 条件 A：必须是这家电影院的 Show
            if (show.getCinema().getCinemaId().equals(selectedCinema.getCinemaId())) {
                
                // ✅ 你的新改法：直接调用 Show 里面的方法获取 LocalDateTime
                // (假设你的获取日期和时间的 getter 叫 getDateString() 和 getTimeSlot())
                LocalDateTime showDateTime = show.getStartDateTime(show.getStartDate(), show.getStartTime());

                // 条件 B：Show 的时间必须大于或等于当前时间（没过期）
                if (!showDateTime.isBefore(now)) { 
                    Movie m = show.getMovie();
                    // 条件 C：去重
                    if (!availableMovies.contains(m)) {
                        availableMovies.add(m);
                    }
                }
            }
        }

        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // 🌟 新增逻辑 2：基于 availableMovies 进行过滤，而不是全局的 movieList
            List<Movie> filteredList = new ArrayList<>();
            if (currentFilter.equals("None")) {
                filteredList.addAll(availableMovies); // <-- 换成了 availableMovies
            } else {
                for (Movie m : availableMovies) {     // <-- 换成了 availableMovies
                    if (m.getTitle().toLowerCase().contains(currentFilter.toLowerCase()) || 
                        m.getGenre().toLowerCase().contains(currentFilter.toLowerCase())) {
                        filteredList.add(m);
                    }
                }
            }

            // 2. 渲染电影表格
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

            // 3. 渲染交互区 (排版逻辑与 Cinema 一模一样，保持统一)
            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.printf("| Filter applied : %-64s |\n", filterText); 
            System.out.printf("| %-81s |\n", "Enter keywords to filter movies (Movie Title, Genre)"); 
            System.out.printf("| %-81s |\n", "(use \"quotes\" for keywords, X to BACK to cinema, Enter to skip) :"); 

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-74s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
                System.out.printf("| %-81s |\n", ""); 
            } else {
                System.out.printf("| %-81s |\n", "Enter movie ID to choose movie (X to refilter the movies) :");
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-74s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
            }
            Utils.displayLine();

            // 4. Input Handling
            if (!isSelectingId) {
                System.out.print("\033[4A\r| (use \"quotes\" for keywords, X to BACK to cinema, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; 
                } else if (input.isEmpty()) {
                    isSelectingId = true;
                    errorMessage = "";
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() > 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = "";
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!";
                }
            } else {
                System.out.print("\033[3A\r| Enter movie ID to choose movie (X to refilter the movies) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false;
                    errorMessage = "";
                } else {
                    Movie found = null;
                    for (Movie m : filteredList) {
                        if (m.getMovieId().equalsIgnoreCase(idInput)) {
                            found = m;
                            break;
                        }
                    }

                    if (found != null) {
                        currentFilter = "None";
                        isSelectingId = false;
                        errorMessage = "";
                        
                        // 🌟 解锁：跳往第三层！
                        showLoop(scanner, selectedCinema, found, showList);
                        
                        // 注意：这里不用写 scanner.nextLine() 了，因为跳进 showLoop 之后，
                        // 等它 return 出来，整个画面会自动重新刷回 Movie 选择页。
                    } else {
                        errorMessage = "Invalid movie ID. Please enter a valid Movie ID.";
                    }
                }
            }
        }
    }

    // =======================================================================
    // 3. SHOW LOOP: 负责在选定的电影院和电影中，选择播放场次
    // =======================================================================
    public static void showLoop(Scanner scanner, Cinema selectedCinema, Movie selectedMovie, List<Show> showList) {
        String currentFilter = "None";
        String errorMessage = "";     
        boolean isSelectingId = false;

        // 🌟 1. 提取可用的场次 (同一家影院 + 同一部电影 + 未来时间)
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
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // 🌟 2. 过滤逻辑 (搜索日期或时间)
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

            // 🌟 3. 渲染 Show 表格
            Utils.displayLine();

            // 打印标题和表头
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

            // 🌟 4. 渲染交互区
            String filterText = currentFilter.equals("None") ? "None" : "\"" + currentFilter + "\"";
            System.out.printf("| Filter applied : %-64s |\n", filterText); 
            System.out.printf("| %-81s |\n", "Enter keywords to filter shows (Date '2026-07-01' or Time '15:00')"); 
            System.out.printf("| %-81s |\n", "(use \"quotes\" for keywords, X to BACK to movies, Enter to skip) :"); 

            if (!isSelectingId) {
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-74s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
                System.out.printf("| %-81s |\n", ""); 
            } else {
                System.out.printf("| %-81s |\n", "Enter show ID to choose time (X to refilter the shows) :");
                if (!errorMessage.isEmpty()) {
                    System.out.printf("| \033[31mError: %-64s\033[0m |\n", errorMessage);
                } else {
                    System.out.printf("| %-81s |\n", ""); 
                }
            }
            Utils.displayLine();

            // 🌟 5. Input Handling
            if (!isSelectingId) {
                System.out.print("\033[4A\r| (use \"quotes\" for keywords, X to BACK to movies, Enter to skip) : ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("X")) {
                    return; // 退回上一页 (Movie Loop)
                } else if (input.isEmpty()) {
                    isSelectingId = true;
                    errorMessage = "";
                } else if (input.startsWith("\"") && input.endsWith("\"") && input.length() > 2) {
                    currentFilter = input.substring(1, input.length() - 1);
                    errorMessage = "";
                } else {
                    errorMessage = "Please use \"quotes\" for keywords!";
                }
            } else {
                System.out.print("\033[3A\r| Enter show ID to choose time (X to refilter the shows) : ");
                String idInput = scanner.nextLine().trim();

                if (idInput.equalsIgnoreCase("X")) {
                    isSelectingId = false;
                    errorMessage = "";
                } else {
                    Show found = null;
                    for (Show s : filteredList) {
                        if (s.getShowId().equalsIgnoreCase(idInput)) {
                            found = s;
                            break;
                        }
                    }

                    if (found != null) {
                        currentFilter = "None";
                        isSelectingId = false;
                        errorMessage = "";
                        
                        System.out.print("\033[3A\r| \033[32mSuccessfully selected Time: " + found.getStartTime() + " on " + found.getStartDate() + "\033[K\n");
                        System.out.print("| Moving to Seat Selection... Press Enter\033[K");
                        scanner.nextLine();
                        
                        // TODO: 未来在这里调用 seatLoop(scanner, found);
                        
                    } else {
                        errorMessage = "Invalid show ID. Please enter a valid Show ID.";
                    }
                }
            }
        }
    }
}