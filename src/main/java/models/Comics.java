package models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Comics")
public class Comics extends Item {

    @Column(name = "pages_number")
    private int pagesNumber;

    public Comics(Long itemId, int basePrice, String itemName, int pagesNumber) {
        super(itemId, basePrice, itemName);
        this.pagesNumber = pagesNumber;
    }

    public Comics() {

    }

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Pages: " + pagesNumber;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }
}
