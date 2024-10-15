package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Movie")
public class Movie extends Item {

    private int minutes;

    private boolean casette;

    public Movie(int basePrice, String itemName, int minutes, boolean casette) {
        super(basePrice, itemName);
        this.minutes = minutes;
        this.casette = casette;
    }

    public Movie() {}

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
