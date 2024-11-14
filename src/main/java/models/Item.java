package models;

import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;

public class Item {
    @BsonId
    private ObjectId id;
    @BsonProperty("basePrice")
    protected int basePrice;
    @BsonProperty("itemName")
    protected String itemName;
    @BsonProperty("available")
    protected boolean available;
    @BsonProperty("itemType")
    private ItemType itemType;

    @BsonCreator
    public Item(
            @BsonProperty("basePrice") int basePrice,
            @BsonProperty("itemName") String itemName,
            @BsonProperty("itemType") ItemType itemType) {
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true;
        this.itemType = itemType;
    }

    public Item() {

    }

    @BsonIgnore
    public String getItemInfo() {
        return "Item ID: " + id + ", Name: " + itemName + ", Base Price: " + basePrice;
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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}