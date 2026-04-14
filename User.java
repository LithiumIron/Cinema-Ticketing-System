public abstract class User {
 
    private String userID;
    private String username;
    private String password;
    private String role;
    
    public User(){
    	   this(" ", " ", " ", " ");
    }
    public User(String userID, String username, String password, String role) {
        this.userID   = userID;
        this.username = username;
        this.password = password;
        this.role     = role;
    }
 
    // ---------- Getters ----------
    public String getUserID()   { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }
 
    // ---------- Setters ----------
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
 
    // ---------- Core methods ----------
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
 
    public boolean registerUser(String username, String password) {
        if (username == null || username.isBlank() ||
            password == null || password.length() < 6) {
            return false;
        }
        this.username = username;
        this.password = password;
        return true;
    }
 
    public void logout() {
        System.out.println(username + " has logged out.");
    }
 
    // ---------- Abstract methods (subclasses must implement) ----------
    public abstract void displayMenu();
    public abstract String getDisplayName();
 
    // ---------- Polymorphism ----------
    public String toString() {
        return String.format("User[%s | %s | Role: %s]", userID, username, role);
    }
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User u = (User) obj;
        return userID.equals(u.userID);
    }
}
