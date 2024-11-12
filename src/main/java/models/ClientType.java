package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("clientType")
public class ClientType {
    @BsonProperty("max_articles")
    protected int maxArticles;
    @BsonProperty("discount")
    protected int discount;

    @BsonCreator
    public ClientType() {
    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public int applyDiscount(int price) {
        return price;
    }

    @BsonIgnore
    public String getClientTypeInfo() {
        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
    }
}
