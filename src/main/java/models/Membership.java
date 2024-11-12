package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;

@BsonDiscriminator("Membership")
public class Membership extends ClientType {

    public Membership() {
        this.maxArticles = 10; // np. ustawiamy maksymalną ilość artykułów dla członków
        this.discount = 20; // np. zniżka w procentach dla członków
    }

    @Override
    public int applyDiscount(int price) {
        return price - (price * discount / 100);
    }

    @BsonIgnore
    @Override
    public String getClientTypeInfo() {
        return "Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
