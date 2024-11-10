package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("comics")
public class Comics extends Item {
    @BsonProperty("pages_number")
    private int pagesNumber;

    @BsonCreator
    public Comics(
            @BsonProperty("base_price") int basePrice,
            @BsonProperty("item_name") String itemName,
            @BsonProperty("pages_number") int pagesNumber
    ) {
        super(basePrice, itemName, true);
        this.pagesNumber = pagesNumber;
    }

    @BsonIgnore
    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Pages: " + pagesNumber;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(Integer pagesNumber) {
        this.pagesNumber = pagesNumber;
    }
}
