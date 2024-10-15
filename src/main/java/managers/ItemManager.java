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

    public void registerMusic(long itemId, int basePrice, String itemName, MusicGenre genre, boolean vinyl)
            throws ItemAvailableException, LogicException {
        Music music = new Music(itemId, basePrice, itemName, genre, vinyl);
        itemRepository.create(music);
    }

    public void registerMovie(long itemId, int basePrice, String itemName, int minutes, boolean casette)
            throws ItemAvailableException, LogicException {
        Movie movie = new Movie(itemId, basePrice, itemName, minutes, casette);
        itemRepository.create(movie);
    }

    public void registerComics(long itemId, int basePrice, String itemName, int pagesNumber)
            throws ItemAvailableException, LogicException {
        Comics comics = new Comics(itemId, basePrice, itemName, pagesNumber);
        itemRepository.create(comics);
    }

    public Item getItem(long itemId) {
        return itemRepository.get(itemId);
    }

    public void deleteItem(long itemId) throws ItemNotAvailableException {
        if (itemRepository.get(itemId) == null) {
            throw new ItemNotAvailableException();
        }
        Item item = itemRepository.get(itemId);
        itemRepository.delete(item);
    }

    public void updateItem(long id, int basePrice, String itemName,
                           MusicGenre genre, Boolean vinyl,
                           Integer minutes, Boolean casette,
                           Integer pagesNumber) throws ItemNotAvailableException {

        Item item = itemRepository.get(id);
        if (item == null) {
            throw new ItemNotAvailableException("Item not found.");
        }

        item.setBasePrice(basePrice);
        item.setItemName(itemName);

        if (item instanceof Music) {
            Music music = (Music) item;
            if (genre != null) {
                music.setGenre(genre);
            }
            if (vinyl != null) {
                music.setVinyl(vinyl);
            }
        } else if (item instanceof Movie) {
            Movie movie = (Movie) item;
            if (minutes != null) {
                movie.setMinutes(minutes);
            }
            if (casette != null) {
                movie.setCasette(casette);
            }
        } else if (item instanceof Comics) {
            Comics comics = (Comics) item;
            if (pagesNumber != null) {
                comics.setPagesNumber(pagesNumber);
            }
        }
        itemRepository.update(item);
    }
}
