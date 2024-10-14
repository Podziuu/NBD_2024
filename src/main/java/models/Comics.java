package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "comics") // Upewnij się, że nazwa tabeli jest napisana małymi literami
public class Comics extends Item {

    @Column(name = "pages_number")
    private int pagesNumber;

    public Comics(Long itemId, int basePrice, String itemName, int pagesNumber) {
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
