package managers;

import models.Item;
import models.Music;
import models.Movie;
import models.Comics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemManager {
    private List<Item> items;

    public ItemManager() {
        this.items = new ArrayList<>();
    }

    public void registerMusic(String itemId, int basePrice, String itemName, String genre, boolean vinyl) {
        Music music = new Music(itemId, basePrice, itemName, genre, vinyl);
        items.add(music);
    }

    public void registerMovie(String itemId, int basePrice, String itemName, String genre, int minutes, boolean casette) {
        Movie movie = new Movie(itemId, basePrice, itemName, genre, minutes, casette);
        items.add(movie);
    }

    public void registerComics(String itemId, int basePrice, String itemName, int pagesNumber) {
        Comics comics = new Comics(itemId, basePrice, itemName, pagesNumber);
        items.add(comics);
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        for (Item item : items) {
            report.append(item.getItemInfo()).append("\n");
        }
        return report.toString();
    }

    public List<Item> getAll() {
        return items;
    }

    public Optional<Item> find(String itemId) {
        return items.stream().filter(item -> item.getItemId().equals(itemId)).findFirst();
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getItemId().equals(itemId));
    }
}