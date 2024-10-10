package models;

public class Item {
    protected String itemId;
    protected int basePrice;
    protected String itemName;
    protected boolean available;

    public Item(String itemId, int basePrice, String itemName) {
        this.itemId = itemId;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true; // Domyślnie dostępny
    }

    public String getItemInfo() {
        return "Item ID: " + itemId + ", Name: " + itemName + ", Base Price: " + basePrice;
    }

    public int getActualPrice() {
        return basePrice;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getItemId() {
        return itemId;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
