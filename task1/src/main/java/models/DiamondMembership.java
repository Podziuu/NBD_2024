package models;

public class DiamondMembership extends Membership {

    public DiamondMembership() {
        this.maxArticles = 15; // Więcej artykułów dla diamentowych członków
        this.discount = 30; // Większa zniżka
    }

    @Override
    public String getClientTypeInfo() {
        return "Diamond Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
