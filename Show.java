import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Show {
    //data attributes
    private Movie movie;
    private Cinema cinema;
    private Hall hall;
    private String startTime; // Format: "16:00"
    private String startDate; // Format: "2026-07-01"
    private String endTime;
    private String endDate;

    //no-arg constructor
    public Show() {
        this(new Movie(), new Cinema(), new Hall(), "", "");
    }

    //constructor with arguments
    public Show(Movie movie, Cinema cinema, Hall hall, String startTime, String startDate) {
        this.movie = movie;
        this.cinema = cinema;
        this.hall = hall;
        this.startTime = startTime;
        this.startDate = startDate;
        this.endDate = calculateEndDate();
        this.endTime = calculateEndTime(this.startTime);
    }

    //getters
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
        this.endTime = calculateEndTime(this.startTime);
        this.endDate = calculateEndDate();
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        this.endTime = calculateEndTime(this.startTime);  //set new start time will change the end time
        this.endDate = calculateEndDate();  //set new start time will change the end date if it is overnight screening
    }

    //other methods
    @Override
    public String toString() {
        return "Movie: " + movie.getMovieTitle() + 
        "\nCinema: " + cinema.getCinemaName() + 
        "\nHall: " + hall.getHallName() + 
        "\nShowtime: " + startTime + 
        "\nShowdate: " + startDate;
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

    private String calculateEndTime(String startTime) {
        if (this.startTime == null || this.startTime.equals("") || this.movie == null) {
            return "";
        }
        LocalTime start = getStartTimeAsLocalTime(startTime);
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
        LocalDateTime start = getStartDateTime();
        int duration = getDurationInMinutes();
        LocalDateTime end = start.plusMinutes(duration);
        return end.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}