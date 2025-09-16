package bookstoreapp.entity;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private double price;
    
    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public String toString() {
        return name + "," + price;
    }
    
    private boolean selected;

    
     public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}