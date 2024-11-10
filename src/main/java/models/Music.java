package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("music")
public class Music extends Item {
    @BsonProperty("vinyl")
    private boolean vinyl;

    @BsonProperty("genre")
    private MusicGenre genre;

    @BsonCreator
    public Music(
            @BsonProperty("base_price") int basePrice,
            @BsonProperty("item_name") String itemName,
            @BsonProperty("vinyl") boolean vinyl,
            @BsonProperty("genre") MusicGenre genre
    ) {
        super(basePrice, itemName, true);
        this.vinyl = vinyl;
        this.genre = genre;
    }

    @BsonIgnore
    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Vinyl: " + (vinyl ? "Yes" : "No");
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public void setVinyl(Boolean vinyl) {
        this.vinyl = vinyl;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }
}