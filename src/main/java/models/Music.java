package models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator("Music")
public class Music extends Item {
    @BsonProperty
    private MusicGenre genre;
    @BsonProperty
    private boolean vinyl;

    public Music(@BsonProperty int basePrice,
                 @BsonProperty String itemName,
                 @BsonProperty MusicGenre genre,
                 @BsonProperty boolean vinyl) {
        super(basePrice, itemName);
        this.itemType = "movie";
        this.genre = genre;
        this.vinyl = vinyl;
    }

    public Music() {

    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}