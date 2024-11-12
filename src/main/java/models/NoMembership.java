package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;

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

    @BsonIgnore
    @Override
    public String getClientTypeInfo() {
        return "No Membership: " + this.getMaxArticles() + " articles, no discount";
    }
}
