package models;

public class ClientType {
    protected int maxArticles;
    protected int discount;

    public ClientType() {
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int applyDiscount(int price) {
        return price;
    }

    public String getClientTypeInfo() {
        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
    }
}
