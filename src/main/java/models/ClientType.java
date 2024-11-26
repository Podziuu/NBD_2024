package models;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ClientType {
    @BsonProperty("max_articles")
    @JsonbProperty("maxArticles")
    protected int maxArticles;
    @BsonProperty("discount")
    @JsonbProperty("discount")
    protected int discount;

    @BsonCreator
    public ClientType(@BsonProperty("max_articles") int maxArticles,
                      @BsonProperty("discount") int discount) {
        this.maxArticles = maxArticles;
        this.discount = discount;
    }

    public ClientType() {

    }

    public int getMaxArticles() {
        return maxArticles;
    }

    public int getDiscount() {
        return discount;
    }

    public void setMaxArticles(int maxArticles) {
        this.maxArticles = maxArticles;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @BsonIgnore
    @JsonbTransient
    public String getClientTypeInfo() {
        return "\nMaksymalna ilość wypożyczonych artykułów: " + this.getMaxArticles();
    }

    public static ClientType createDiamondMembership() {
        return new ClientType(15, 30);
    }

    public static ClientType createMembership() {
        return new ClientType(10, 20);
    }

    public static ClientType createNoMembership() {
        return new ClientType(2, 0);
    }
}