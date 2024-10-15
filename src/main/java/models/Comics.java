package models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Comics")
public class Comics extends Item {

    private int pagesNumber;

    public Comics(long itemId, int basePrice, String itemName, int pagesNumber) {
        super(itemId, basePrice, itemName); // Wywołaj konstruktor klasy bazowej
        this.pagesNumber = pagesNumber;
    }

    // Domyślny konstruktor
    public Comics() {}

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Pages: " + pagesNumber;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }
}
