package managers;

import com.mongodb.client.MongoCollection;
import exceptions.ItemAvailableException;
import exceptions.ItemNotAvailableException;
import exceptions.LogicException;
import models.*;
import repos.ItemRepository;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public long registerMusic(int basePrice, String itemName, boolean vinyl, MusicGenre genre)
            throws ItemAvailableException, LogicException {
        Music music = new Music(basePrice, itemName, vinyl, genre);
        itemRepository.create(music);
        return music.getItemId();
    }

    public long registerMovie(int basePrice, String itemName, int minutes, boolean casette) {
        Movie movie = new Movie(basePrice, itemName, minutes, casette);
        itemRepository.create(movie);
        return movie.getItemId();
    }

    public long registerComics(int basePrice, String itemName, int pagesNumber) {
        Comics comics = new Comics(basePrice, itemName, pagesNumber);
        itemRepository.create(comics);
        return comics.getItemId();
    }

    public Item getItem(long itemId) {
        return itemRepository.read(itemId);
    }

    public void deleteItem(long itemId) throws ItemNotAvailableException {
        Item item = itemRepository.read(itemId);
        if (item == null) {
            throw new ItemNotAvailableException("itemu nie znaleziono");
        }
        itemRepository.delete(itemId);
    }

    public void updateItem(long id, int basePrice, String itemName,
                           MusicGenre genre, Boolean vinyl,
                           Integer minutes, Boolean casette,
                           Integer pagesNumber) throws ItemNotAvailableException {

        Item item = itemRepository.read(id);
        if (item == null) {
            throw new ItemNotAvailableException("itemu nie znaleziono");
        }

        item.setBasePrice(basePrice);
        item.setItemName(itemName);

        switch (item) {
            case Music music -> {
                if (genre != null) {
                    music.setGenre(genre);
                }
                if (vinyl != null) {
                    music.setVinyl(vinyl);
                }
            }
            case Movie movie -> {
                if (minutes != null) {
                    movie.setMinutes(minutes);
                }
                if (casette != null) {
                    movie.setCasette(casette);
                }
            }
            case Comics comics -> {
                if (pagesNumber != null) {
                    comics.setPagesNumber(pagesNumber);
                }
            }
            default -> {
            }
        }

        itemRepository.update(item);
    }

    public MongoCollection<Item> getAllItems() {
        return itemRepository.getItems();
    }
}