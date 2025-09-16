package bookstoreapp.state;

public class GoldState implements CustomerState {
    @Override
    public int calculatePoints(double amount) {
        return (int)(amount * 10);
    }
    
    @Override
    public double getDiscount(int points) {
        return points / 100.0;
    }
    
    @Override
    public String getStatus() {
        return "Gold";
    }
}