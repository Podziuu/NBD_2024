package managers;

import models.*;
import org.bson.types.ObjectId;
import repos.ItemRepository;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ObjectId addItem(int basePrice, String itemName, String itemType) {
        ItemType type = getItemType(itemType);
        return itemRepository.addItem(new Item(basePrice, itemName, type));
    }

    public Item getItem(ObjectId id) {
        return itemRepository.getItem(id);
    }

    public void updateItem(ObjectId id, int basePrice, String itemName, String itemType) {
        if (itemRepository.getItem(id) == null) {
            throw new NullPointerException("Item not found");
        }
        Item item = itemRepository.getItem(id);
        item.setBasePrice(basePrice);
        item.setItemName(itemName);
        item.setItemType(getItemType(itemType));
        itemRepository.updateItem(item);
    }

    public void removeItem(ObjectId id) {
        if (itemRepository.getItem(id) == null) {
            throw new NullPointerException("Item not found");
        }
        itemRepository.removeItem(id);
    }

    public void setAvailable(ObjectId id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(true);
        itemRepository.updateItem(item);
    }

    public void setUnavailable(ObjectId id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(false);
        itemRepository.updateItem(item);
    }

    private ItemType getItemType(String itemType) {
        if (itemType == null || itemType.isEmpty()) {
            throw new IllegalArgumentException("Item type cannot be null or empty");
        }

        switch (itemType) {
            case "Comics":
                return new Comics();
            case "Music":
                return new Music();
            case "Movie":
                return new Movie();
            default:
                throw new IllegalArgumentException("Invalid item type: " + itemType);
        }
    }
}

