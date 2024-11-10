import com.mongodb.client.MongoCollection;
import exceptions.ItemAvailableException;
import exceptions.ItemNotAvailableException;
import exceptions.LogicException;
import managers.ItemManager;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repos.ItemRepository;

import static org.junit.jupiter.api.Assertions.*;

public class ItemManagerTest {

    public static ItemRepository itemRepository;
    public static ItemManager itemManager;

    @BeforeAll
    public static void setUp() {
        itemRepository = new ItemRepository();
        itemManager = new ItemManager(itemRepository);
    }

    @AfterEach
    void dropDatabase() {
        MongoCollection<Item> collection = itemRepository.getItems();
        collection.drop();
    }

    @Test
    void registerMusicTest() throws ItemAvailableException, LogicException {
        MusicGenre genre = MusicGenre.Jazz;

        long id = itemManager.registerMusic(100, "Kizo Bengier", true, genre);

        Item registeredMusic = itemManager.getItem(id);
        assertNotNull(registeredMusic);
        assertEquals("Kizo Bengier", registeredMusic.getItemName());
    }

    @Test
    void registerMovieTest() {
        long id = itemManager.registerMovie(120, "za szybcy za wsciekli", 120, true);

        Item registeredMovie = itemManager.getItem(id);
        assertNotNull(registeredMovie);
        assertEquals("za szybcy za wsciekli", registeredMovie.getItemName());
    }

    @Test
    void registerComicsTest() {
        long id = itemManager.registerComics(50, "scooby doo czlowieku", 100);

        Item registeredComics = itemManager.getItem(id);
        assertNotNull(registeredComics);
        assertEquals("scooby doo czlowieku", registeredComics.getItemName());
    }

    @Test
    void deleteItemTest() throws ItemNotAvailableException, ItemAvailableException, LogicException {
        long id = itemManager.registerMusic(100, "Kizo Bengier", true, MusicGenre.Jazz);

        Item item = itemManager.getItem(id);
        assertNotNull(item);

        itemManager.deleteItem(id);

        Item deletedItem = itemManager.getItem(id);
        assertNull(deletedItem);
    }

    @Test
    void deleteItemNotAvailableTest() {
        Exception exception = assertThrows(ItemNotAvailableException.class, () -> {
            itemManager.deleteItem(9999999L);
        });

        assertEquals("itemu nie znaleziono", exception.getMessage());
    }

    @Test
    void updateMusicTest() throws ItemAvailableException, LogicException, ItemNotAvailableException {
        long id = itemManager.registerMusic(100, "Kizo Bengier", true, MusicGenre.Jazz);

        itemManager.updateItem(id, 120, "OIO", MusicGenre.Metal, false, null, false, null);

        Item updatedItem = itemManager.getItem(id);
        assertNotNull(updatedItem);
        assertEquals("OIO", updatedItem.getItemName());
    }

    @Test
    void updateMovieTest() throws ItemNotAvailableException {
        long id = itemManager.registerMovie(120, "za szybcy za wsciekli", 120, true);

        itemManager.updateItem(id, 140, "rambo", null,null, 150, false, null);

        Item updatedItem = itemManager.getItem(id);
        assertNotNull(updatedItem);
        assertEquals("rambo", updatedItem.getItemName());
    }

    @Test
    void updateComicsTest() throws ItemNotAvailableException {
        long id = itemManager.registerComics(50, "scooby doo", 100);

        itemManager.updateItem(id, 60, "tytus", null, null, null, null, 120);

        Item updatedItem = itemManager.getItem(id);
        assertNotNull(updatedItem);
        assertEquals("tytus", updatedItem.getItemName());
    }
}
