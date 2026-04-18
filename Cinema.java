import java.util.ArrayList;

import org.jcp.xml.dsig.internal.dom.Utils;

public class Cinema {
    //data properties
    private String cinemaId;
    private String cinemaName;
    private String state;
    private String city;
    private String postalCode;
    private ArrayList<Hall> halls;
    private static int cinemaCounter = 1;


    //no-arg constructor
    public Cinema() {
        this(" ", " ", " ", " ");
    }

    //constructor with args
    public Cinema(String cinemaName, String state, String city, String postalCode) {
        this.cinemaId = generateCinemaId();
        this.cinemaName = cinemaName;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
        this.halls = new ArrayList<>();
        cinemaCounter++;
    }

    //getter

    public String getCinemaId(){
        return cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }

    //setter
    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setHalls(ArrayList<Hall> halls) {
        this.halls = halls;
    }

    //other methods
    public String toString() {
        return "Cinema ID: " + cinemaId +
        "\nCinema Name: " + cinemaName + 
        "\nState: " + state + 
        "\nCity: " + city + 
        "\nPostal Code: " + postalCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cinema)) return false;
        return cinemaId.equals(((Cinema) obj).cinemaId);
    }

    private String generateCinemaId() {
        return String.format("C%03d", cinemaCounter);
    }

    public static Boolean IsPostalCodeFormatValid(String postalCode){
        if (postalCode.trim().length() != 5) {
            return false;
        }
        return postalCode.trim().matches("\\d{5}");
    }

    public void addHall(Hall newHall) {
        this.halls.add(newHall);
    }

    public static void displaySelectCinemaHeader() {
        System.out.println("|" + " ".repeat(33) + "Cinema Selection" + " ".repeat(34) + "|");
    }

    public static void displayCinemaInfoHeader() {
        System.out.printf("| %-4s | %-30s | %-15s | %-23s |\n", "ID", "Cinema Name", "State", "City");
    }

    public void displayCinemaDetails() {
        String cinemaName = Utils.truncate(this.cinemaName, 30);
        String city = Utils.truncate(this.city, 23);
        System.out.printf("| %-4s | %-30s | %-15s | %-23s |\n", cinemaId, cinemaName, state, city);
    }
}
