package models;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.util.UUID;

@Entity
@CqlName("movies")
public class Movie extends Item {

    @CqlName("minutes")
    private int minutes;

    @CqlName("casette")
    private boolean casette;

    public Movie(UUID id, int basePrice, String itemName, int minutes, boolean casette) {
        super(id, basePrice, itemName);
        this.itemType = "movie";
        this.minutes = minutes;
        this.casette = casette;
    }

    public Movie() {}

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

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Minutes: " + minutes + ", Casette: " + casette;
    }
}