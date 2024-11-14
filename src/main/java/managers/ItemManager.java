package managers;

import models.*;
import org.bson.types.ObjectId;
import repos.ItemRepository;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ObjectId addMusic(int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        return itemRepository.addItem(new Music(basePrice, itemName, genre, vinyl));
    }

    public ObjectId addMovie(int basePrice, String itemName, int minutes, boolean casette) {
        return itemRepository.addItem(new Movie(basePrice, itemName, minutes, casette));
    }

    public ObjectId addComics(int basePrice, String itemName, int pageNumber) {
        return itemRepository.addItem(new Comics(basePrice, itemName, pageNumber));
    }

    public Item getItem(ObjectId id) {
        return itemRepository.getItem(id);
    }

    public void updateItem(ObjectId id, int basePrice, String itemName) {
        if (itemRepository.getItem(id) == null) {
            throw new NullPointerException("Item not found");
        }
        Item item = itemRepository.getItem(id);
        item.setBasePrice(basePrice);
        item.setItemName(itemName);
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
}