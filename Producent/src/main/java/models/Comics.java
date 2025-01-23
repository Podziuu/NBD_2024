package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("Comics")
public class Comics extends Item {
    @BsonProperty("pageNumber")
    private int pageNumber;

    public Comics(@BsonProperty("basePrice") int basePrice,
                  @BsonProperty("itemName") String itemName,
                  @BsonProperty("pageNumber") int pageNumber) {
        super(basePrice, itemName);
        this.itemType = "comics";
        this.pageNumber = pageNumber;
    }

    public Comics() {

    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}