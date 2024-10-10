package models;

public class Movie extends Item {
    private int minutes;
    private boolean casette;

    public Movie(String itemId, int basePrice, String itemName, String genre, int minutes, boolean casette) {
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
}
