package models;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Entity
@CqlName("items_by_id")
public class Music extends Item {

    @CqlName("genre")
    private String genreString;

    @CqlName("vinyl")
    private boolean vinyl;

    public Music(UUID id, int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        super(id, basePrice, itemName);
        this.itemType = "music";
        this.genreString = genre.name();
        this.vinyl = vinyl;
    }

    public Music() {}

    public MusicGenre getGenre() {
        return MusicGenre.valueOf(genreString);
    }

    public void setGenre(MusicGenre genre) {
        this.genreString = genre.name();
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}