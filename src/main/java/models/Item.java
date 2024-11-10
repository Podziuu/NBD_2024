package models;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

public class Item {
    //private ObjectId id;
    @BsonId
    private long itemId;
    @BsonProperty("base_price")
    protected int basePrice;
    @BsonProperty("item_name")
    protected String itemName;
    @BsonProperty("available")
    protected boolean available;

    @BsonCreator
    public Item(
            @BsonProperty("base_price") int basePrice,
            @BsonProperty("item_name") String itemName,
            @BsonProperty("available") boolean available
    ) {
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = available;
    }

    public Item() {

    }

    @BsonIgnore
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

    public long getItemId() {
        return itemId;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}