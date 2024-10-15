package models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Comics")
public class Comics extends Item {

    private int pagesNumber;

    public Comics(int basePrice, String itemName, int pagesNumber) {
        super(basePrice, itemName);
        this.pagesNumber = pagesNumber;
    }

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
