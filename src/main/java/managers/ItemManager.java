package managers;

import exceptions.LogicException;
import models.*;
import repos.ItemRepository;
import exceptions.ItemAvailableException;
import exceptions.ItemNotAvailableException;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager() {
        this.itemRepository = new ItemRepository();
    }

    public void registerMusic(Long itemId, int basePrice, String itemName, MusicGenre genre, boolean vinyl)
            throws ItemAvailableException, LogicException {
        Music music = new Music(itemId, basePrice, itemName, genre, vinyl);
        itemRepository.create(music);
    }

    public void registerMovie(Long itemId, int basePrice, String itemName, String genre, int minutes, boolean casette)
            throws ItemAvailableException, LogicException {
        Movie movie = new Movie(itemId, basePrice, itemName, genre, minutes, casette);
        itemRepository.create(movie);
    }

    public void registerComics(Long itemId, int basePrice, String itemName, int pagesNumber)
            throws ItemAvailableException, LogicException {
        Comics comics = new Comics(itemId, basePrice, itemName, pagesNumber);
        itemRepository.create(comics);
    }

    public Item getItem(long itemId) {
        return itemRepository.get(itemId);
    }

    public void deleteItem(Item itemId) throws ItemNotAvailableException {
        if (!itemId.isAvailable())
            if (itemRepository.get(itemId.getItemId()) == null) {
                throw new ItemNotAvailableException();
            }
        Item item = itemRepository.get(itemId.getItemId());
        itemRepository.delete(item);
    }

    public void updateItem() {
        //TODO
    }
}
