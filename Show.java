import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Show {
    //data attributes
    private String showId;
    private Movie movie;
    private Cinema cinema;
    private Hall hall;
    private String startTime; // Format: "16:00"
    private String startDate; // Format: "2026-07-01"
    private String endTime;
    private String endDate;
    private static int showCounter = 1;

    //no-arg constructor
    public Show() {
        this(new Movie(), new Cinema(), new Hall(), "", "");
    }

    //constructor with arguments
    public Show(Movie movie, Cinema cinema, Hall hall, String startTime, String startDate) {
        this.showId = generateShowId();
        this.movie = movie;
        this.cinema = cinema;
        this.hall = hall;
        this.startTime = startTime;
        this.startDate = startDate;
        this.endDate = calculateEndDate();
        this.endTime = calculateEndTime();
        showCounter++;
    }

    //getters
    public String getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Hall getHall() {
        return hall;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    //setters
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        this.endTime = calculateEndTime();
        this.endDate = calculateEndDate();
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        this.endTime = calculateEndTime();  //set new start time will change the end time
        this.endDate = calculateEndDate();  //set new start time will change the end date if it is overnight screening
    }

    //other methods
    @Override
    public String toString() {
        return "Movie: " + movie.getTitle() + 
        "\nCinema: " + cinema.getCinemaName() + 
        "\nHall: " + hall.getHallId() + 
        "\nShowtime: " + startTime + 
        "\nShowdate: " + startDate;
    }

    private String generateShowId() {
        return String.format("S%05d", showCounter);
    }

    public LocalDate getStartDateAsLocalDate(String startDate) {
        String start = startDate.trim();
        return LocalDate.parse(start, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public LocalTime getStartTimeAsLocalTime(String startTime) {
        String start = startTime.trim();
        return LocalTime.parse(start, DateTimeFormatter.ofPattern("HH:mm")); 
    }

    public LocalDateTime getStartDateTime(String startDate, String startTime) {
        LocalDate showDate = getStartDateAsLocalDate(startDate); 
        LocalTime showTime = getStartTimeAsLocalTime(startTime); 
        return LocalDateTime.of(showDate, showTime);
    }

    public boolean isDateFormatValid(String startDate) {
        if (startDate == null || startDate.isEmpty()) {
            return false;
        }
        try {
            getStartDateAsLocalDate(startDate);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isTimeFormatValid(String startTime) {
        if (startTime == null || startTime.isEmpty()) {
            return false;
        }
        try {
            getStartTimeAsLocalTime(startTime);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isStartDateNotInPast(String startDate) {
        try {
            LocalDate start = getStartDateAsLocalDate(startDate);
            return !start.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public boolean isStartTimeInFuture(String startDate, String startTime) {
        try {
            LocalDateTime startDateTime = getStartDateTime(startDate, startTime);
            return startDateTime.isAfter(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private int getDurationInMinutes() {
        if (movie == null || movie.getDuration() == null || movie.getDuration().trim().isEmpty()) {
            return 0; 
        }
        int totalMinutes = 0;
        String duration = movie.getDuration().trim();
        String[] parts = duration.split(" ");
        
        for (String part : parts) {
            if (part.contains("h")) {
                totalMinutes += Integer.parseInt(part.replace("h", "")) * 60;
            } else {
                totalMinutes += Integer.parseInt(part.replace("m", ""));
            }
        }
        return totalMinutes;
    }

    private String calculateEndTime() {
        if (this.startTime == null || this.startTime.isEmpty() || this.movie == null) {
            return "";
        }
        LocalTime start = getStartTimeAsLocalTime(this.startTime);
        int duration = getDurationInMinutes();
        LocalTime end = start.plusMinutes(duration);
        return end.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String calculateEndDate() {
        if (this.startDate == null || this.startDate.isEmpty() || 
            this.startTime == null || this.startTime.isEmpty() || 
            this.movie == null) {
            return "";
        }
        LocalDateTime start = getStartDateTime(this.startDate, this.startTime);
        int duration = getDurationInMinutes();
        LocalDateTime end = start.plusMinutes(duration);
        return end.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static void displaySelectShowHeader(String movieTitle) {
        String safeMovieTitle = Utils.truncate(movieTitle, 45); // 放宽名字限制
        String headerText = safeMovieTitle + " : Date/Time Selection";
        
        int totalSpaces = 83 - headerText.length();
        int padLeft = totalSpaces / 2;
        int padRight = totalSpaces - padLeft;
        
        System.out.println("|" + " ".repeat(padLeft) + headerText + " ".repeat(padRight) + "|");
    }

    public static void displayShowInfoHeader() {
        Utils.displayInnerLine();
        System.out.printf("| %-6s | %-20s | %-15s | %-31s |\n", "ID", "Hall Name", "Show Date", "Showtime");
        Utils.displayInnerLine();
    }

    public void displayShowDetails() {
        String dHall = Utils.truncate(hall.getHallId(), 20);
        String dDate = Utils.truncate(startDate, 15);
        String dTime = Utils.truncate(startTime + " - " + endTime, 31);
        System.out.printf("| %-6s | %-20s | %-15s | %-31s |\n", showId, dHall, dDate, dTime);
    }

    public static void displayEmptyShowRow() {
        System.out.printf("| %-6s | %-20s | %-15s | %-31s |\n", 
                          "-".repeat(6), "-".repeat(20), "-".repeat(15), "-".repeat(31));
    }
}
