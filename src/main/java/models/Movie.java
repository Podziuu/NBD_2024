package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Movie") // Użycie typu dziedziczenia, aby rozróżnić rodzaje Item
public class Movie extends Item {

    @Column(name = "movie_duration") // Nazwa kolumny w bazie danych
    private int minutes; // Czas trwania filmu

    @Column(name = "is_cassette") // Nazwa kolumny w bazie danych
    private boolean casette; // Informacja, czy to kaseta

    // Konstruktor
    public Movie(long itemId, int basePrice, String itemName, int minutes, boolean casette) {
        super(itemId, basePrice, itemName); // Użycie konstruktora klasy bazowej
        this.minutes = minutes;
        this.casette = casette;
    }

    // Domyślny konstruktor
    public Movie() {}

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Minutes: " + minutes + ", Casette: " + (casette ? "Yes" : "No");
    }

    // Gettery i settery
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
