package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Movie")
public class Movie extends Item {

    @Column(name = "movie_duration")
    private int minutes;

    @Column(name = "is_cassette")
    private boolean casette;

    public Movie() {}

    public Movie(Long itemId, int basePrice, String itemName, String genre, int minutes, boolean casette) {
        super(itemId, basePrice, itemName);
        this.minutes = minutes;
        this.casette = casette;
    }

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

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setCasette(boolean casette) {
        this.casette = casette;
    }
}
