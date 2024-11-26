package managers;

import models.*;
import org.bson.types.ObjectId;
import repos.CachedItemRepository;
import repos.ItemRepository;

public class ItemManager {
    private final ItemRepository itemRepository;
    private final CachedItemRepository cachedItemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.cachedItemRepository = new CachedItemRepository();
    }

    public ObjectId addMusic(int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        return cachedItemRepository.addItem(new Music(basePrice, itemName, genre, vinyl));
    }

    public ObjectId addMovie(int basePrice, String itemName, int minutes, boolean casette) {
        return cachedItemRepository.addItem(new Movie(basePrice, itemName, minutes, casette));
    }

    public ObjectId addComics(int basePrice, String itemName, int pageNumber) {
        return cachedItemRepository.addItem(new Comics(basePrice, itemName, pageNumber));
    }

    public Item getItem(ObjectId id) {
        return cachedItemRepository.getItem(id);
    }

    public void updateItem(ObjectId id, int basePrice, String itemName) {
        if (cachedItemRepository.getItem(id) == null) {
            throw new NullPointerException("Item not found");
        }
        Item item = cachedItemRepository.getItem(id);
        item.setBasePrice(basePrice);
        item.setItemName(itemName);
        cachedItemRepository.updateItem(item);
    }

    public void removeItem(ObjectId id) {
        if (cachedItemRepository.getItem(id) == null) {
            throw new NullPointerException("Item not found");
        }
        cachedItemRepository.removeItem(id);
    }

    public void setAvailable(ObjectId id) {
        Item item = cachedItemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(true);
        cachedItemRepository.updateItem(item);
    }

    public void setUnavailable(ObjectId id) {
        Item item = cachedItemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(false);
        cachedItemRepository.updateItem(item);
    }
}