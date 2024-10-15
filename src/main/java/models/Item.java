package models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generowanie wartości identyfikatora
    private long id;

    @Column(name = "base_price") // Nazwa kolumny w bazie danych
    private int basePrice;

    @Column(name = "item_name") // Nazwa kolumny w bazie danych
    private String itemName;
    private boolean available;

    @Version
    private int version;

    public Item(long itemId, int basePrice, String itemName) {
        this.id = itemId;
        this.basePrice = basePrice;
        this.itemName = itemName;
    }

    public Item() {}

    public String getItemInfo() {
        return "ID: " + id + ", Name: " + itemName + ", Base Price: " + basePrice + ", Available: " + available;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getActualPrice() {
        return this.getBasePrice();
    }
}
