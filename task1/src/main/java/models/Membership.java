package models;

public class Membership extends ClientType {

    public Membership() {
        this.maxArticles = 10; // np. ustawiamy maksymalną ilość artykułów dla członków
        this.discount = 20; // np. zniżka w procentach dla członków
    }

    @Override
    public int applyDiscount(int price) {
        return price - (price * discount / 100);
    }

    @Override
    public String getClientTypeInfo() {
        return "Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
