public class Cinema {
    //data attributes
    private String cinemaName;
    private String state;
    private String city;
    private String postalCode;
    private Hall[] halls;

    //no-arg constructor
    public Cinema() {
        this(" ", " ", " ", " ");
    }

    //constructor with args
    public Cinema(String cinemaName, String state, String city, String postalCode) {
        this.cinemaName = cinemaName;
        this.state = state;
        this.city = city;
        this.postalCode = postalCode;
    }

    //getter
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

    public Hall[] getHalls() {
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

    public void setHalls(Hall[] halls) {
        this.halls = halls;
    }

    //other methods
    @Override
    public String toString() {
        return "Cinema Name: " + cinemaName + 
        "\nState: " + state + 
        "\nCity: " + city + 
        "\nPostal Code: " + postalCode;
    }

    public Boolean IsPostalCodeFormatValid(){
        if (postalCode == null || postalCode.trim().length() != 5) {
            return false;
        }
        return postalCode.trim().matches("\\d{5}");
    }
}
