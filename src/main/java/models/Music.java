package models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Music")
public class Music extends Item {

    @Enumerated(EnumType.STRING)
    private MusicGenre genre;

    private boolean vinyl;
    public Music(int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        super(basePrice, itemName);
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
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void setVinyl(boolean vinyl) {
        this.vinyl = vinyl;
    }
}
