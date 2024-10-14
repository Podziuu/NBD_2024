package managers;

import exceptions.LogicException;
import models.*;
import repos.ItemRepository; // Zakładając, że istnieje klasa repozytorium dla przedmiotów
import exceptions.ItemAvailableException;
import exceptions.ItemNotAvailableException;

import java.util.List;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager() {
        this.itemRepository = new ItemRepository();
    }

    public void registerMusic(Long itemId, int basePrice, String itemName, MusicGenre genre, boolean vinyl)
            throws ItemAvailableException, LogicException {
        Music music = new Music(itemId, basePrice, itemName, genre, vinyl);
        itemRepository.add(music);
    }

    public void registerMovie(Long itemId, int basePrice, String itemName, String genre, int minutes, boolean casette)
            throws ItemAvailableException, LogicException {
        Movie movie = new Movie(itemId, basePrice, itemName, genre, minutes, casette);
        itemRepository.add(movie);
    }

    public void registerComics(Long itemId, int basePrice, String itemName, int pagesNumber)
            throws ItemAvailableException, LogicException {
        Comics comics = new Comics(itemId, basePrice, itemName, pagesNumber);
        itemRepository.add(comics);
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        List<Item> items = itemRepository.getAll();
        for (Item item : items) {
            report.append(item.getItemInfo()).append("\n");
        }
        return report.toString();
    }

    public Item find(String itemId) {
        return itemRepository.find(itemId);
    }

    public void removeItem(String itemId) throws ItemNotAvailableException {
        if (!find(itemId).isAvailable()) {
            throw new ItemNotAvailableException();
        }
        itemRepository.remove(itemId);
    }
}
