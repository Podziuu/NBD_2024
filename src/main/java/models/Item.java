package models;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    protected Long itemId;

    @Column(name = "base_price")
    protected int basePrice;

    @Column(name = "item_name")
    protected String itemName;

    protected boolean available;


    public Item(Long itemId, int basePrice, String itemName) {
        this.itemId = itemId;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true;
    }

    public Item() {}

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

    public Long getItemId() {
        return itemId;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
