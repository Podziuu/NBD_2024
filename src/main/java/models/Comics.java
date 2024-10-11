package models;

public class Comics extends Item {
    private int pagesNumber;

    public Comics(String itemId, int basePrice, String itemName, int pagesNumber) {
        super(itemId, basePrice, itemName);
        this.pagesNumber = pagesNumber;
    }

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Pages: " + pagesNumber;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }
}
