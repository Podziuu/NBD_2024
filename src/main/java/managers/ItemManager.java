package managers;

import models.*;
import repos.ItemRepository;

import java.util.UUID;

public class ItemManager {
    private final ItemRepository itemRepository;

    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public UUID addMusic(int basePrice, String itemName, MusicGenre genre, boolean vinyl) {
        UUID id = UUID.randomUUID();
        Music music = new Music(id, basePrice, itemName, genre, vinyl);
        //music.setItemType("music");
        itemRepository.addItem(music);
        System.out.println(music.getId());
        return music.getId();
    }

    public UUID addMovie(int basePrice, String itemName, int minutes, boolean casette) {
        UUID id = UUID.randomUUID();
        Movie movie = new Movie(id, basePrice, itemName, minutes, casette);
        movie.setItemType("movie");
        itemRepository.addItem(movie);
        return movie.getId();
    }

    public UUID addComics(int basePrice, String itemName, int pageNumber) {
        UUID id = UUID.randomUUID();
        Comics comics = new Comics(id, basePrice, itemName, pageNumber);
        comics.setItemType("comics");
        itemRepository.addItem(comics);
        return comics.getId();
    }

    public Item getItem(UUID id) {
        Item item = itemRepository.getItem(id);
        if (item == null) {
            throw new NullPointerException("Item not found");
        }
        return mapToSpecificType(item);
    }

    public void updateItem(UUID id, int basePrice, String itemName) {
        Item item = getItem(id);
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

    public void setAvailable(UUID itemId) {
        Item item = itemRepository.getItem(itemId);
        if (item != null) {
            item.setAvailable(true);
            itemRepository.updateItem(item);
        }
    }


    public void setUnavailable(UUID itemId) {
        Item item = itemRepository.getItem(itemId);
        if (item != null) {
            item.setAvailable(false);
            itemRepository.updateItem(item);
        }
    }


    private Item mapToSpecificType(Item item) {
        switch (item.getItemType()) {
            case "music":
                return mapToMusic(item);
            case "movie":
                return mapToMovie(item);
            case "comics":
                return mapToComics(item);
            default:
                throw new IllegalArgumentException(item.getItemType());
        }
    }

    private Music mapToMusic(Item item) {
        if (!(item instanceof Music)) {
            throw new IllegalArgumentException(item.getClass().getSimpleName());
        }
        Music music = (Music) item;
        return music;
    }

    private Movie mapToMovie(Item item) {
        if (!(item instanceof Movie)) {
            throw new IllegalArgumentException(item.getClass().getSimpleName());
        }
        Movie movie = (Movie) item;
        return movie;
    }

    private Comics mapToComics(Item item) {
        if (!(item instanceof Comics)) {
            throw new IllegalArgumentException(item.getClass().getSimpleName());
        }
        Comics comics = (Comics) item;
        return comics;
    }
}