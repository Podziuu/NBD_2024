package models;

import com.datastax.oss.driver.api.mapper.annotations.*;
import java.util.UUID;

@Entity
@CqlName("items_by_id")
public class Item {
    @PartitionKey
    @CqlName("item_id")
    private UUID id;

    @CqlName("base_price")
    protected int basePrice;

    @CqlName("item_name")
    protected String itemName;

    @CqlName("available")
    protected boolean available;

    @ClusteringColumn(0)
    @CqlName("item_type")
    protected String itemType;

    public Item(UUID id, int basePrice, String itemName) {
        this.id = id;
        this.basePrice = basePrice;
        this.itemName = itemName;
        this.available = true;
    }

    public Item() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemInfo() {
        return "Item ID: " + id + ", Name: " + itemName + ", Base Price: " + basePrice;
    }

    public int getActualPrice() {
        return basePrice;
    }
}