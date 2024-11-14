package models;

import org.bson.codecs.pojo.annotations.*;

@BsonDiscriminator("Movie")
public class Movie extends Item {
    @BsonProperty
    private int minutes;
    @BsonProperty
    private boolean casette;

    public Movie(@BsonProperty int basePrice,
                 @BsonProperty String itemName,
                 @BsonProperty int minutes,
                 @BsonProperty boolean casette) {
        super(basePrice, itemName);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public Movie() {

    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean isCasette() {
        return casette;
    }

    public void setCasette(boolean casette) {
        this.casette = casette;
    }
}