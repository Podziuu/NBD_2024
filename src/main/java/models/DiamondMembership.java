package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;

@BsonDiscriminator("DiamondMembership")
public class DiamondMembership extends ClientType {

    public DiamondMembership() {
        this.maxArticles = 15; // Więcej artykułów dla diamentowych członków
        this.discount = 30; // Większa zniżka
    }

    @BsonIgnore
    @Override
    public String getClientTypeInfo() {
        return "Diamond Membership: " + this.getMaxArticles() + " articles, discount: " + discount + "%";
    }
}
