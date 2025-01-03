package models;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Entity
@CqlName("music_items")
public class Music extends Item {
    @CqlName("genre")
    private int genreValue;

    @CqlName("vinyl")
    private boolean vinyl;

    public Music(UUID id, int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        super(id, basePrice, itemName);
        this.itemType = "music";
        this.genreValue = genre.getValue();
        this.vinyl = vinyl;
    }

    public Music() {}

    public int getGenreValue() {
        return genreValue;
    }

    public MusicGenre getGenre() {
        return MusicGenre.fromValue(genreValue);
    }

    public void setGenre(MusicGenre genre) {
        this.genreValue = genre.getValue();
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}