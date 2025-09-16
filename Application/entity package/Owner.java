package bookstoreapp.entity;

import java.io.Serializable;

public class Owner implements Serializable {
    private String username;
    private String password;
    
    public Owner() {
        this.username = "admin";
        this.password = "admin";
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}