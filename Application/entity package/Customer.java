package bookstoreapp.entity;

import bookstoreapp.state.CustomerState;
import bookstoreapp.state.GoldState;
import bookstoreapp.state.SilverState;
import java.io.Serializable;

public class Customer implements Serializable {
    private String username;
    private String password;
    private int points;
    private CustomerState state;
    
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.points = 0;
        this.state = new SilverState();
    }
    
    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPoints() { return points; }
    public CustomerState getState() { return state; }
    
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setPoints(int points) { 
        this.points = points;
        updateStatus();
    }
    
    public void setState(CustomerState state) {
        this.state = state;
    }
    
    public void updateStatus() {
        if (points >= 1000 && !(state instanceof GoldState)) {
            setState(new GoldState());
        } else if (points < 1000 && !(state instanceof SilverState)) {
            setState(new SilverState());
        }
    }
    
    public void addPoints(double amount) {
        points += state.calculatePoints(amount);
        updateStatus();
    }
    
    public double redeemPoints(double amount) {
        // Calculate maximum possible discount (1 CAD per 100 points)
        double maxDiscount = points / 100.0;
    
      // Determine actual discount to apply
        double discountToApply = Math.min(maxDiscount, amount);
    
     // Deduct points used (100 points per 1 CAD)
        int pointsUsed = (int)(discountToApply * 100);
        points -= pointsUsed;
    
        updateStatus();
        return discountToApply;
    }
    
    @Override
    public String toString() {
        return username + "," + password + "," + points;
    }
}