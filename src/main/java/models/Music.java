package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Music") // Użycie typu dziedziczenia, aby rozróżnić rodzaje Item
public class Music extends Item {

    @Enumerated(EnumType.STRING) // Przechowywanie jako string
    private MusicGenre genre; // Nowe pole enum

    private boolean vinyl; // Informacja, czy to winyl

    // Konstruktor
    public Music(long itemId, int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        super(itemId, basePrice, itemName); // Użycie konstruktora klasy bazowej
        this.genre = genre;
        this.vinyl = vinyl;
    }

    // Domyślny konstruktor
    public Music() {}

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Genre: " + genre + ", Vinyl: " + (vinyl ? "Yes" : "No");
    }

    // Gettery i settery
    public boolean isVinyl() {
        return vinyl;
    }

    public MusicGenre getGenre() {
        return genre; // Dodaj gettera dla genre
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre; // Dodaj settera dla genre
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}
