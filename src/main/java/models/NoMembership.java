package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator("NoMembership")
public class NoMembership extends ClientType {

    public NoMembership() {
        this.maxArticles = 5; // Mniejsza liczba artykułów dla nieczłonków
        this.discount = 0; // Brak zniżki
    }

    @Override
    public int applyDiscount(int price) {
        return price; // Brak zniżki, zwracamy pełną cenę
    }

    @Override
    public String getClientTypeInfo() {
        return "No Membership: " + this.getMaxArticles() + " articles, no discount";
    }
}
