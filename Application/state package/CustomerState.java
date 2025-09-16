package bookstoreapp.state;

public interface CustomerState {
    int calculatePoints(double amount);
    double getDiscount(int points);
    String getStatus();
}