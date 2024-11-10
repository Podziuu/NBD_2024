package models;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;

@BsonDiscriminator("movie")
public class Movie extends Item {
    @BsonProperty("minutes")
    private int minutes;
    @BsonProperty("casette")
    private boolean casette;

    @BsonCreator
    public Movie(
            @BsonProperty("base_price") int basePrice,
            @BsonProperty("item_name") String itemName,
            @BsonProperty("minutes") int minutes,
            @BsonProperty("casette") boolean casette
    ) {
        super(basePrice, itemName, true);
        this.minutes = minutes;
        this.casette = casette;
    }

    @BsonIgnore
    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Minutes: " + minutes + ", Casette: " + (casette ? "Yes" : "No");
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean isCasette() {
        return casette;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public void setCasette(Boolean casette) {
        this.casette = casette;
    }
}