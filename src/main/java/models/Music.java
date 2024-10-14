package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Music")
public class Music extends Item {

    @Enumerated(EnumType.STRING) // lub EnumType.ORDINAL, zależnie od tego, jak chcesz przechowywać
    private MusicGenre genre; // Nowe pole enum

    private boolean vinyl;

    public Music(Long itemId, int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        super(itemId, basePrice, itemName);
        this.genre = genre;
        this.vinyl = vinyl;
    }

    public Music() {}

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Genre: " + genre + ", Vinyl: " + (vinyl ? "Yes" : "No");
    }

    public boolean isVinyl() {
        return vinyl;
    }

    public MusicGenre getGenre() {
        return genre; // Dodaj gettera dla genre
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre; // Dodaj settera dla genre
    }
}
