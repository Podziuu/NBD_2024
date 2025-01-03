package managers;

import models.*;
import java.util.UUID;
import repos.ItemRepository;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public UUID addMusic(int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        UUID id = UUID.randomUUID();
        Music music = new Music(id, basePrice, itemName, genre, vinyl);
        return itemRepository.addItem(music);
    }

    public UUID addMovie(int basePrice, String itemName, int minutes, boolean casette) {
        UUID id = UUID.randomUUID();
        Movie movie = new Movie(id, basePrice, itemName, minutes, casette);
        return itemRepository.addItem(movie);
    }

    public UUID addComics(int basePrice, String itemName, int pageNumber) {
        UUID id = UUID.randomUUID();
        Comics comics = new Comics(id, basePrice, itemName, pageNumber);
        return itemRepository.addItem(comics);
    }

    public Item getItem(UUID id) {
        return itemRepository.getItem(id);
    }

    public void updateItem(UUID id, int basePrice, String itemName) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setBasePrice(basePrice);
        item.setItemName(itemName);
        itemRepository.updateItem(item);
    }

    public void removeItem(UUID id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        itemRepository.removeItem(id);
    }

    public void setAvailable(UUID id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(true);
        itemRepository.updateItem(item);
    }

    public void setUnavailable(UUID id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        item.setAvailable(false);
        itemRepository.updateItem(item);
    }
}